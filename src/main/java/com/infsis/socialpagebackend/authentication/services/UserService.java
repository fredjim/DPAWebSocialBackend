package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.dtos.CreateUserDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.mappers.UserMapper;
import com.infsis.socialpagebackend.authentication.models.Permissions;
import com.infsis.socialpagebackend.authentication.models.Role;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.RoleRepository;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
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
}
