package com.infsis.socialpagebackend.authentication.controllers;

import com.infsis.socialpagebackend.authentication.dtos.CreateUserDTO;
import com.infsis.socialpagebackend.authentication.dtos.PermissionDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.services.RoleService;
import com.infsis.socialpagebackend.authentication.services.UserService;
import com.infsis.socialpagebackend.security.AuthContext;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthContext authContext;

    public UserController(UserService userService, RoleService roleService, AuthContext authContext) {
        this.userService = userService;
        this.roleService = roleService;
        this.authContext = authContext;
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailDTO createUser(@Valid @RequestBody CreateUserDTO dto) {
        return userService.createUser(dto, authContext.isRoot(), authContext.getInstitutionId());
    }

    @PreAuthorize("hasAuthority('CREATE_USER_ADMIN')")
    @GetMapping("/permissions")
    public List<PermissionDTO> getAvailablePermissions() {
        return roleService.getAvailablePermissions(authContext.isRoot());
    }

    // ── Gestión genérica de usuarios ─────────────────────────────────────────

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public Page<UserDetailDTO> listUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return userService.listUsers(role, enabled, search,
                authContext.isRoot(), authContext.getInstitutionId(), pageable);
    }

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{uuid}")
    public UserDetailDTO getUser(@PathVariable String uuid) {
        return userService.getUser(uuid, authContext.isRoot(), authContext.getInstitutionId());
    }

    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PutMapping("/{uuid}")
    public UserDetailDTO updateUser(@PathVariable String uuid,
                                    @Valid @RequestBody UserDetailDTO dto) {
        return userService.updateUser(uuid, dto, authContext.isRoot(), authContext.getInstitutionId());
    }

    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PatchMapping("/{uuid}/disable")
    public UserDetailDTO toggleDisable(@PathVariable String uuid) {
        return userService.toggleDisable(uuid, authContext.isRoot(), authContext.getInstitutionId());
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{uuid}")
    public Map<String, Object> deleteUser(@PathVariable String uuid) {
        userService.deleteUser(uuid, authContext.isRoot(), authContext.getInstitutionId());
        return Map.of("message", "User deleted successfully.");
    }
}
