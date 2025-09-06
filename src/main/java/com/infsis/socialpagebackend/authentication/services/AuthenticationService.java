package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.dtos.AuthResponseDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserLoginDTO;
import com.infsis.socialpagebackend.authentication.mappers.UserMapper;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.models.Token;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.authentication.repositories.TokenRepository;
import com.infsis.socialpagebackend.security.JwtGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    public AuthenticationService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            UserMapper userMapper,
            AuthenticationManager authenticationManager,
            JwtGenerator jwtGenerator
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    public UserDetailDTO updateUserProfile(UserDetailDTO userDetailDTO) {
        Users currentUser = getCurrentUser();
        updateUserFields(currentUser, userDetailDTO);
        userRepository.save(currentUser);
        return userMapper.toDTO(currentUser);
    }

    public UserDetailDTO getUserDetails() {
        Users currentUser = getCurrentUser();
        UserDetailDTO userDetailDTO = userMapper.toDTO(currentUser);
        userDetailDTO.setRole(currentUser.getRoles().get(0).getName());
        return userDetailDTO;
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
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in database"));

        if (tokenEntity.getIsRevoked() || tokenEntity.getIsExpired()) {
            throw new IllegalArgumentException("Refresh token is revoked or expired");
        }

        if (!isRefreshToken(tokenEntity)) {
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

        tokenEntity.setIsRevoked(true);
        tokenEntity.setIsExpired(true);
        tokenRepository.save(tokenEntity);

        saveRefreshTokenToDatabase(user, newRefreshToken);

        return new AuthResponseDTO(accessToken, newRefreshToken);
    }

    public void logout(String refreshTokenHeader) {
        String refreshToken = extractToken(refreshTokenHeader);
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in database"));

        if (!isRefreshToken(tokenEntity)) {
            throw new IllegalArgumentException("Solo se permite el uso de refresh token para cerrar sesión.");
        }

        tokenEntity.setIsRevoked(true);
        if (!jwtGenerator.isTokenValid(refreshToken, tokenEntity.getUser())) {
            tokenEntity.setIsExpired(true);
        }
        tokenRepository.save(tokenEntity);
    }

    // --- Métodos privados ---

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: ", email));
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

    private boolean isRefreshToken(Token tokenEntity) {
        long expirationTime = jwtGenerator.getExpirationTime();
        long refreshExpirationTime = jwtGenerator.getRefreshExpirationTime();
        return !tokenEntity.getIsExpired() && expirationTime != refreshExpirationTime;
    }

    private void updateUserFields(Users user, UserDetailDTO dto) {
        user.setEmail(dto.getEmail());
        user.setName(dto.getName() != null ? dto.getName() : user.getEmail());
        user.setLastName(dto.getLastName() != null ? dto.getLastName() : user.getLastName());
        user.setPassword(dto.getPassword() != null ? dto.getPassword() : user.getPassword());
        user.setPhone(dto.getPhone() != null ? dto.getPhone() : user.getPhone());
        user.setPhoto_profile_path(dto.getPhoto_profile_path() != null ? dto.getPhoto_profile_path() : user.getPhoto_profile_path());
        user.setPhoto_cover_path(dto.getPhoto_cover_path() != null ? dto.getPhoto_cover_path() : user.getPhoto_cover_path());
    }
}
