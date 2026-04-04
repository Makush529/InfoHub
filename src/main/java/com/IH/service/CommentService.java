package com.IH.service;

import com.IH.model.dto.CommentStatus;
import com.IH.model.dto.request.CreateCommentRequest;
import com.IH.model.dto.responce.CommentDto;
import com.IH.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Long> createComment(CreateCommentRequest request, Long userId) throws SQLException {
        try {
            Long commentId = commentRepository.createComment(
                    request.getContent(),
                    request.getPostId(),
                    userId,
                    CommentStatus.PENDING
            );
            log.debug("Comment created with id {} for post {} ", commentId, request.getPostId());
            return Optional.ofNullable(commentId);
        } catch (SQLException e) {
            log.error("Error creating for post {} ", request.getPostId(), e);
            return Optional.empty();
        }
    }

    public List<CommentDto> getCommentsByPost(Long postId, Long userId){
        try {
            return commentRepository.getCommentByPost(postId, userId);
        } catch (SQLException e) {
            log.error("Error getting comments for post: {}", postId, e);
            return List.of();
        }
    }

    public List<CommentDto> getPendingComment() {
        try {
            return commentRepository.getPendingComment();
        } catch (SQLException e) {
            log.error("Error getting pending comments", e);
            return List.of();
        }
    }

    public boolean moderateComment(Long commentId, CommentStatus newStatus) {//TODO добавить проверку на админа, модератора!
        try {
            boolean updated = commentRepository.updateCommentStatus(commentId, newStatus);
            if (updated){
                log.info("Comment {} moderated to status: {}", commentId, newStatus);
            }
            return updated;
        } catch (SQLException e) {
            log.error("Error moderating comment: {}", commentId, e);
            return false;
        }
    }

    public boolean deleteComment(Long commentId, Long userId){
        try {
            if(!commentRepository.isCommentOwner(commentId, userId)){
                log.warn("Comment {} is not owned by user: {}", commentId, userId);
                return false;
            }
            return commentRepository.deleteComment(commentId);}
        catch (SQLException e) {
            log.error("Error deleting comment: {}", commentId, e);
            return false;
        }
    }
}
