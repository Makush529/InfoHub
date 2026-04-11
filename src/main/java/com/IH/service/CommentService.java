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

    public List<CommentDto> getPendingComments() {
        try {
            return commentRepository.getPendingComment();
        } catch (SQLException e) {
            log.error("Error getting pending comments", e);
            return List.of();
        }
    }

    public boolean approveComment(Long commentId) {
        try {
            return commentRepository.updateCommentStatus(commentId, CommentStatus.APPROVED);
        } catch (SQLException e) {
            log.error("Error approving comment: {}", commentId, e);
            return false;
        }
    }

    public boolean rejectComment(Long commentId) {
        try {
            return commentRepository.updateCommentStatus(commentId, CommentStatus.REJECTED);
        } catch (SQLException e) {
            log.error("Error rejecting comment: {}", commentId, e);
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
