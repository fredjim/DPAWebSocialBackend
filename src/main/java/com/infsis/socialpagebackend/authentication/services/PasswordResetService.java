package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.security.ConstantsSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Base64;

@Service
public class PasswordResetService {

    @Value("${app.email.reset-url}")
    private String resetUrl;

    @Value("${spring.mail.from}")
    private String mailFrom;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void requestReset(String email) {
        // Siempre responde OK — nunca revela si el email existe (anti-enumeration)
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = buildToken(user);
            String link  = resetUrl + "?token=" + token;

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(mailFrom);
            msg.setTo(user.getEmail());
            msg.setSubject("Restablecer contraseña");
            msg.setText("Solicitaste restablecer tu contraseña.\n\n"
                    + "Haz clic aquí (expira en 1 hora):\n\n" + link
                    + "\n\nSi no fuiste tú, ignora este email.");
            mailSender.send(msg);
        });
    }

    public void resetPassword(String token, String newPassword) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) throw new IllegalArgumentException("Token inválido.");

        String encodedPayload = parts[0];
        String receivedSig    = parts[1];

        String payload;
        try {
            payload = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token inválido.");
        }

        String[] fields = payload.split("\\|");
        if (fields.length != 3 || !"RESET_PASSWORD".equals(fields[2]))
            throw new IllegalArgumentException("Token inválido.");

        int  userId     = Integer.parseInt(fields[0]);
        long expiration = Long.parseLong(fields[1]);

        if (System.currentTimeMillis() > expiration)
            throw new IllegalArgumentException("El token ha expirado.");

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        String expectedSig = hmac(encodedPayload, user.getPassword() + ConstantsSecurity.JWT_FIRMA);
        if (!MessageDigest.isEqual(
                receivedSig.getBytes(StandardCharsets.UTF_8),
                expectedSig.getBytes(StandardCharsets.UTF_8)))
            throw new IllegalArgumentException("Token inválido.");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private String buildToken(Users user) {
        long expiration   = System.currentTimeMillis() + Duration.ofHours(1).toMillis();
        String payload    = user.getId() + "|" + expiration + "|RESET_PASSWORD";
        String encoded    = Base64.getUrlEncoder().withoutPadding()
                                  .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature  = hmac(encoded, user.getPassword() + ConstantsSecurity.JWT_FIRMA);
        return encoded + "." + signature;
    }

    private String hmac(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                         .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
