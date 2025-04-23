package com.infsis.socialpagebackend.replies.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.comments.models.Comment;
import com.infsis.socialpagebackend.comments.repositories.CommentRepository;
import com.infsis.socialpagebackend.posts.dtos.ReactionCounterDTO;
import com.infsis.socialpagebackend.posts.dtos.ReactionItemDTO;
import com.infsis.socialpagebackend.reactions.models.EmojiType;
import com.infsis.socialpagebackend.replies.dto.ReplyDTO;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.reactions.models.ReplyReaction;
import com.infsis.socialpagebackend.reactions.repositories.EmojiTypeRepository;
import com.infsis.socialpagebackend.reactions.repositories.ReplyReactionRepository;
import com.infsis.socialpagebackend.replies.model.Reply;
import com.infsis.socialpagebackend.replies.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReplyService {

    private static final String ANONYMOUS_USER = "anonymousUser";


    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmojiTypeRepository emojiTypeRepository;

    @Autowired
    private ReplyReactionRepository replyReactionRepository;

    public List<ReplyDTO> getRepliesByCommentUuid(String commentUuid) {
        List<Reply> replies = replyRepository.findByCommentUuid(commentUuid);
        
        // 📌 Construimos la jerarquía de respuestas
        return buildReplyTree(replies, null);
    }
    
   
    private List<ReplyDTO> buildReplyTree(List<Reply> replies, String parentReplyUuid) {
        return replies.stream()
                .filter(reply -> {
                    if (parentReplyUuid == null) {
                        return reply.getParentReply() == null; // Respuestas de primer nivel
                    } else {
                        return reply.getParentReply() != null && reply.getParentReply().getUuid().equals(parentReplyUuid);
                    }
                })
                .map(reply -> {
                    ReplyDTO dto = convertToDTO(reply);
                    dto.setReactions(getReplyReactionCounterDTO(reply));
    
                    // 📌 Obtener respuestas hijas de forma recursiva
                    List<ReplyDTO> childReplies = buildReplyTree(replies, reply.getUuid());
                    dto.setReplies(childReplies != null ? childReplies : new ArrayList<>()); // ✅ Evita null
    
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
 
    
    

    public ReplyDTO saveReply(String comment_uuid, ReplyDTO replyRequest) {
        Comment comment = commentRepository.findByUuid(comment_uuid);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found");
        }
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: ", email));
    
        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setContent(replyRequest.getContent());
        reply.setUser(user);
    
     // Si hay `parentReplyUuid`, se asocia a una respuesta existente
if (replyRequest.getParentReplyUuid() != null) {
    Reply parentReply = replyRepository.findByUuid(replyRequest.getParentReplyUuid());
    if (parentReply == null) {
        throw new NotFoundException("Parent reply not found with uuid: " , replyRequest.getParentReplyUuid());
    }
    reply.setParentReply(parentReply);  // 🔥 Aquí asignamos correctamente el padre
}

        reply = replyRepository.save(reply);
        return convertToDTO(reply);
    }
    

    public void deleteReply(String uuid) {
        Reply reply = replyRepository.findByUuid(uuid);
        if (reply == null) {
            throw new NotFoundException("Reply not found with uuid: " ,uuid);
        }
        replyRepository.delete(reply);
    }

    private ReplyDTO convertToDTO(Reply reply) {
        ReplyDTO dto = new ReplyDTO();
        dto.setUuid(reply.getUuid());
        dto.setCreatedDate(reply.getCreatedDate());
        dto.setContent(reply.getContent());
        dto.setName(reply.getUser().getName());
        dto.setLastName(reply.getUser().getLastName());
        dto.setUser_photo(reply.getUser().getPhoto_profile_path());
        dto.setParentReplyUuid(reply.getParentReply() != null ? reply.getParentReply().getUuid() : null);
    
       
        dto.setReplies(reply.getReplies() != null ? 
            reply.getReplies().stream().map(this::convertToDTO).collect(Collectors.toList()) : new ArrayList<>());
        
        return dto;
    }
    
    
    private ReactionCounterDTO getReplyReactionCounterDTO(Reply reply) {

        ReactionCounterDTO reactionCounterDTO = new ReactionCounterDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = new Users();
        if (!email.equals(ANONYMOUS_USER)) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found: ", email));
        }
        List<ReplyReaction> replyReactions = replyReactionRepository.findByReplyId(reply.getUuid());
        List<EmojiType> emojiTypes = emojiTypeRepository.findAll();

        List<ReactionItemDTO> reactionItemDTOList = new ArrayList<>();
        Integer totalReplyReactions = replyReactions.size();

        for (EmojiType emojiType : emojiTypes) {
            Long reactionCounter =
                    replyReactions
                            .stream()
                            .filter(replyReaction -> replyReaction.getEmojiType().getEmoji_name().equals(emojiType.getEmoji_name()))
                            .count();

            ReactionItemDTO reactionItemDTO = new ReactionItemDTO();
            reactionItemDTO.setEmoji_type(emojiType.getEmoji_name());
            reactionItemDTO.setAmount(reactionCounter.intValue());

            reactionItemDTOList.add(reactionItemDTO);
        }

        String currentUserId = !email.equals(ANONYMOUS_USER) ? user.getUuid() : "";

        Optional<ReplyReaction> optionalReplyReaction = Optional.empty();
        if (!currentUserId.isEmpty()) {
            optionalReplyReaction =
                    replyReactions
                            .stream()
                            .filter(replyReaction -> replyReaction.getUser().getUuid().equals(currentUserId))
                            .findFirst();
        }

        String currentUserEmojiReaction = "";
        if(optionalReplyReaction.isPresent()) {
            ReplyReaction currentReplyReaction = optionalReplyReaction.get();
            currentUserEmojiReaction = currentReplyReaction.getEmojiType().getEmoji_name();
        }

        reactionCounterDTO.setMy_reaction_emoji(currentUserEmojiReaction);
        reactionCounterDTO.setTotal_reactions(totalReplyReactions);
        reactionCounterDTO.setReactions_by_type(totalReplyReactions != 0 ? reactionItemDTOList : new ArrayList<>());

        return reactionCounterDTO;
    }
}