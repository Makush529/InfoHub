package com.IH.repository;


import com.IH.SQLCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class SecurityRepository {
    private final Connection connection;

    @Autowired
    public SecurityRepository(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(String login, String password, String firstname, LocalDate age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLCommands.REGISTER_USER)) {
            statement.setString(1, firstname); // first_name
            statement.setObject(2, age);       // user_age
            statement.setString(3, login);     // login
            statement.setString(4, password);  // password
            statement.execute();
        }
    }
}
