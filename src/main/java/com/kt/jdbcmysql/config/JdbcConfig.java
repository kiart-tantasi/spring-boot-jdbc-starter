package com.kt.jdbcmysql.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfig {

    @Value("${jdbc.config.connectionstring}")
    private String connectionString;

    @Value("${jdbc.config.username}")
    private String username;

    @Value("${jdbc.config.password}")
    private String password;

    @Bean
    public Statement getStatement() throws Exception {
        try {
            Connection connection = DriverManager.getConnection(connectionString, username, password);
            Statement statement = connection.createStatement();
            return statement;
        } catch (Exception ex) {
            throw new Exception("MySQL Connection or Statement failed.");
        }
    }

    @Bean
    Connection getConnection() throws Exception {
        try {
            return DriverManager.getConnection(connectionString, username, password);
        } catch (Exception ex) {
            throw new Exception("MySQL Connection failed.");
        }
    }

}
