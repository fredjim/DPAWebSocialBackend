package com.infsis.socialpagebackend.authentication.controllers;

import com.infsis.socialpagebackend.authentication.dtos.RoleDTO;
import com.infsis.socialpagebackend.authentication.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@Validated
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @GetMapping
    public List<RoleDTO> getAll() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO create(@Valid @RequestBody RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @PutMapping("/{id}")
    public RoleDTO update(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(id, roleDTO);
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(Map.of("message", "Rol eliminado correctamente"));
    }
}
