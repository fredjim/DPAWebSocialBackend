package com.infsis.socialpagebackend.sections.mappers;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.navigation.models.NavItem;
import com.infsis.socialpagebackend.sections.dtos.SectionDTO;
import com.infsis.socialpagebackend.sections.models.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {

    public SectionDTO toDTO(Section section) {

        SectionDTO sectionDTO = new SectionDTO();

        sectionDTO.setUuid(section.getUuid());
        sectionDTO.setName(section.getName());
        sectionDTO.setPath(section.getPath());
        sectionDTO.setDate(section.getDate());
        sectionDTO.setUser_id(section.getUsers().getUuid());
        sectionDTO.setInstitution_id(section.getInstitution().getUuid());
        sectionDTO.setNav_item_id(section.getNavItem() != null ? section.getNavItem().getUuid() : null);

        return sectionDTO;
    }

    public Section getSection(SectionDTO sectionDTO, Institution institution, Users user, NavItem navItem) {

        Section section = new Section();

        section.setInstitution(institution);
        section.setUsers(user);
        section.setNavItem(navItem);
        section.setName(sectionDTO.getName());
        section.setPath(sectionDTO.getPath());
        section.setDate(sectionDTO.getDate());

        return section;
    }
}
