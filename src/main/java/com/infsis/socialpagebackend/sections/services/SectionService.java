package com.infsis.socialpagebackend.sections.services;

import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.multitenant.TenantResolver;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.enums.OwnerType;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.sections.models.Article;
import com.infsis.socialpagebackend.sections.repositories.LinkRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SectionService {

    private static final String VIDEOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/videos/";
    private static final String DOCUMENTS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/documents/";
    private static final String PHOTOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/photos/";

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private FileStorageService fileStorageService;

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

    @Transactional
    public SectionDTO deleteSection(String sectionUuid) {
        Section section = sectionRepository.findOneByUuid(sectionUuid);
        if (section == null) throw new NotFoundException("Section", sectionUuid);

        SectionDTO dto = sectionMapper.toDTO(section);

        List<String> articleUuids = new ArrayList<>();

        List<Article> articles = section.getArticles();
        if (articles != null) {
            articles.forEach(article -> {
                if (article.getUuid() != null) {
                    articleUuids.add(article.getUuid());
                }
                if (article.getArticle_medias() != null) {
                    article.getArticle_medias().forEach(media -> {
                        if (media.getUploadedFile() != null) {
                            try {
                                fileStorageService.deleteFileOnly(
                                    media.getUploadedFile().getUuid(),
                                    resolveDirectory(media.getUploadedFile().getCategory())
                                );
                            } catch (Exception e) {
                                log.warn("Could not delete file uuid={}: {}", media.getUploadedFile().getUuid(), e.getMessage());
                            }
                        }
                    });
                }
            });
        }

        if (!articleUuids.isEmpty()) {
            linkRepository.hardDeleteByOwnerTypeAndOwnerUuids(OwnerType.ARTICLE, articleUuids);
        }

        sectionRepository.hardDeleteByUuid(sectionUuid);
        return dto;
    }

    private String resolveDirectory(FileCategory category) {
        if (category == null) return PHOTOS_DIRECTORY;
        return switch (category) {
            case VIDEO    -> VIDEOS_DIRECTORY;
            case DOCUMENT -> DOCUMENTS_DIRECTORY;
            default       -> PHOTOS_DIRECTORY;
        };
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
