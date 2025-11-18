package com.infsis.socialpagebackend.sections.mappers;

import com.infsis.socialpagebackend.sections.dtos.LinkDTO;
import com.infsis.socialpagebackend.sections.models.Link;
import com.infsis.socialpagebackend.enums.OwnerType;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {

    public LinkDTO toDTO(Link link) {
        LinkDTO dto = new LinkDTO();
        dto.setUuid(link.getUuid());
        dto.setName(link.getName());
        dto.setUrl(link.getUrl());
        return dto;
    }

    public Link getLink(LinkDTO dto, OwnerType ownerType, String ownerUuid) {
        Link link = new Link();
        link.setOwnerType(ownerType);
        link.setOwnerUuid(ownerUuid);
        link.setName(dto.getName());
        link.setUrl(dto.getUrl());
        return link;
    }
}
