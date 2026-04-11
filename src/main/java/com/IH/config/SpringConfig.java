package com.IH.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Configuration
@ComponentScan("com.IH")
public class SpringConfig {

    @Value("${spring.datasource.url}")
    private String dbURL;
    @Value("${spring.datasource.name}")
    private String dbName;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Connection getConnection() {
        try {
            log.debug("Trying to connect to: " + dbURL + " under user: " + dbName);
            return DriverManager.getConnection(dbURL, dbName, dbPassword);
        } catch (SQLException e) {
            log.debug("CONNECTION ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

