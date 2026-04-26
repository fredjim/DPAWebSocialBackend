package com.infsis.socialpagebackend.social_networks.mappers;

import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigRequestDTO;
import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigResponseDTO;
import com.infsis.socialpagebackend.social_networks.models.InstitutionFacebookConfig;
import org.springframework.stereotype.Component;

@Component
public class InstitutionFacebookConfigMapper {

    public FacebookConfigResponseDTO toResponseDTO(InstitutionFacebookConfig config, String rawToken) {
        FacebookConfigResponseDTO dto = new FacebookConfigResponseDTO();
        dto.setUuid(config.getUuid());
        dto.setInstitution_id(config.getInstitution().getUuid());
        dto.setPage_id(config.getPageId());
        dto.setConfigured(true);
        dto.setEnabled(config.isEnabled());
        dto.setToken_hint(rawToken != null && rawToken.length() > 6
                ? "..." + rawToken.substring(rawToken.length() - 6)
                : "******");
        return dto;
    }

    public FacebookConfigResponseDTO toNotConfiguredDTO(String institutionId) {
        FacebookConfigResponseDTO dto = new FacebookConfigResponseDTO();
        dto.setInstitution_id(institutionId);
        dto.setConfigured(false);
        dto.setEnabled(false);
        return dto;
    }

    public InstitutionFacebookConfig toEntity(FacebookConfigRequestDTO requestDTO,
                                               Institution institution,
                                               String encryptedToken) {
        InstitutionFacebookConfig config = new InstitutionFacebookConfig();
        config.setInstitution(institution);
        config.setPageId(requestDTO.getPage_id());
        config.setAccessToken(encryptedToken);
        config.setEnabled(requestDTO.isEnabled());
        return config;
    }
}
