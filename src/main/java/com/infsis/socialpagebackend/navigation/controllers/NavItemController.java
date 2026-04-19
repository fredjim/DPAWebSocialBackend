package com.infsis.socialpagebackend.navigation.controllers;

import com.infsis.socialpagebackend.multitenant.TenantResolver;
import com.infsis.socialpagebackend.navigation.dtos.NavItemDTO;
import com.infsis.socialpagebackend.navigation.services.NavItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/navitems")
@Validated
public class NavItemController {

    @Autowired
    private NavItemService navItemService;

    @Autowired
    private TenantResolver tenantResolver;

    @GetMapping("/{uuid}")
    public NavItemDTO get(@PathVariable String uuid) {
        return navItemService.getNavItem(uuid);
    }

    @GetMapping
    public List<NavItemDTO> getAllByInstitution(
            @RequestHeader(value = "X-Tenant-Slug", required = false) String tenantSlug) {
        String tenantId = tenantResolver.resolveOrThrow(tenantSlug);
        return navItemService.getAllByInstitution(tenantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NavItemDTO create(@Valid @RequestBody NavItemDTO dto) {
        return navItemService.createNavItem(dto);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public NavItemDTO update(@PathVariable String uuid, @Valid @RequestBody NavItemDTO dto) {
        return navItemService.updateNavItem(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public NavItemDTO delete(@PathVariable String uuid) {
        return navItemService.deleteNavItem(uuid);
    }
}

