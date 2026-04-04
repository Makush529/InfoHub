package com.IH;

public interface SQLCommandsComments {
    String CREATE_COMMENT =
            "INSERT INTO comments (content, comment_date, post_id, user_id, status) " +
                    "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?) RETURNING id";

    String GET_COMMENT_BY_POST =
            "SELECT  c.id, c.content, c.comment_date, c.status, " +
                    "u.id as author_id, u.name as author_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE c.post_id = ? AND c.status = 'APPROVED' " +
                    "ORDER BY c.comment_date DESC";

    String GET_PENDING_COMMENTS =
            "SELECT  c.id, c.content, c.comment_date, c.status, " +
                    "u.id as author_id, u.name as author_name, " +
                    "FROM comments c " +
                    "JOIN users u c.user_id = u.id " +
                    "WHERE c.post_id = ? AND c.status = 'PENDING' " +
                    "ORDER BY c.comment_date DESC";

    String UPDATE_COMMENT_STATUS =
            "UPDATE comments set status = ? where id = ?";

    String IS_COMMENT_OWNER =
            "SELECT 1 FROM comments WHERE id = ? and user_id = ?";

    String DELETE_COMMENT =
            "DELETE FROM comments WHERE id = ? ";


}
