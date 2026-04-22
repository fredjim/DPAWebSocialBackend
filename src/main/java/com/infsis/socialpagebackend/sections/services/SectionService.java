package com.infsis.socialpagebackend.sections.services;

import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.multitenant.TenantResolver;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.sections.dtos.SectionDTO;
import com.infsis.socialpagebackend.sections.mappers.SectionMapper;
import com.infsis.socialpagebackend.sections.models.Section;
import com.infsis.socialpagebackend.sections.repositories.SectionRepository;
import com.infsis.socialpagebackend.navigation.models.NavItem;
import com.infsis.socialpagebackend.navigation.repositories.NavItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NavItemRepository navItemRepository;

    @Autowired
    private TenantResolver tenantResolver;



    public SectionDTO getSection(String sectionUuid) {
        Section section = sectionRepository.findOneByUuid(sectionUuid);

        if(section == null || section.isDeleted()) {
            throw new NotFoundException("Section", sectionUuid);
        }
        //List<Article> articles = getPostReactionCounterDTO(article);

        return sectionMapper.toDTO(section);
    }

    public List<SectionDTO> getAllSections(String tenantId) {
        return sectionRepository
                .findByInstitutionUuid(tenantId)
                .stream()
                .map(section -> sectionMapper.toDTO(section))
                .collect(Collectors.toList());
    }

    public SectionDTO getSectionByPath(String path, String tenantSlug) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            tenantId = tenantResolver.resolveOrThrow(tenantSlug);
        }

        return sectionRepository
                .findByInstitutionUuidAndPath(tenantId, path)
                .map(sectionMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Section not found with path: ", path));
    }

    public SectionDTO saveSection(SectionDTO sectionDTO) {

        String tenantId = TenantContext.getCurrentTenant();
        Institution institution = institutionRepository.findOneByUuid(tenantId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        if (sectionRepository.existsByInstitutionUuidAndPath(tenantId, sectionDTO.getPath())) {
            throw new DuplicateKeyException("Already exists a section with the path '" + sectionDTO.getPath() + "' in this institution");
        }

        NavItem navItem = null;
        if (sectionDTO.getNav_item_id() != null) {
            navItem = navItemRepository.findOneByUuid(sectionDTO.getNav_item_id());
        }

        Section section = sectionMapper.getSection(sectionDTO, institution, user, navItem);
        sectionRepository.save(section);

        return sectionMapper.toDTO(section);
    }

    public SectionDTO deleteSection(String sectionUuid) {
        Section section = sectionRepository.findOneByUuid(sectionUuid);
        section.setDeleted(true);
        sectionRepository.save(section);
        return sectionMapper.toDTO(section);
    }

    public SectionDTO updateSection(String sectionUuid, SectionDTO sectionDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        Section foundSection = sectionRepository.findOneByUuid(sectionUuid);
        if (foundSection == null || foundSection.isDeleted()) {
            throw new NotFoundException("Section", sectionUuid);
        }

        if (sectionDTO.getPath() != null && !sectionDTO.getPath().equals(foundSection.getPath())) {
            if (sectionRepository.existsByInstitutionUuidAndPathAndUuidNot(foundSection.getInstitution().getUuid(), sectionDTO.getPath(), sectionUuid)) {
                throw new DuplicateKeyException("Already exists a section with the path '" + sectionDTO.getPath() + "' in this institution");
            }
            foundSection.setPath(sectionDTO.getPath());
        }

        if (sectionDTO.getNav_item_id() != null) {
            NavItem navItem = navItemRepository.findOneByUuid(sectionDTO.getNav_item_id());
            foundSection.setNavItem(navItem);
        }

        foundSection.setDate(sectionDTO.getDate());
        foundSection.setName(sectionDTO.getName());

        Section updatedSection = sectionRepository.save(foundSection);

        return sectionMapper.toDTO(updatedSection);
    }

    public List<SectionDTO> getSectionsByNavItem(String navItemUuid) {
        return sectionRepository.findByNavItemUuid(navItemUuid)
                .stream()
                .map(section -> sectionMapper.toDTO(section))
                .collect(Collectors.toList());
    }

}
