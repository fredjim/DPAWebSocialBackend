package com.infsis.socialpagebackend.institutions.services;

import com.infsis.socialpagebackend.institutions.dtos.InstitutionDTO;
import com.infsis.socialpagebackend.institutions.mappers.InstitutionMapper;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import com.infsis.socialpagebackend.medias.repositories.UploadedFileRepository;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public InstitutionDTO getInstitution(String institutionUuid) {
        Institution institution = institutionRepository.findOneByUuid(institutionUuid);
        if(institution == null) {
            throw new NotFoundException("Institution", institutionUuid);
        }
        return institutionMapper.toDTO(institution);
    }

    public List<InstitutionDTO> getAllInstitutions() {
        return institutionRepository
                .findAll()
                .stream()
                .map(institution -> institutionMapper.toDTO(institution))
                .collect(Collectors.toList());
    }

    public InstitutionDTO saveInstitution(InstitutionDTO institutionDTO) {
        if (institutionRepository.findBySlug(institutionDTO.getSlug()).isPresent()) {
            throw new IllegalArgumentException("El slug '" + institutionDTO.getSlug() + "' ya está en uso por otra institución.");
        }

        Institution institution = institutionMapper.getInstitution(institutionDTO);
        institution.setLogoFile(resolveFileFromUuid(institutionDTO.getLogoFileUuid()));
        institution.setBackgroundFile(resolveFileFromUuid(institutionDTO.getBackgroundFileUuid()));
        institutionRepository.save(institution);

        log.info("INSTITUCION_CREADA institucionUuid={} nombre={} slug={}",
                institution.getUuid(), institution.getName(), institution.getSlug());
        return institutionMapper.toDTO(institution);
    }

    public InstitutionDTO updateInstitution(InstitutionDTO institutionDTO, boolean callerIsRoot, String callerInstitutionId) {
        String targetUuid = callerIsRoot ? institutionDTO.getUuid() : callerInstitutionId;
        if (targetUuid == null || targetUuid.isBlank()) {
            throw new IllegalArgumentException("Institution UUID is required.");
        }
        Optional<Institution> optionalInstitution = findInstitution(targetUuid);

        Institution institution = optionalInstitution.get();

        if (institutionDTO.getSlug() != null) {
            institutionRepository.findBySlug(institutionDTO.getSlug())
                    .filter(existing -> !existing.getUuid().equals(institution.getUuid()))
                    .ifPresent(existing -> {
                        throw new IllegalArgumentException("El slug '" + institutionDTO.getSlug() + "' ya está en uso por otra institución.");
                    });
            institution.setSlug(institutionDTO.getSlug());
        }

        if (institutionDTO.getName() != null) institution.setName(institutionDTO.getName());
        if (institutionDTO.getDescription() != null) institution.setDescription(institutionDTO.getDescription());
        if (institutionDTO.getLocation() != null) institution.setLocation(institutionDTO.getLocation());
        if (institutionDTO.getCategory() != null) institution.setCategory(institutionDTO.getCategory());
        if (institutionDTO.getEmail() != null) institution.setEmail(institutionDTO.getEmail());
        if (institutionDTO.getPhone() != null) institution.setPhone(institutionDTO.getPhone());
        if (institutionDTO.getUrl() != null) institution.setUrl(institutionDTO.getUrl());

        UploadedFile oldLogoFile = null;
        UploadedFile oldBackgroundFile = null;

        if (institutionDTO.getLogoFileUuid() != null && !institutionDTO.getLogoFileUuid().isBlank()) {
            UploadedFile current = institution.getLogoFile();
            if (current == null || !current.getUuid().equals(institutionDTO.getLogoFileUuid())) {
                UploadedFile newFile = uploadedFileRepository.findByUuid(institutionDTO.getLogoFileUuid())
                        .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado con UUID: " + institutionDTO.getLogoFileUuid()));
                oldLogoFile = current;
                institution.setLogoFile(newFile);
                institution.setLogo_url(newFile.getUrlResource());
            }
        }

        if (institutionDTO.getBackgroundFileUuid() != null && !institutionDTO.getBackgroundFileUuid().isBlank()) {
            UploadedFile current = institution.getBackgroundFile();
            if (current == null || !current.getUuid().equals(institutionDTO.getBackgroundFileUuid())) {
                UploadedFile newFile = uploadedFileRepository.findByUuid(institutionDTO.getBackgroundFileUuid())
                        .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado con UUID: " + institutionDTO.getBackgroundFileUuid()));
                oldBackgroundFile = current;
                institution.setBackgroundFile(newFile);
                institution.setBackground_url(newFile.getUrlResource());
            }
        }

        institutionRepository.save(institution);

        if (oldLogoFile != null) fileStorageService.deleteFileOnlyByUuid(oldLogoFile.getUuid());
        if (oldBackgroundFile != null) fileStorageService.deleteFileOnlyByUuid(oldBackgroundFile.getUuid());

        log.info("INSTITUCION_MODIFICADA institucionUuid={} nombre={}", institution.getUuid(), institution.getName());
        return institutionMapper.toDTO(institution);
    }

    public InstitutionDTO deleteInstitution(String institutionUuid) {
        Optional<Institution> optionalInstitution = findInstitution(institutionUuid);

        Institution institution = optionalInstitution.get();
        institutionRepository.delete(institution);
        log.warn("INSTITUCION_ELIMINADA institucionUuid={} nombre={} slug={}",
                institution.getUuid(), institution.getName(), institution.getSlug());
        return institutionMapper.toDTO(institution);
    }

    private UploadedFile resolveFileFromUuid(String uuid) {
        if (uuid == null || uuid.isBlank()) return null;
        return uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado con UUID: " + uuid));
    }

    private Optional<Institution> findInstitution(String institutionUuid) {
        Institution example1 = new Institution(institutionUuid);
        Optional<Institution> optionalInstitution = institutionRepository.findOne(Example.of(example1));

        if (optionalInstitution.isEmpty()) {
            throw  new NotFoundException("Institution", institutionUuid);
        }

        return optionalInstitution;
    }
}
