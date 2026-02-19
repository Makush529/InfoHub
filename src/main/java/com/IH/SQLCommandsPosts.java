package com.IH;

public interface SQLCommandsPosts {
    String CREATE_POST =
            "INSERT INTO posts (post_title, text, post_age, user_id, status) " +
                    "VALUES (?, ?, CURRENT_DATE, ?, 'PENDING')";
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
    String GET_ALL_POSTS =//устаревший!!!!!!!!!!!!
            "SELECT posts.id, posts.post_title, posts.text, posts.post_age, users.username " +
                    "FROM posts JOIN users ON posts.user_id = users.id " +
                    "ORDER BY posts.id DESC";
    // Проверка, ставил ли пользователь лайк
    String CHECK_USER_LIKE =
            "SELECT 1 FROM user_likes WHERE post_id = ? AND user_id = ?";

    // Проверка, ставил ли пользователь дизлайк
    String CHECK_USER_DISLIKE =
            "SELECT 1 FROM user_dislikes WHERE post_id = ? AND user_id = ?";

    // Поставить лайк
    String ADD_LIKE =
            "INSERT INTO user_likes (post_id, user_id) VALUES (?, ?)";

    // Поставить дизлайк
    String ADD_DISLIKE =
            "INSERT INTO user_dislikes (post_id, user_id) VALUES (?, ?)";

    // Убрать лайк
    String REMOVE_LIKE =
            "DELETE FROM user_likes WHERE post_id = ? AND user_id = ?";

    // Убрать дизлайк
    String REMOVE_DISLIKE =
            "DELETE FROM user_dislikes WHERE post_id = ? AND user_id = ?";
}
