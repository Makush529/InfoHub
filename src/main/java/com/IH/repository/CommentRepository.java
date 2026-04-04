package com.IH.repository;

import com.IH.SQLCommandsComments;
import com.IH.model.dto.CommentStatus;
import com.IH.model.dto.responce.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {
    private final Connection connection;

    @Autowired
    public CommentRepository(Connection connection) {
        this.connection = connection;
    }

    public Long createComment(String content, Long postId, Long userId, CommentStatus status) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.CREATE_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, content);
            ps.setLong(2, postId);
            ps.setLong(3, userId);
            ps.setString(4, status.name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to create comment, no ID returned");//TODO доделать исключения
    }

    public List<CommentDto> getCommentByPost(Long postId/*, Long currentUserId*/) throws SQLException {
        List<CommentDto> comments = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.GET_COMMENT_BY_POST)) {
            ps.setLong(1, postId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comments.add(new CommentDto());//TODO mapRsultSet in()
                }
            }
        }
        return comments;
    }

    public List<CommentDto> getPendingComment() throws SQLException {
        List<CommentDto> comments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.GET_PENDING_COMMENTS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comments.add(mapResultSetToCommentDto(rs, null));
            }
        }
        return comments;
    }

    public boolean isCommentOwner(Long commentId, Long userId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.IS_COMMENT_OWNER)) {
            ps.setLong(1, commentId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean updateCommentStatus(Long commentId, CommentStatus status) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.UPDATE_COMMENT_STATUS)) {
            ps.setString(1, status.name());
            ps.setLong(2, commentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteComment(Long commentId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsComments.DELETE_COMMENT)) {
            ps.setLong(1, commentId);
            return ps.executeUpdate() > 0;
        }
    }

    private CommentDto mapResultSetToCommentDto(ResultSet rs, Long currentUserId) throws SQLException {//TODO глянуть
        CommentDto comment = new CommentDto();
        comment.setId(rs.getLong("id"));
        comment.setContent(rs.getString("content"));
        comment.setCreatedAt(rs.getDate("comment_date").toLocalDate());
        comment.setAuthorId(rs.getLong("author_id"));
        comment.setAuthorName(rs.getString("author_name"));
        comment.setStatus(CommentStatus.valueOf(rs.getString("status")));
        comment.setPostId(rs.getLong("post_id"));

        if (currentUserId != null) {
            comment.setCanEdit(currentUserId.equals(comment.getAuthorId()));
            comment.setCanDelete(currentUserId.equals(comment.getAuthorId()));
        }
        return comment;
    }
}
