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
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";//поиск через юзер(старый)
    String GET_USER_BY_ID_FULL =//поиск через authController
            "SELECT u.id, u.username, u.user_age, s.login " +//добавить рейтинги и роль
                    "FROM users u JOIN security s ON u.id = s.user_id " +
                    "WHERE u.id = ?";

}
