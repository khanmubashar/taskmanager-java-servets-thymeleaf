package com.mubasharkhan.config;

import com.mubasharkhan.exceptions.PropertiesFileReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlJDBCConfig  {


    private static final Logger logger = LoggerFactory.getLogger(MysqlJDBCConfig.class);
    private static String jdbcUrl;
    private static String username;
    private static String password;
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
       try {
           if (connection == null || connection.isClosed()) {
               connection = initializeConnection();
           }
           return connection;
       } catch (PropertiesFileReadException e) {
           System.out.println("Error while reading properties file");
           throw new SQLException("Failed to establish connection");
       }
    }

    private static Connection initializeConnection() {
       properties();
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            return connection;
        } catch (SQLException e) {
            logger.error("Error while initializing connection", e);
            throw new RuntimeException("Error while initializing connection", e);
        }
    }

    private static Properties properties() {
        Properties properties = new Properties();
        try (InputStream inputStream = MysqlJDBCConfig.class.getResourceAsStream("/properties/mysqljdbc-config.properties")) {
            if (inputStream == null) {
                throw new PropertiesFileReadException("properties/mysqljdbc2-config.properties not found");
            }
            properties.load(inputStream);
            jdbcUrl = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            return  properties;
        } catch (IOException e) {
            logger.error("Error while reading properties file", e);
            throw new RuntimeException("Error while reading properties file", e);
        }
    }

}
