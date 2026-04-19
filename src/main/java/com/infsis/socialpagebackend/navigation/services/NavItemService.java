package com.infsis.socialpagebackend.navigation.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NavItemService {

    @Autowired
    private NavItemRepository navItemRepository;

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

    public NavItemDTO deleteNavItem(String uuid) {
        NavItem navItem = navItemRepository.findOneByUuid(uuid);
        if (navItem == null) throw new NotFoundException("NavItem", uuid);
        navItem.setDeleted(true);
        navItemRepository.save(navItem);
        return navItemMapper.toDTO(navItem);
    }
}

