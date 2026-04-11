package com.IH;

public interface SQLCommandsPosts {
    String CREATE_POST =
            "INSERT INTO posts (post_title, text, post_age, user_id, status) " +
                    "VALUES (?, ?, CURRENT_DATE, ?, ?)";
    String GET_ALL_PUBLISHED_POSTS =
            "SELECT p.id, p.post_title, p.text, p.post_age, p.status, " +
                    "       u.id as author_id, u.username as author_name, " +
                    "       COALESCE(ul.likes, 0) as likes_count, " +
                    "       COALESCE(ud.dislikes, 0) as dislikes_count " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "LEFT JOIN (SELECT post_id, COUNT(*) as likes FROM user_likes GROUP BY post_id) ul ON p.id = ul.post_id " +
                    "LEFT JOIN (SELECT post_id, COUNT(*) as dislikes FROM user_dislikes GROUP BY post_id) ud ON p.id = ud.post_id " +
                    "WHERE p.status = 'APPROVED' " +
                    "ORDER BY p.post_age DESC";
    String GET_POST_BY_ID =
            "SELECT p.id, p.post_title, p.text, p.post_age, p.status, " +
                    "       u.id as author_id, u.username as author_name, " +
                    "       COALESCE(ul.likes, 0) as likes_count, " +
                    "       COALESCE(ud.dislikes, 0) as dislikes_count " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "LEFT JOIN (SELECT post_id, COUNT(*) as likes FROM user_likes GROUP BY post_id) ul ON p.id = ul.post_id " +
                    "LEFT JOIN (SELECT post_id, COUNT(*) as dislikes FROM user_dislikes GROUP BY post_id) ud ON p.id = ud.post_id " +
                    "WHERE p.id = ?";

    String GET_ALL_POSTS =
            "SELECT posts.id, posts.post_title, posts.text, posts.post_age, users.username " +
                    "FROM posts JOIN users ON posts.user_id = users.id " +
                    "ORDER BY posts.id DESC";

    String GET_PENDING_POSTS =
            "SELECT p.id, p.post_title, p.text, p.post_age, p.status, " +
                    "       u.id as author_id, u.username as author_name " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.status = 'PENDING' " +
                    "ORDER BY p.post_age ASC";

    String DELETE_POST_BY_ID =
            "DELETE FROM posts WHERE id = ?";

    String UPDATE_POST_STATUS =
            "UPDATE posts SET status = ? WHERE id = ?";

    String CHECK_USER_LIKE =
            "SELECT 1 FROM user_likes WHERE post_id = ? AND user_id = ?";

    String CHECK_USER_DISLIKE =
            "SELECT 1 FROM user_dislikes WHERE post_id = ? AND user_id = ?";

    String ADD_LIKE =
            "INSERT INTO user_likes (post_id, user_id) VALUES (?, ?)";

    String ADD_DISLIKE =
            "INSERT INTO user_dislikes (post_id, user_id) VALUES (?, ?)";

    String REMOVE_LIKE =
            "DELETE FROM user_likes WHERE post_id = ? AND user_id = ?";

    String REMOVE_DISLIKE =
            "DELETE FROM user_dislikes WHERE post_id = ? AND user_id = ?";

    String GET_POST_AUTHOR =
            "SELECT user_id FROM posts WHERE id = ?";
}
