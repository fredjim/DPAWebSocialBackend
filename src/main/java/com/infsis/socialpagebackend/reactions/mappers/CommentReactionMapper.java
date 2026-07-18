package com.infsis.socialpagebackend.reactions.mappers;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.comments.models.Comment;
import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.reactions.dtos.CommentReactionDTO;
import com.infsis.socialpagebackend.reactions.models.CommentReaction;
import com.infsis.socialpagebackend.reactions.models.EmojiType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentReactionMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public CommentReactionDTO toDTO(CommentReaction commentReaction) {
        CommentReactionDTO commentReactionDTO = new CommentReactionDTO();
        commentReactionDTO.setUuid(commentReaction.getUuid());
        commentReactionDTO.setUserId(commentReaction.getUsers().getUuid());
        commentReactionDTO.setCommentId(commentReaction.getComment().getUuid());
        commentReactionDTO.setReactionDate(commentReaction.getReactionDate());
        commentReactionDTO.setEmojiTypeId(commentReaction.getEmojiType().getUuid());

        // Nuevos campos
        commentReactionDTO.setUserName(commentReaction.getUsers().getName() + " " + commentReaction.getUsers().getLastName());
        commentReactionDTO.setUserPhoto(buildUrlIfPresent(commentReaction.getUsers().getPhoto_profile_path()));

        return commentReactionDTO;
    }

    private String buildUrlIfPresent(String path) {
        if (path == null || path.isBlank()) {
            return path;
        }
        return appUrlProperties.buildResourceUrl(path);
    }
    

    public CommentReaction getReaction(CommentReactionDTO commentReactionDTO, Users users, Comment comment, EmojiType emojiType) {
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setUsers(users);
        commentReaction.setComment(comment);
        commentReaction.setEmojiType(emojiType);
        commentReaction.setReactionDate(commentReactionDTO.getReactionDate());

        return commentReaction;
    }
}

