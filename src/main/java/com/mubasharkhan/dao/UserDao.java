package com.mubasharkhan.dao;

import com.mubasharkhan.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public boolean insert(Connection connection, UserModel user) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO user");
        sql.append("(username, email, password, first_name, last_name, agree)");
        sql.append("VALUES");
        sql.append("(?, ?, ?, ?, ?, ?)");

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setBoolean(6, user.getAgree());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public boolean checkIfEmailExist(Connection connection, String email) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user WHERE email = ?");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
            return false;
        }
    }
}
