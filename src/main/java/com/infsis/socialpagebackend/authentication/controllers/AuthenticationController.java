package com.infsis.socialpagebackend.authentication.controllers;


import com.infsis.socialpagebackend.authentication.dtos.AuthResponseDTO;
import com.infsis.socialpagebackend.authentication.dtos.ForgotPasswordDTO;
import com.infsis.socialpagebackend.authentication.dtos.ResetPasswordDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserLoginDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserRegistryDTO;
import com.infsis.socialpagebackend.authentication.services.EmailVerificationService;
import com.infsis.socialpagebackend.authentication.services.PasswordResetService;
import com.infsis.socialpagebackend.authentication.models.Token;
import com.infsis.socialpagebackend.authentication.models.Role;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.TokenRepository;
import com.infsis.socialpagebackend.authentication.repositories.RoleRepository;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.multitenant.TenantResolver;
import com.infsis.socialpagebackend.security.JwtGenerator;
import com.infsis.socialpagebackend.authentication.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private static final String PASSWORD_INVALID_MATCHING_MESSAGE = "Passwords don't match, try again";
    private static final String REGISTERED_USER_EMAIL_MESSAGE = "The user email is already registered";
    private static final String CLOSED_USER_SESSION_MESSAGE = "User session closed successfully";
    private static final String ROLE_STUDENT = "STUDENT";
    private static final String REFRESH_COOKIE_NAME = "refresh_token";

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Value("${app.auth.cookie.secure:false}")
    private boolean cookieSecure;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private TenantResolver tenantResolver;

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private RoleRepository rolesRepository;
    private UserRepository usuariosRepository;
    private JwtGenerator jwtGenerador;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleRepository rolesRepository, UserRepository usuariosRepository, JwtGenerator jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
        this.jwtGenerador = jwtGenerador;
    }

    //Método para poder registrar usuarios con role "user"
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registrar(
            @Valid @RequestBody UserRegistryDTO userRegistryDTO,
            @RequestHeader(value = "X-Tenant-Slug", required = false) String tenantSlug) {

        String tenantId = tenantResolver.resolveOrThrow(tenantSlug);

        if (!userRegistryDTO.getPassword().equals(userRegistryDTO.getRepeat_password())) {
            return new ResponseEntity<>(Collections.singletonMap("message", PASSWORD_INVALID_MATCHING_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        if (usuariosRepository.existsByEmail(userRegistryDTO.getEmail())) {
            return new ResponseEntity<>(Collections.singletonMap("message", REGISTERED_USER_EMAIL_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        Users usuarios = new Users();
        usuarios.setName(userRegistryDTO.getName());
        usuarios.setLastName(userRegistryDTO.getLastName());
        usuarios.setEmail(userRegistryDTO.getEmail());
        usuarios.setPhone(userRegistryDTO.getPhone());
        usuarios.setPassword(passwordEncoder.encode(userRegistryDTO.getPassword()));
        usuarios.setInstitutionId(tenantId);

        Role roles = rolesRepository.findByName(ROLE_STUDENT).get();
        usuarios.setRoles(Collections.singletonList(roles));
        usuariosRepository.save(usuarios);

        try {
            emailVerificationService.generateAndSend(usuarios);
        } catch (Exception e) {
            log.warn("No se pudo enviar el email de verificación a {}: {}", usuarios.getEmail(), e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("message",
                    "Usuario registrado, pero no se pudo enviar el email de verificación. Contacta al administrador."),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(Collections.singletonMap("message",
                "Te enviamos un email para verificar tu cuenta."), HttpStatus.OK);
    }

    @PostMapping("/auth/root/login")
    public ResponseEntity<AuthResponseDTO> rootLogin(
            @RequestBody UserLoginDTO userLoginDTO,
            HttpServletResponse httpResponse) {
        AuthResponseDTO response = authenticationService.rootLogin(userLoginDTO);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString());
        response.setRefreshToken(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody UserLoginDTO userLoginDTO,
            @RequestHeader(value = "X-Tenant-Slug", required = false) String tenantSlug,
            HttpServletResponse httpResponse) {
        String tenantId = tenantResolver.resolveOrThrow(tenantSlug);
        AuthResponseDTO response = authenticationService.login(userLoginDTO, tenantId);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString());
        response.setRefreshToken(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse httpResponse) {
        if (refreshToken != null) {
            authenticationService.logout(refreshToken);
        }
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString());
        SecurityContextHolder.clearContext();
        Map<String, Object> response = new HashMap<>();
        response.put("message", CLOSED_USER_SESSION_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse httpResponse) {
        if (refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        AuthResponseDTO response = authenticationService.refreshToken(refreshToken);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString());
        response.setRefreshToken(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/v1/users/me")
    public UserDetailDTO getUserFromToken() {
        return authenticationService.getUserDetails();
    }

    @PutMapping("/v1/users/me")
    public UserDetailDTO updateUserProfile(@Valid @RequestBody UserDetailDTO userDetailDTO) {
        return authenticationService.updateUserProfile(userDetailDTO);
    }

    // ─── Admin user management — ROOT only ───────────────────────────────────

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @GetMapping("/v1/users/admin")
    public ResponseEntity<java.util.List<UserDetailDTO>> getAdminUsers(
            @RequestParam(required = false) String institutionId) {
        return new ResponseEntity<>(authenticationService.getAdminUsers(institutionId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @PutMapping("/v1/users/admin/{uuid}")
    public ResponseEntity<UserDetailDTO> updateAdminUser(
            @PathVariable String uuid,
            @Valid @RequestBody UserDetailDTO userDetailDTO) {
        return new ResponseEntity<>(authenticationService.updateAdminUser(uuid, userDetailDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @DeleteMapping("/v1/users/admin/{uuid}")
    public ResponseEntity<Map<String, Object>> deleteAdminUser(@PathVariable String uuid) {
        authenticationService.deleteAdminUser(uuid);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin user deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/auth/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        emailVerificationService.verify(token);
        return ResponseEntity.ok(Collections.singletonMap("message",
                "Email verificado correctamente. Ya puedes iniciar sesión."));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
        passwordResetService.requestReset(dto.email());
        return ResponseEntity.ok(Collections.singletonMap("message",
                "Si el email existe, recibirás un enlace para restablecer tu contraseña."));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        passwordResetService.resetPassword(dto.token(), dto.newPassword());
        return ResponseEntity.ok(Collections.singletonMap("message",
                "Contraseña actualizada correctamente."));
    }

    // ─── Cookie helpers ──────────────────────────────────────────────────────

    private ResponseCookie buildRefreshCookie(String token) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(Duration.ofMillis(refreshExpirationTime))
                .build();
    }

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(0)
                .build();
    }
}