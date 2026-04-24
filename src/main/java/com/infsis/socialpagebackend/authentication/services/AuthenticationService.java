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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            UserMapper userMapper,
            AuthenticationManager authenticationManager,
            JwtGenerator jwtGenerator,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
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

    /**
     * Login exclusivo para usuarios ROOT. No requiere tenant.
     */
    public AuthResponseDTO rootLogin(UserLoginDTO userLoginDTO) {
        authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        Users user = findUserByEmail(userLoginDTO.getEmail());

        if (!user.isRoot()) {
            throw new IllegalArgumentException("Acceso denegado: este endpoint es exclusivo para usuarios ROOT.");
        }

        String accessToken = jwtGenerator.generarAccessToken(user);
        String refreshToken = jwtGenerator.generarRefreshToken(user);
        revokeAllUserRefreshTokens(user);
        saveRefreshTokenToDatabase(user, refreshToken);
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    /**
     * Login: autentica usuario, valida que pertenezca al tenant, genera tokens y revoca los anteriores.
     */
    public AuthResponseDTO login(UserLoginDTO userLoginDTO, String tenantId) {
        authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        Users user = findUserByEmail(userLoginDTO.getEmail());

        if (user.getInstitutionId() == null || !user.getInstitutionId().equals(tenantId)) {
            throw new IllegalArgumentException("El usuario no pertenece a esta institución.");
        }

        String accessToken = jwtGenerator.generarAccessToken(user);
        String refreshToken = jwtGenerator.generarRefreshToken(user);
        revokeAllUserRefreshTokens(user);
        saveRefreshTokenToDatabase(user, refreshToken);
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    /**
     * Valida que el token exista, no esté revocado y no haya expirado.
     * Lanza IllegalArgumentException con mensaje claro si falla alguna condición.
     */
    private Token validarTokenActivo(String token) {
        Token tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("El token no existe en la base de datos."));
        if (tokenEntity.getIsRevoked()) {
            throw new IllegalArgumentException("El token ha sido revocado.");
        }
        try {
            if (!jwtGenerator.validarToken(token)) {
                throw new IllegalArgumentException("El token ha expirado o es inválido.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("El token ha expirado o es inválido.");
        }
        return tokenEntity;
    }

    /**
     * Refresh token: valida el refresh token, genera nuevos tokens y revoca el anterior.
     */
    public AuthResponseDTO refreshToken(String refreshTokenHeader) {
        String refreshToken = extractToken(refreshTokenHeader);
        Token tokenEntity = validarTokenActivo(refreshToken);
        if (!isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Solo se permite el uso de refresh token en este endpoint.");
        }
        String userEmail = jwtGenerator.obtenerUsernameDeJwt(refreshToken);
        if (userEmail == null) {
            throw new IllegalArgumentException("No se encontró el email en el token.");
        }
        Users user = findUserByEmail(userEmail);
        if (!jwtGenerator.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("El refresh token es inválido o ha expirado.");
        }
        String accessToken = jwtGenerator.generarAccessToken(user);
        String newRefreshToken = jwtGenerator.generarRefreshToken(user);
        tokenEntity.setIsRevoked(true);
        tokenRepository.save(tokenEntity);
        saveRefreshTokenToDatabase(user, newRefreshToken);
        return new AuthResponseDTO(accessToken, newRefreshToken);
    }

    /**
     * Logout: revoca el refresh token aunque esté expirado, si existe y no está ya revocado.
     */
    public void logout(String refreshTokenHeader) {
        String refreshToken = extractToken(refreshTokenHeader);
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("El token no existe en la base de datos."));
        if (tokenEntity.getIsRevoked()) {
            throw new IllegalArgumentException("El token ya ha sido revocado.");
        }
        tokenEntity.setIsRevoked(true);
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

    /**
     * Revoca todos los refresh tokens válidos del usuario.
     */
    private void revokeAllUserRefreshTokens(Users user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> token.setIsRevoked(true));
            tokenRepository.saveAll(validUserTokens);
        }
    }

    /**
     * Guarda el refresh token en la base de datos.
     */
    private void saveRefreshTokenToDatabase(Users user, String refreshToken) {
        Token tokenEntity = Token.builder()
                .token(refreshToken)
                .isRevoked(false)
                .user(user)
                .build();
        tokenRepository.save(tokenEntity);
    }

    /**
     * Extrae el token del header Authorization.
     */
    private String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("El formato del token es inválido.");
        }
        return header.substring(7);
    }

    /**
     * Valida si el token es de tipo refresh (puedes mejorar con claims personalizados).
     */
    private boolean isRefreshToken(String token) {
        // Mejorable: puedes agregar un claim "type" en el JWT y verificarlo aquí
        return true;
    }

    private void updateUserFields(Users user, UserDetailDTO dto) {
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getPhoto_profile_path() != null) user.setPhoto_profile_path(dto.getPhoto_profile_path());
        if (dto.getPhoto_cover_path() != null) user.setPhoto_cover_path(dto.getPhoto_cover_path());
    }

    // ─── Admin user management (ROOT only) ───────────────────────────────────

    public List<UserDetailDTO> getAdminUsers() {
        return userRepository.findAllByRoleName("ADMIN")
                .stream()
                .map(u -> {
                    UserDetailDTO dto = userMapper.toDTO(u);
                    dto.setRole("ADMIN");
                    return dto;
                })
                .toList();
    }

    public UserDetailDTO updateAdminUser(String uuid, UserDetailDTO dto) {
        Users admin = userRepository.findByUuidAndRoleName(uuid, "ADMIN")
                .orElseThrow(() -> new NotFoundException("Admin user", uuid));
        updateAdminFields(admin, dto);
        userRepository.save(admin);
        UserDetailDTO result = userMapper.toDTO(admin);
        result.setRole("ADMIN");
        return result;
    }

    public void deleteAdminUser(String uuid) {
        Users admin = userRepository.findByUuidAndRoleName(uuid, "ADMIN")
                .orElseThrow(() -> new NotFoundException("Admin user", uuid));
        userRepository.delete(admin);
    }

    private void updateAdminFields(Users user, UserDetailDTO dto) {
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getInstitutionId() != null) user.setInstitutionId(dto.getInstitutionId());
    }
}