package com.infsis.socialpagebackend.navigation.controllers;

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

    @GetMapping("/{uuid}")
    public NavItemDTO get(@PathVariable String uuid) {
        return navItemService.getNavItem(uuid);
    }

    @GetMapping
    public List<NavItemDTO> getAllByInstitution(@RequestParam(name = "institution_id", required = true) String institutionUuid) {
        return navItemService.getAllByInstitution(institutionUuid);
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

