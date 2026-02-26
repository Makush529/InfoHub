package com.IH.repository;

import com.IH.SQLCommands;
import com.IH.model.dto.rest.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Connection connection;

    @Autowired
    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<UserDto> getUserById(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.GET_USER_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserDto user = new UserDto();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setBirthDate(LocalDate.parse(resultSet.getString("user_age")));
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty(); // Если юзер не найден
    }
}

