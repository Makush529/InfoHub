package com.IH;

public interface SQLCommands {
    String GET_ALL_USERS = "SELECT * FROM users";
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    String CREATE_USER = "INSERT INTO public.users (first_name, user_age) VALUES (?, ?)";
    String AUTH_USER =
            "SELECT users.first_name FROM security " +
                    "JOIN users ON security.user_id = users.id " +
                    "WHERE security.login = ? AND security.password = ?";
    String REGISTER_USER =
            "WITH inserted_user AS (" +
                    "  INSERT INTO public.users (first_name, user_age) " +
                    "  VALUES (?, ?) " +
                    "  RETURNING id" +
                    ") " +
                    "INSERT INTO public.security (login, password, user_id) " +
                    "VALUES (?, ?, (SELECT id FROM inserted_user))";
}
