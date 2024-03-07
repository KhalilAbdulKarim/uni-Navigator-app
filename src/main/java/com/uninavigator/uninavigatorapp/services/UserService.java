package com.uninavigator.uninavigatorapp.services;
import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.controllers.User;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public User authenticateUser (String username , String plainPassword){
        String query = "SELECT * FROM User WHERE Username = ? AND Password = ?";

        try(Connection conn = dbHandler.connect();
            PreparedStatement st = conn.prepareStatement(query)){

            st.setString(1, username);
            st.setString(2, plainPassword);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                LocalDate dob = rs.getDate("DOB").toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dobString = dob.format(formatter);
                // User found with matching username and password
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Role"),
                        dobString
                );
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}


