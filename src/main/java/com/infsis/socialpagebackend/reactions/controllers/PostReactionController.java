package com.infsis.socialpagebackend.reactions.controllers;

import com.infsis.socialpagebackend.reactions.dtos.PostReactionDTO;
import com.infsis.socialpagebackend.reactions.services.PostReactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Validated
public class PostReactionController {

    @Autowired
    private PostReactionService postReactionService;

    @GetMapping("/posts/{postUuid}/reactions/{reactionUuid}")
    public PostReactionDTO get(@PathVariable String postUuid, @PathVariable String reactionUuid) {
        return postReactionService.getPostReaction(postUuid, reactionUuid);
    }

    @GetMapping("/posts/{postUuid}/reactions")
    public List<PostReactionDTO> getAll(@PathVariable String postUuid) {
        return postReactionService.getAllPostReaction(postUuid);
    }

    @PreAuthorize("hasAuthority('CREATE_POST_REACTION')")
    @PostMapping("/posts/{postUuid}/reactions")
    @ResponseStatus(HttpStatus.CREATED)
    public PostReactionDTO create(@PathVariable String postUuid, @Valid @RequestBody PostReactionDTO postReactionDTO) {
        return postReactionService.saveReaction(postUuid, postReactionDTO);
    }

    @PreAuthorize("hasAuthority('DELETE_POST_REACTION')")
    @DeleteMapping("/posts/{postUuid}/reactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String postUuid) {
        postReactionService.deleteReaction(postUuid);
    }

}