package com.infsis.socialpagebackend.comments.services;

import com.infsis.socialpagebackend.comments.dtos.CommentDTO;
import com.infsis.socialpagebackend.comments.mappers.CommentMapper;
import com.infsis.socialpagebackend.enums.*;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.comments.models.Comment;
import com.infsis.socialpagebackend.posts.models.Post;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.comments.repositories.CommentRepository;
import com.infsis.socialpagebackend.posts.repositories.PostRepository;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.moderation.dtos.ModerationResponse;
import com.infsis.socialpagebackend.moderation.enums.ContentType;
import com.infsis.socialpagebackend.moderation.exceptions.ContentBlockedException;
import com.infsis.socialpagebackend.moderation.services.ModerationPipeline;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ModerationPipeline moderationPipeline;

    public CommentDTO saveComment(String postUuid, CommentDTO commentDTO) {

        String tenantId = TenantContext.getCurrentTenant();

        Users user = getCurrentUser();
        Post post = postRepository.findOneByUuid(postUuid);

        if (!post.isCommentsEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Los comentarios están desactivados en esta publicación");
        }

        // ── Moderación automática ────────────────────────────────────────────
        ModerationResponse modResult = moderationPipeline.moderate(commentDTO.getContent());

        if (modResult.isRejected()) {
            throw new ContentBlockedException(modResult.getReason());
        }

        // ── Determinar estado del comentario ─────────────────────────────────
        Comment comment = commentMapper.getComment(commentDTO, user, post, tenantId);

        boolean requiresManualReview = modResult.isNeedsReview();

        if (requiresManualReview) {
            comment.setModerated(true);
            comment.setState(CommentState.PENDING_APPROVAL.name());
        }
        // Si no requiere revisión, el estado ya viene como VISIBLE desde el mapper

        comment = commentRepository.save(comment);

        // ── Guardar auditoría de moderación ──────────────────────────────────
        moderationPipeline.saveResult(modResult, comment.getUuid(), ContentType.COMMENT);

        return commentMapper.toDTO(comment);
    }

    public List<CommentDTO> getCommentsByPost(String postUuid) {
        Post post = postRepository.findOneByUuid(postUuid);

        return post.getComments()
                .stream()
                .filter(comment ->
                        comment.getState().equals(CommentState.VISIBLE.name())
                        || comment.getState().equals(CommentState.APPROVED.name()))
                .map(comment -> {
                    CommentDTO commentDTO = commentMapper.toDTO(comment);
                    commentDTO.setReplyCount(getReplyCounter(comment.getUuid()));
                    return commentDTO;
                })
                .collect(Collectors.toList());
    }

    private int getReplyCounter(String commentUuid) {
        Comment comment = commentRepository.findByUuid(commentUuid);
        if (comment == null) {
            throw new NotFoundException("Comment", commentUuid);
        }
        return comment.getReplies().size();
    }

    public void deleteComment(String postUuid, String commentUuid) {
        Comment comment = commentRepository.findByUuid(commentUuid);
        if (comment == null) {
            throw new NotFoundException("Comment no found", commentUuid);
        }

        Users user = getCurrentUser();

        if (!comment.getUser().getUuid().equals(user.getUuid())) {
            throw new RuntimeException("No tienes permiso para eliminar este comentario");
        }

        commentRepository.delete(comment);
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: ", email));
    }

    public List<CommentDTO> getAllPendingModeratedComments() {
        String tenantId = TenantContext.getCurrentTenant();
        return commentRepository
                .findAllByInstitutionIdAndState(tenantId, CommentState.PENDING_APPROVAL.name())
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getAllRejectedModeratedComments() {
        String tenantId = TenantContext.getCurrentTenant();
        return commentRepository
                .findAllByInstitutionIdAndState(tenantId, CommentState.REJECTED.name())
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public long countModeratedComments() {
        String tenantId = TenantContext.getCurrentTenant();
        return commentRepository.countByTenantAndState(tenantId, CommentState.PENDING_APPROVAL.name());
    }

    public CommentDTO approvePendingModeratedComment(CommentDTO commentDTO) {

        Comment currentComment = commentRepository.findByUuid(commentDTO.getUuid());

        currentComment.setState(CommentState.APPROVED.name());
        currentComment.setModerated(false);

        commentRepository.save(currentComment);

        return commentMapper.toDTO(currentComment);
    }

    public CommentDTO rejectPendingModeratedComment(CommentDTO commentDTO) {

        Comment currentComment = commentRepository.findByUuid(commentDTO.getUuid());

        currentComment.setState(CommentState.REJECTED.name());

        commentRepository.save(currentComment);

        return commentMapper.toDTO(currentComment);
    }
    public CommentDTO removeModeratedComment(CommentDTO commentDTO) {

        Comment currentComment = commentRepository.findByUuid(commentDTO.getUuid());

        currentComment.setState(CommentState.REMOVED.name());

        commentRepository.save(currentComment);

        return commentMapper.toDTO(currentComment);
    }
    public List<CommentDTO> getAllDeletedModeratedComments() {
        String tenantId = TenantContext.getCurrentTenant();
        return commentRepository
                .findAllByInstitutionIdAndState(tenantId, CommentState.REMOVED.name())
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}