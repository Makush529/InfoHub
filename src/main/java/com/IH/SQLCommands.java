package com.IH;

public interface SQLCommands {
    String GET_ALL_USERS = "SELECT * FROM users";
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    String CREATE_USER = "INSERT INTO public.users (first_name, user_age) VALUES (?, ?)";
    String REGISTER_USER =
            "WITH inserted_user AS (" +
                    "  INSERT INTO public.users (first_name, user_age) " +
                    "  VALUES (?, ?) " +
                    "  RETURNING id" +
                    ") " +
                    "INSERT INTO public.security (login, password, user_id) " +
                    "VALUES (?, ?, (SELECT id FROM inserted_user))";
}
