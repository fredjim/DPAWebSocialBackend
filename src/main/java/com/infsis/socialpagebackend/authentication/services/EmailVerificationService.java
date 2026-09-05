package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.models.EmailVerificationToken;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.EmailVerificationTokenRepository;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.security.CustomCorsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    // Usado solo cuando la petición no trae un Origin confiable (ej. llamada no-browser).
    @Value("${app.frontend.fallback-url}")
    private String fallbackFrontendUrl;

    @Value("${spring.mail.from}")
    private String mailFrom;

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final CustomCorsConfiguration corsConfiguration;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository,
                                    UserRepository userRepository,
                                    JavaMailSender mailSender,
                                    CustomCorsConfiguration corsConfiguration) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.corsConfiguration = corsConfiguration;
    }

    /**
     * @param requestOrigin header "Origin" de la petición de registro (ej. "https://dpa.umss.dev").
     *                      Si es un origen permitido (misma lista que valida CORS), el link de
     *                      verificación apunta al subdominio real del tenant del usuario. Si no,
     *                      cae al fallback configurado.
     */
    public void generateAndSend(Users user, String requestOrigin) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken entity = new EmailVerificationToken();
        entity.setToken(token);
        entity.setUser(user);
        entity.setExpiresAt(LocalDateTime.now().plusHours(24));
        tokenRepository.save(entity);

        String base = resolveFrontendBase(requestOrigin);
        String link = base + "/verify-email?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(user.getEmail());
        message.setSubject("Verifica tu cuenta");
        message.setText("Haz clic en el siguiente enlace para verificar tu email:\n\n"
                + link + "\n\nEl enlace expira en 24 horas.");
        mailSender.send(message);
    }

    public void verify(String token) {
        EmailVerificationToken entity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (entity.isUsed())
            throw new IllegalArgumentException("El token ya fue utilizado.");
        if (entity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("El token ha expirado.");

        entity.setUsed(true);
        tokenRepository.save(entity);

        Users user = entity.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    private String resolveFrontendBase(String requestOrigin) {
        if (requestOrigin != null && corsConfiguration.isAllowedOrigin(requestOrigin)) {
            return requestOrigin;
        }
        return fallbackFrontendUrl;
    }
}
