package com.IH;

public interface SQLCommands {
    String AUTH_USER =
            "SELECT users.id, users.username FROM security " +
                    "JOIN users ON security.user_id = users.id " +
                    "WHERE security.login = ? AND security.password = ?";

    String REGISTER_USER =
            "WITH inserted_user AS (" +
                    "    INSERT INTO users (username, user_age) VALUES (?, ?) RETURNING id" +
                    ") " +
                    "INSERT INTO security (login, password, user_id) " +
                    "VALUES (?, ?, (SELECT id FROM inserted_user)) " +
                    "RETURNING user_id";
    String GET_USER_BY_LOGIN =
            "SELECT u.id, u.username, s.login, s.password " +
                    "FROM security s " +
                    "JOIN users u ON s.user_id = u.id " +
                    "WHERE s.login = ?";

    String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";//TODO поиск через юзер(старый)

    String GET_USER_BY_ID_FULL =
            "SELECT u.id, u.username, u.user_age, s.login " +
                    "FROM users u JOIN security s ON u.id = s.user_id " +
                    "WHERE u.id = ?";

    String ADD_USER_ROLE =
            "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

    String GET_USER_ROLE =
            "SELECT role FROM user_roles WHERE user_id = ?";

    String GET_USER_RATING =//TODO рассмотреть вариант хранения рейтинга в бд, запись делать вместе с лайком или дизлайком
            "SELECT COALESCE(SUM(" +
                    "(SELECT COUNT(*) FROM user_likes WHERE post_id = p.id) - " +
                    "(SELECT COUNT(*) FROM user_dislikes WHERE post_id = p.id) " +
                    "), 0) as rating " +
                    "FROM posts p WHERE p.user_id = ?";

    String INSERT_LOG =
            "INSERT INTO logs (user_id, action, details, created_at) " +
                    "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

}
