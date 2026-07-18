package com.infsis.socialpagebackend.reactions.mappers;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.reactions.dtos.ReplyReactionDTO;
import com.infsis.socialpagebackend.reactions.models.EmojiType;
import com.infsis.socialpagebackend.reactions.models.ReplyReaction;
import com.infsis.socialpagebackend.replies.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplyReactionMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public ReplyReactionDTO toDTO(ReplyReaction replyReaction) {
        ReplyReactionDTO dto = new ReplyReactionDTO();
        dto.setUuid(replyReaction.getUuid());
        dto.setUser_id(replyReaction.getUser().getUuid());
        dto.setReply_id(replyReaction.getReply().getUuid());
        dto.setReaction_date(replyReaction.getReactionDate());
        dto.setEmoji_type_id(replyReaction.getEmojiType().getUuid());

        // Campos nuevos
        dto.setUserName(replyReaction.getUser().getName() + " " + replyReaction.getUser().getLastName());
        dto.setUserPhoto(buildUrlIfPresent(replyReaction.getUser().getPhoto_profile_path()));

        return dto;
    }

    private String buildUrlIfPresent(String path) {
        if (path == null || path.isBlank()) {
            return path;
        }
        return appUrlProperties.buildResourceUrl(path);
    }

    public ReplyReaction getReaction(ReplyReactionDTO dto, Users user, Reply reply, EmojiType emojiType) {
        ReplyReaction replyReaction = new ReplyReaction();
        replyReaction.setUser(user);
        replyReaction.setReply(reply);
        replyReaction.setEmojiType(emojiType);
        replyReaction.setReactionDate(dto.getReaction_date());
        return replyReaction;
    }
}