package com.infsis.socialpagebackend.authentication.controllers;

import com.infsis.socialpagebackend.authentication.dtos.CreateUserDTO;
import com.infsis.socialpagebackend.authentication.dtos.PermissionDTO;
import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.services.RoleService;
import com.infsis.socialpagebackend.authentication.services.UserService;
import com.infsis.socialpagebackend.security.AuthContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
