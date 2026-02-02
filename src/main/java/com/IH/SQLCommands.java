package com.IH;

public interface SQLCommands {
    String AUTH_USER =
            "SELECT users.id, users.username FROM security " +
                    "JOIN users ON security.user_id = users.id " +
                    "WHERE security.login = ? AND security.password = ?";
    String REGISTER_USER =
            "WITH inserted_user AS (" +
                    "  INSERT INTO users (username, user_age) " +
                    "  VALUES (?, ?) " +
                    "  RETURNING id) " +
                    "INSERT INTO public.security (login, password, user_id) " +
                    "VALUES (?, ?, (SELECT id FROM inserted_user))";
    String CREATE_POST = "INSERT INTO posts (post_name, title, post_age, user_id) VALUES (?, ?, CURRENT_DATE, ?)";
    String GET_ALL_POSTS =
            "SELECT posts.id, posts.post_name, posts.title, posts.post_age, users.username " +
                    "FROM posts JOIN users ON posts.user_id = users.id " +
                    "ORDER BY posts.id DESC";
}
