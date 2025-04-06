package com.infsis.socialpagebackend.posts.controllers;

import com.infsis.socialpagebackend.posts.dtos.GroupDTO;
import com.infsis.socialpagebackend.posts.services.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasAuthority('VIEW_GROUP')")
    @GetMapping("/{groupUuid}")
    public GroupDTO get(@PathVariable String groupUuid) {
        return groupService.getGroup(groupUuid);
    }

    @PreAuthorize("hasAuthority('VIEW_ALL_GROUPS')")
    @GetMapping
    public List<GroupDTO> getAll() {
        return groupService.getAllGroups();
    }

    @PreAuthorize("hasAuthority('CREATE_GROUP')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDTO create(@Valid @RequestBody GroupDTO groupDTO) {
        return groupService.saveGroup(groupDTO);
    }
}