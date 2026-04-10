package com.IH.repository;


import com.IH.SQLCommands;
import com.IH.model.dto.responce.UserResponse;
import com.IH.model.dto.UserRole;
import com.IH.model.dto.responce.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class SecurityRepository {
    private final Connection connection;

    @Autowired
    public SecurityRepository(Connection connection) {
        this.connection = connection;
    }

    public Long registerUser(String login, String password, String username, LocalDate age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.REGISTER_USER)) {
            statement.setString(1, username);
            statement.setObject(2, age);
            statement.setString(3, login);
            statement.setString(4, password);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("user_id");
                }
            }
        }throw new SQLException("Failed to register user, no ID returned");
    }

    public UserResponse getUserByLogin(String login) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(SQLCommands.GET_USER_BY_LOGIN)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserResponse user = new UserResponse();
                    user.setId(rs.getLong("id"));
                    user.setLogin(rs.getString("login"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password"));
                    return user;
                }
            }
        }
        return null;
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

    public Optional<UserDto> getUserById(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.GET_USER_BY_ID_FULL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserDto user = new UserDto();
                    user.setId(resultSet.getLong("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setUsername(resultSet.getString("username"));
                    if (resultSet.getDate("user_age") != null) {
                        user.setBirthDate(resultSet.getDate("user_age").toLocalDate());
                    }
                    user.setRating(0);
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty(); // Если юзер не найден
    }

    public void addUserRole(Long userId, UserRole role) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommands.ADD_USER_ROLE)) {
            ps.setLong(1, userId);
            ps.setString(2, role.name());
            ps.executeUpdate();
        }
    }
}
