package com.infsis.socialpagebackend.replies.dto;

import com.infsis.socialpagebackend.posts.dtos.ReactionCounterDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReplyDTO {
    private String uuid;
    private LocalDateTime createdDate;
    private String content;
    private String name;
    private String lastName;
    private String user_photo;
    private String parentReplyUuid;  // ✅ Referencia a la respuesta padre
    private ReactionCounterDTO reactions;
private List<ReplyDTO> replies = new ArrayList<>();

}
