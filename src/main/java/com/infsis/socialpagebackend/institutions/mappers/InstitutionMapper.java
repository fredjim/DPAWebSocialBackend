package com.infsis.socialpagebackend.institutions.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.institutions.dtos.InstitutionDTO;
import com.infsis.socialpagebackend.institutions.models.Institution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstitutionMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public InstitutionDTO toDTO(Institution institution) {
        InstitutionDTO institutionDTO = new InstitutionDTO();
        institutionDTO.setUuid(institution.getUuid());
        institutionDTO.setSlug(institution.getSlug());
        institutionDTO.setName(institution.getName());
        institutionDTO.setDescription(institution.getDescription());
        institutionDTO.setLocation(institution.getLocation());
        institutionDTO.setCategory(institution.getCategory());
        institutionDTO.setEmail(institution.getEmail());
        institutionDTO.setPhone(institution.getPhone());
        institutionDTO.setUrl(institution.getUrl());
        institutionDTO.setLogo_url(appUrlProperties.buildResourceUrl(institution.getLogo_url()));
        institutionDTO.setBackground_url(appUrlProperties.buildResourceUrl(institution.getBackground_url()));
        return institutionDTO;
    }

    public Institution getInstitution(InstitutionDTO institutionDTO) {
        Institution institution = new Institution();
        institution.setUuid(institutionDTO.getUuid());
        institution.setSlug(institutionDTO.getSlug());
        institution.setName(institutionDTO.getName());
        institution.setDescription(institutionDTO.getDescription());
        institution.setLocation(institutionDTO.getLocation());
        institution.setCategory(institutionDTO.getCategory());
        institution.setEmail(institutionDTO.getEmail());
        institution.setPhone(institutionDTO.getPhone());
        institution.setUrl(institutionDTO.getUrl());
        institution.setLogo_url(institutionDTO.getLogo_url());
        institution.setBackground_url(institutionDTO.getBackground_url());
        return institution;
    }

}
