package com.infsis.socialpagebackend.institutions.controllers;

import com.infsis.socialpagebackend.posts.services.PostService;
import com.infsis.socialpagebackend.posts.dtos.MediaItemDTO;
import com.infsis.socialpagebackend.institutions.dtos.InstitutionDTO;
import com.infsis.socialpagebackend.institutions.services.InstitutionService;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.security.AuthContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institutions")
@Validated
public class InstitutionController {
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String DOCUMENT   = "document";
    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthContext authContext;

    @GetMapping("/current")
    public InstitutionDTO getCurrent() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new NotFoundException("Institution", "no tenant in context");
        }
        return institutionService.getInstitution(tenantId);
    }

    @GetMapping("/{institutionUuid}")
    public InstitutionDTO get(@PathVariable String institutionUuid) {
        return institutionService.getInstitution(institutionUuid);
    }

    @PreAuthorize("hasAuthority('VIEW_ALL_INSTITUTIONS')")
    @GetMapping
    public List<InstitutionDTO> getAll() {
        return institutionService.getAllInstitutions();
    }

    @PreAuthorize("hasAuthority('CREATE_INSTITUTION')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionDTO create(@Valid @RequestBody InstitutionDTO institutionDTO) {
        return institutionService.saveInstitution(institutionDTO);
    }

    @PreAuthorize("hasAuthority('UPDATE_INSTITUTION')")
    @PutMapping()
    public InstitutionDTO update(@Valid @RequestBody InstitutionDTO institutionDTO) {
        return institutionService.updateInstitution(institutionDTO, authContext.isRoot(), authContext.getInstitutionId());
    }

    @PreAuthorize("hasAuthority('DELETE_INSTITUTION')")
    @DeleteMapping("/{institutionUuid}")
    public InstitutionDTO delete(@PathVariable String institutionUuid) {
        return institutionService.deleteInstitution(institutionUuid);
    }

    @GetMapping("/{institutionUuid}/photos")
    public List<MediaItemDTO> getPhotosByInstitution(@PathVariable String institutionUuid) {
        return postService.getMediasInstitution(institutionUuid, IMAGE);
    }

    @GetMapping("/{institutionUuid}/videos")
    public List<MediaItemDTO> getVideosByInstitution(@PathVariable String institutionUuid) {
        return postService.getMediasInstitution(institutionUuid, VIDEO);
    }

    @GetMapping("/{institutionUuid}/documents")
    public List<MediaItemDTO> getDocsByInstitution(@PathVariable String institutionUuid) {
        return postService.getMediasInstitution(institutionUuid, DOCUMENT);
    }
}