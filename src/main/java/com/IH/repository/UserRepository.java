package com.IH.repository;

import com.IH.SQLCommands;
import com.IH.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Connection connection;

    @Autowired
    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> getUserById(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.GET_USER_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                // ОБЯЗАТЕЛЬНО проверяем, есть ли запись!
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    // ВАЖНО: Название в getString должно быть ТАКИМ ЖЕ, как в базе (username)
                    user.setUsername(resultSet.getString("username"));
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty(); // Если юзер не найден
    }
}

