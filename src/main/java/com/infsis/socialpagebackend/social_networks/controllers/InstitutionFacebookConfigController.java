package com.infsis.socialpagebackend.social_networks.controllers;

import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigRequestDTO;
import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigResponseDTO;
import com.infsis.socialpagebackend.social_networks.services.InstitutionFacebookConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/institutions/{institutionUuid}/facebook-config")
public class InstitutionFacebookConfigController {

    @Autowired
    private InstitutionFacebookConfigService service;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_FACEBOOK_CONFIG')")
    public ResponseEntity<FacebookConfigResponseDTO> getConfig(
            @PathVariable String institutionUuid) {
        return ResponseEntity.ok(service.getConfig(institutionUuid));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_FACEBOOK_CONFIG')")
    public ResponseEntity<FacebookConfigResponseDTO> saveConfig(
            @PathVariable String institutionUuid,
            @Valid @RequestBody FacebookConfigRequestDTO requestDTO) {
        return ResponseEntity.ok(service.saveConfig(institutionUuid, requestDTO));
    }

    @PatchMapping("/disable")
    @PreAuthorize("hasAuthority('MANAGE_FACEBOOK_CONFIG')")
    public ResponseEntity<FacebookConfigResponseDTO> disableConfig(
            @PathVariable String institutionUuid) {
        return ResponseEntity.ok(service.disableConfig(institutionUuid));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('MANAGE_FACEBOOK_CONFIG')")
    public ResponseEntity<Void> deleteConfig(
            @PathVariable String institutionUuid) {
        service.deleteConfig(institutionUuid);
        return ResponseEntity.noContent().build();
    }
}
