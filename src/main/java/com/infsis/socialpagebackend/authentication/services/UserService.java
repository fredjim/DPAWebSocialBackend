package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.dtos.CreateUserDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.mappers.UserMapper;
import com.infsis.socialpagebackend.authentication.models.Permissions;
import com.infsis.socialpagebackend.authentication.models.Role;
import com.infsis.socialpagebackend.authentication.models.Token;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.RoleRepository;
import com.infsis.socialpagebackend.authentication.repositories.TokenRepository;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, UserMapper userMapper,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tokenRepository = tokenRepository;
    }

    public UserDetailDTO createUser(CreateUserDTO dto, boolean callerIsRoot, String callerInstitutionId) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("The email is already registered.");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role", String.valueOf(dto.getRoleId())));

        if (!callerIsRoot) {
            if (role.isSystemRole() && role.getName().equals("ROOT")) {
                throw new IllegalArgumentException("Only root users can assign the ROOT role.");
            }
            boolean hasSystemPerms = role.getPermisos().stream()
                    .anyMatch(Permissions::isSystemOnly);
            if (hasSystemPerms) {
                throw new IllegalArgumentException("The selected role contains system-only permissions.");
            }
        }

        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singletonList(role));
        user.setEmailVerified(callerIsRoot);

        if (callerIsRoot) {
            if (!role.getName().equals("ROOT") && dto.getInstitutionId() == null) {
                throw new IllegalArgumentException("You must specify institutionId when creating a non-ROOT user.");
            }
            user.setInstitutionId(dto.getInstitutionId());
        } else {
            user.setInstitutionId(callerInstitutionId);
        }

        userRepository.save(user);

        UserDetailDTO result = userMapper.toDTO(user);
        result.setRole(role.getName());
        return result;
    }

    // ── Gestión genérica de usuarios ─────────────────────────────────────────

    public Page<UserDetailDTO> listUsers(String roleFilter, Boolean enabled, String search, String requestedInstitutionId,
                                          boolean callerIsRoot, String callerInstitutionId,
                                          Pageable pageable) {
        String institutionId = callerIsRoot ? requestedInstitutionId : callerInstitutionId;
        return userRepository
                .findAllWithFilters(institutionId, roleFilter, enabled, search, pageable)
                .map(userMapper::toDTO);
    }

    public UserDetailDTO getUser(String uuid, boolean callerIsRoot, String callerInstitutionId) {
        return userMapper.toDTO(findManageableUser(uuid, callerIsRoot, callerInstitutionId));
    }

    public UserDetailDTO updateUser(String uuid, UserDetailDTO dto,
                                    boolean callerIsRoot, String callerInstitutionId) {
        Users target = findManageableUser(uuid, callerIsRoot, callerInstitutionId);
        guardSelf(target.getUuid());

        if (dto.getName()     != null) target.setName(dto.getName());
        if (dto.getLastName() != null) target.setLastName(dto.getLastName());
        if (dto.getEmail()    != null) target.setEmail(dto.getEmail());
        if (dto.getPhone()    != null) target.setPhone(dto.getPhone());
        if (dto.getPassword() != null) target.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (callerIsRoot && dto.getInstitutionId() != null) target.setInstitutionId(dto.getInstitutionId());

        userRepository.save(target);
        return userMapper.toDTO(target);
    }

    public UserDetailDTO toggleDisable(String uuid, boolean callerIsRoot, String callerInstitutionId) {
        Users target = findManageableUser(uuid, callerIsRoot, callerInstitutionId);
        guardSelf(target.getUuid());

        target.setEnabled(!target.isEnabled());
        userRepository.save(target);

        if (!target.isEnabled()) {
            List<Token> tokens = tokenRepository.findAllValidTokenByUser(target.getId());
            if (!tokens.isEmpty()) {
                tokens.forEach(t -> t.setIsRevoked(true));
                tokenRepository.saveAll(tokens);
            }
        }
        return userMapper.toDTO(target);
    }

    public void deleteUser(String uuid, boolean callerIsRoot, String callerInstitutionId) {
        Users target = findManageableUser(uuid, callerIsRoot, callerInstitutionId);
        guardSelf(target.getUuid());
        userRepository.delete(target);
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    private Users findManageableUser(String uuid, boolean callerIsRoot, String callerInstitutionId) {
        Users target = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User", uuid));

        if (target.isRoot()) {
            throw new IllegalArgumentException("No se puede gestionar un usuario ROOT.");
        }
        if (!callerIsRoot && !target.getInstitutionId().equals(callerInstitutionId)) {
            throw new IllegalArgumentException("El usuario no pertenece a tu institución.");
        }
        return target;
    }

    private void guardSelf(String targetUuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String callerEmail = auth.getName();
        userRepository.findByEmail(callerEmail).ifPresent(caller -> {
            if (caller.getUuid().equals(targetUuid)) {
                throw new IllegalArgumentException("No puedes realizar esta operación sobre tu propio usuario.");
            }
        });
    }
}
