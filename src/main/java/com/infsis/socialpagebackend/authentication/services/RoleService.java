package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.dtos.PermissionDTO;
import com.infsis.socialpagebackend.authentication.dtos.RoleDTO;
import com.infsis.socialpagebackend.authentication.models.Permissions;
import com.infsis.socialpagebackend.authentication.models.Role;
import com.infsis.socialpagebackend.authentication.repositories.PermissionRepository;
import com.infsis.socialpagebackend.authentication.repositories.RoleRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public RoleDTO createRole(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + dto.getName());
        }

        Set<Permissions> permisos = resolveAndValidatePermissions(dto.getPermissionIds());

        Role role = new Role();
        role.setName(dto.getName());
        role.setSystemRole(false);
        role.setPermisos(permisos);

        return toDTO(roleRepository.save(role));
    }

    public RoleDTO updateRole(Long id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role", String.valueOf(id)));

        if (role.isSystemRole()) {
            throw new IllegalArgumentException("Los roles del sistema no se pueden modificar.");
        }

        Set<Permissions> permisos = resolveAndValidatePermissions(dto.getPermissionIds());
        role.setPermisos(permisos);

        return toDTO(roleRepository.save(role));
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role", String.valueOf(id)));

        if (role.isSystemRole()) {
            throw new IllegalArgumentException("Los roles del sistema no se pueden eliminar.");
        }

        roleRepository.delete(role);
    }

    public List<PermissionDTO> getAvailablePermissions(boolean callerIsRoot) {
        List<Permissions> perms = callerIsRoot
                ? permissionRepository.findAll()
                : permissionRepository.findByIsSystemOnlyFalse();

        return perms.stream()
                .map(p -> {
                    PermissionDTO dto = new PermissionDTO();
                    dto.setId(p.getIdPermission());
                    dto.setName(p.getNamePermission());
                    dto.setSystemOnly(p.isSystemOnly());
                    return dto;
                })
                .toList();
    }

    private Set<Permissions> resolveAndValidatePermissions(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();

        List<Permissions> found = permissionRepository.findAllByIdPermissionIn(ids);

        if (found.size() != ids.size()) {
            throw new IllegalArgumentException("Uno o más IDs de permiso no existen.");
        }

        boolean hasSystemOnly = found.stream().anyMatch(Permissions::isSystemOnly);
        if (hasSystemOnly) {
            throw new IllegalArgumentException("No se pueden asignar permisos exclusivos del sistema a roles custom.");
        }

        return new HashSet<>(found);
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getIdRole());
        dto.setName(role.getName());
        dto.setSystemRole(role.isSystemRole());
        dto.setPermissionIds(role.getPermisos().stream()
                .map(Permissions::getIdPermission)
                .collect(Collectors.toSet()));
        return dto;
    }
}
