package com.IH.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            System.out.println("Попытка подключения к: " + dbURL + " под пользователем: " + dbName);
            return DriverManager.getConnection(dbURL, dbName, dbPassword);
        } catch (SQLException e) {
            System.err.println("ОШИБКА ПОДКЛЮЧЕНИЯ: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

