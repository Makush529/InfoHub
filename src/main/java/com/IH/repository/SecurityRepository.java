package com.IH.repository;


import com.IH.SQLCommands;
import com.IH.model.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class SecurityRepository {
    private final Connection connection;

    @Autowired
    public SecurityRepository(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(String login, String password, String username, LocalDate age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.REGISTER_USER)) {
            statement.setString(1, username);
            statement.setObject(2, age);
            statement.setString(3, login);
            statement.setString(4, password);
            statement.execute();
        }
    }
    public UserResponse getUserByCredentials(String login, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.AUTH_USER)) {
            statement.setString(1, login);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserResponse user  = new UserResponse();
                    user.setUsername(resultSet.getString("username"));
                    user.setId(resultSet.getLong("id"));
                    return user;
                }
            }
        }
        return null;
    }
}
