package com.IH;

public interface SQLCommandsComments {
    String CREATE_COMMENT =
            "INSERT INTO comments (content, comment_date, post_id, user_id, status) " +
                    "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?) RETURNING id";

    String GET_COMMENT_BY_POST =
            "SELECT  c.id, c.content, c.comment_date, c.status, c.post_id, " +
                    "u.id as author_id, u.username as author_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE c.post_id = ? AND c.status = 'APPROVED' " +
                    "ORDER BY c.comment_date DESC";

    String GET_PENDING_COMMENTS =
            "SELECT c.id, c.content, c.comment_date, c.status, " +
                    "       u.id as author_id, u.username as author_name, " +
                    "       c.post_id " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE c.status = 'PENDING' " +
                    "ORDER BY c.comment_date ASC";

    String UPDATE_COMMENT_STATUS =
            "UPDATE comments set status = ? where id = ?";

    String DELETE_COMMENT =
            "DELETE FROM comments WHERE id = ? ";

    String GET_COMMENT_AUTHOR =
            "SELECT user_id FROM comments WHERE id = ?";
}
