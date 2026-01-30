package com.IH.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.IH")
public class SpringConfig {

    @Value("spring.datasource.url")
    private String dbURL;
    @Value("spring.datasource.name")
    private String dbName;
    @Value("spring.datasource.password")
    private String dbPassword;

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    dbURL,
                    dbName,
                    dbPassword);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            throw new RuntimeException(e);
        }
    }
}

