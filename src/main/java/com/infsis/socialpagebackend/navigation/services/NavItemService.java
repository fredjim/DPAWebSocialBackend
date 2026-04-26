package com.infsis.socialpagebackend.navigation.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.enums.OwnerType;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.sections.models.Article;
import com.infsis.socialpagebackend.sections.repositories.LinkRepository;
import com.infsis.socialpagebackend.sections.repositories.SectionRepository;
import org.springframework.dao.DuplicateKeyException;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.navigation.dtos.NavItemDTO;
import com.infsis.socialpagebackend.navigation.mappers.NavItemMapper;
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
public class NavItemService {

    private static final String VIDEOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/videos/";
    private static final String DOCUMENTS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/documents/";
    private static final String PHOTOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/photos/";

    @Autowired
    private NavItemRepository navItemRepository;

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
    private NavItemMapper navItemMapper;

    public NavItemDTO getNavItem(String uuid) {
        NavItem navItem = navItemRepository.findOneByUuid(uuid);
        if (navItem == null || navItem.isDeleted()) {
            throw new NotFoundException("NavItem", uuid);
        }
        return navItemMapper.toDTO(navItem);
    }

    public List<NavItemDTO> getAllByInstitution(String institutionUuid) {
        Institution institution = institutionRepository.findOneByUuid(institutionUuid);
        if (institution == null) throw new NotFoundException("Institution", institutionUuid);

        return navItemRepository.findByInstitutionUuidOrderByOrderIndexAsc(institutionUuid)
                .stream()
                .map(navItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NavItemDTO createNavItem(NavItemDTO dto) {
        String tenantId = TenantContext.getCurrentTenant();
        Institution institution = institutionRepository.findOneByUuid(tenantId);
        if (institution == null) throw new NotFoundException("Institution", tenantId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        if (navItemRepository.existsByInstitutionUuidAndPath(tenantId, dto.getPath())) {
            throw new DuplicateKeyException("Already exists a nav_item with the path '" + dto.getPath() + "' in this institution");
        }

        NavItem navItem = navItemMapper.getNavItem(dto, institution, user);
        navItemRepository.save(navItem);
        return navItemMapper.toDTO(navItem);
    }

    public NavItemDTO updateNavItem(String uuid, NavItemDTO dto) {
        NavItem existing = navItemRepository.findOneByUuid(uuid);
        if (existing == null || existing.isDeleted()) {
            throw new NotFoundException("NavItem", uuid);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        if (dto.getLabel() != null) existing.setLabel(dto.getLabel());
        if (dto.getPath() != null) {
            if (navItemRepository.existsByInstitutionUuidAndPathAndUuidNot(existing.getInstitution().getUuid(), dto.getPath(), uuid)) {
                throw new DuplicateKeyException("Already exists a nav_item with the path '" + dto.getPath() + "' in this institution");
            }
            existing.setPath(dto.getPath());
        }
        if (dto.getVisible() != null) existing.setVisible(dto.getVisible());
        if (dto.getOrderIndex() != null) existing.setOrderIndex(dto.getOrderIndex());

        // Always set the authenticated user as modifier
        existing.setUsers(user);

        navItemRepository.save(existing);
        return navItemMapper.toDTO(existing);
    }

    @Transactional
    public NavItemDTO deleteNavItem(String uuid) {
        NavItem navItem = navItemRepository.findOneByUuid(uuid);
        if (navItem == null) throw new NotFoundException("NavItem", uuid);

        NavItemDTO dto = navItemMapper.toDTO(navItem);

        List<String> articleUuids = new ArrayList<>();

        sectionRepository.findByNavItemUuid(uuid).forEach(section -> {
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
        });

        if (!articleUuids.isEmpty()) {
            linkRepository.hardDeleteByOwnerTypeAndOwnerUuids(OwnerType.ARTICLE, articleUuids);
        }

        navItemRepository.hardDeleteByUuid(uuid);
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
}

