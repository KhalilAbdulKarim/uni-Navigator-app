package com.uninavigator.uninavigatorapp.services;
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
//import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private DBHandler dbHandler;

    public UserService(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public boolean createUser(String username, String password, String email, String firstName, String lastName, String role, LocalDate dob) {
        String insertUserSQL = "INSERT INTO User (Username, Email,Password ,FirstName, LastName, Role, DOB) VALUES (?, ?, ?, ?, ?, ?, ?);";
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection connection = dbHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, firstName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setString(6, role);
            preparedStatement.setDate(7, java.sql.Date.valueOf(dob));

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


