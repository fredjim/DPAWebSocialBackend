package com.infsis.socialpagebackend.social_networks.services;

import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.security.AesTokenEncryptor;
import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigRequestDTO;
import com.infsis.socialpagebackend.social_networks.dtos.FacebookConfigResponseDTO;
import com.infsis.socialpagebackend.social_networks.mappers.InstitutionFacebookConfigMapper;
import com.infsis.socialpagebackend.social_networks.models.InstitutionFacebookConfig;
import com.infsis.socialpagebackend.social_networks.repositories.InstitutionFacebookConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstitutionFacebookConfigService {

    @Autowired
    private InstitutionFacebookConfigRepository configRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionFacebookConfigMapper mapper;

    @Autowired
    private AesTokenEncryptor encryptor;

    public FacebookConfigResponseDTO saveConfig(String institutionUuid,
                                                FacebookConfigRequestDTO requestDTO) {
        Institution institution = institutionRepository.findOneByUuid(institutionUuid);
        if (institution == null) {
            throw new NotFoundException("Institution", institutionUuid);
        }

        String encryptedToken = encryptor.encrypt(requestDTO.getAccess_token());

        InstitutionFacebookConfig config =
                configRepository.findByInstitutionUuid(institutionUuid)
                        .orElse(mapper.toEntity(requestDTO, institution, encryptedToken));

        config.setPageId(requestDTO.getPage_id());
        config.setAccessToken(encryptedToken);
        config.setEnabled(requestDTO.isEnabled());
        config.setInstitution(institution);

        configRepository.save(config);

        return mapper.toResponseDTO(config, requestDTO.getAccess_token());
    }

    public FacebookConfigResponseDTO getConfig(String institutionUuid) {
        Optional<InstitutionFacebookConfig> configOpt =
                configRepository.findByInstitutionUuid(institutionUuid);

        if (configOpt.isEmpty()) {
            return mapper.toNotConfiguredDTO(institutionUuid);
        }

        InstitutionFacebookConfig config = configOpt.get();
        String rawToken = encryptor.decrypt(config.getAccessToken());
        return mapper.toResponseDTO(config, rawToken);
    }

    public FacebookConfigResponseDTO disableConfig(String institutionUuid) {
        InstitutionFacebookConfig config =
                configRepository.findByInstitutionUuid(institutionUuid)
                        .orElseThrow(() -> new NotFoundException("FacebookConfig", institutionUuid));

        config.setEnabled(false);
        configRepository.save(config);

        String rawToken = encryptor.decrypt(config.getAccessToken());
        return mapper.toResponseDTO(config, rawToken);
    }

    public void deleteConfig(String institutionUuid) {
        InstitutionFacebookConfig config =
                configRepository.findByInstitutionUuid(institutionUuid)
                        .orElseThrow(() -> new NotFoundException("FacebookConfig", institutionUuid));
        configRepository.delete(config);
    }

    public Optional<InstitutionFacebookConfig> getActiveConfig(String institutionUuid) {
        return configRepository.findByInstitutionUuid(institutionUuid)
                .filter(InstitutionFacebookConfig::isEnabled);
    }

    public String decryptToken(InstitutionFacebookConfig config) {
        return encryptor.decrypt(config.getAccessToken());
    }
}
