package com.IH.repository;

import com.IH.SQLCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Repository
public class LogRepository {

    private final Connection connection;

    @Autowired
    public LogRepository(Connection connection) {
        this.connection = connection;
    }

    public void saveLog(Long userId, String action, String details) {
        if (userId == null) return;

        try (PreparedStatement ps = connection.prepareStatement(SQLCommands.INSERT_LOG)) {
            ps.setLong(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
            log.debug("Log saved: user={}, action={}", userId, action);
        } catch (SQLException e) {
            log.error("Failed to save log: user={}, action={}", userId, action, e);
        }
    }
}