package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.dtos.AuthResponseDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserLoginDTO;
import com.infsis.socialpagebackend.authentication.mappers.UserMapper;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.infsis.socialpagebackend.authentication.models.Token;
import com.infsis.socialpagebackend.authentication.repositories.TokenRepository;
import java.util.List;

@Component
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    public UserDetailDTO updateUserProfile(UserDetailDTO userDetailDTO) {

        Users currentUser = getCurrentUser();

        currentUser.setEmail(userDetailDTO.getEmail());
        currentUser.setName(userDetailDTO.getName() != null ? userDetailDTO.getName() : currentUser.getEmail());
        currentUser.setLastName(userDetailDTO.getLastName() != null ? userDetailDTO.getLastName() : currentUser.getLastName());
        currentUser.setPassword(userDetailDTO.getPassword() != null ? userDetailDTO.getPassword() : currentUser.getPassword());
        currentUser.setPhone(userDetailDTO.getPhone() != null ? userDetailDTO.getPhone() : currentUser.getPhone());
        currentUser.setPhoto_profile_path(userDetailDTO.getPhoto_profile_path() != null ? userDetailDTO.getPhoto_profile_path() : currentUser.getPhoto_profile_path());
        currentUser.setPhoto_cover_path(userDetailDTO.getPhoto_cover_path() != null ? userDetailDTO.getPhoto_cover_path() : currentUser.getPhoto_cover_path());

        userRepository.save(currentUser);

        return userMapper.toDTO(currentUser);
    }

    public UserDetailDTO getUserDetails() {
        Users currentUser = getCurrentUser();
        UserDetailDTO userDetailDTO = userMapper.toDTO(currentUser);
        // Asumiendo que el usuario tiene un solo rol
        userDetailDTO.setRole(currentUser.getRoles().get(0).getName());
        return userDetailDTO;
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: ", email));
    }
    public AuthResponseDTO login(UserLoginDTO userLoginDTO) {
        authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());

        Users user = findUserByEmail(userLoginDTO.getEmail());
        String accessToken = jwtGenerator.generarAccessToken(user);
        String refreshToken = jwtGenerator.generarRefreshToken(user);

        revokeAllUserRefreshTokens(user);
        saveRefreshTokenToDatabase(user, refreshToken);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO refreshToken(String refreshTokenHeader) {
        String refreshToken = extractToken(refreshTokenHeader);

        // Buscar el refresh token en la base de datos
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in database"));

        // Validar que el token no esté revocado ni expirado
        if (tokenEntity.getIsRevoked() || tokenEntity.getIsExpired()) {
            throw new IllegalArgumentException("Refresh token is revoked or expired");
        }

        // Validar que el token sea realmente un refresh token (no access token)
        long expirationTime = jwtGenerator.getExpirationTime();
        long refreshExpirationTime = jwtGenerator.getRefreshExpirationTime();
        if (tokenEntity.getIsExpired() || expirationTime == refreshExpirationTime) {
            // Si el token está expirado o no es refresh, rechazar
            throw new IllegalArgumentException("Invalid token type for refresh endpoint");
        }

        validateToken(refreshToken);

        String userEmail = jwtGenerator.obtenerUsernameDeJwt(refreshToken);
        if (userEmail == null) {
            throw new IllegalArgumentException("User email not found in token");
        }

        Users user = findUserByEmail(userEmail);

        if (!jwtGenerator.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String accessToken = jwtGenerator.generarAccessToken(user);
        String newRefreshToken = jwtGenerator.generarRefreshToken(user);

        // Revocar el refresh token anterior
        tokenEntity.setIsRevoked(true);
        tokenEntity.setIsExpired(true);
        tokenRepository.save(tokenEntity);

        // Guardar el nuevo refresh token
        saveRefreshTokenToDatabase(user, newRefreshToken);

        return new AuthResponseDTO(accessToken, newRefreshToken);
    }

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private Users findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    // Revoca todos los refresh tokens válidos del usuario
    private void revokeAllUserRefreshTokens(Users user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setIsExpired(true);
                token.setIsRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    // Guarda el refresh token en la base de datos
    private void saveRefreshTokenToDatabase(Users user, String refreshToken) {
        Token tokenEntity = Token.builder()
                .token(refreshToken)
                .isRevoked(false)
                .isExpired(false)
                .user(user)
                .build();
        tokenRepository.save(tokenEntity);
    }

    private String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        return header.substring(7);
    }

    private void validateToken(String token) {
        if (!jwtGenerator.validarToken(token)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
    }

    public void logout(String refreshTokenHeader) {
        String refreshToken = extractToken(refreshTokenHeader);
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in database"));

        // Validar que el token sea realmente un refresh token
        long expirationTime = jwtGenerator.getExpirationTime();
        long refreshExpirationTime = jwtGenerator.getRefreshExpirationTime();
        if (expirationTime == refreshExpirationTime) {
            throw new IllegalArgumentException("Solo se permite el uso de refresh token para cerrar sesión.");
        }

        tokenEntity.setIsRevoked(true);
        // Si no ha expirado, mantener isExpired=false
        if (!jwtGenerator.isTokenValid(refreshToken, tokenEntity.getUser())) {
            tokenEntity.setIsExpired(true);
        }
        tokenRepository.save(tokenEntity);
    }
}
