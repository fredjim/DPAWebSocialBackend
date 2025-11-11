package com.infsis.socialpagebackend.navigation.mappers;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.navigation.dtos.NavItemDTO;
import com.infsis.socialpagebackend.navigation.models.NavItem;
import org.springframework.stereotype.Component;

@Component
public class NavItemMapper {

    public NavItemDTO toDTO(NavItem navItem) {
        if (navItem == null) return null;
        NavItemDTO dto = new NavItemDTO();
        dto.setUuid(navItem.getUuid());
        dto.setUser_id(navItem.getUsers() != null ? navItem.getUsers().getUuid() : null);
        dto.setInstitution_id(navItem.getInstitution() != null ? navItem.getInstitution().getUuid() : null);
        dto.setLabel(navItem.getLabel());
        dto.setUrl(navItem.getUrl());
        dto.setVisible(navItem.isVisible());
        dto.setOrderIndex(navItem.getOrderIndex());
        dto.setCreatedDate(navItem.getCreatedDate());
        dto.setLastModifiedDate(navItem.getLastModifiedDate());
        return dto;
    }

    public NavItem getNavItem(NavItemDTO dto, Institution institution, Users user) {
        NavItem navItem = new NavItem();
        navItem.setLabel(dto.getLabel());
        navItem.setUrl(dto.getUrl());
        navItem.setVisible(dto.getVisible() != null ? dto.getVisible() : true);
        navItem.setOrderIndex(dto.getOrderIndex() != null ? dto.getOrderIndex() : 0);
        navItem.setInstitution(institution);
        navItem.setUsers(user);
        return navItem;
    }
}

