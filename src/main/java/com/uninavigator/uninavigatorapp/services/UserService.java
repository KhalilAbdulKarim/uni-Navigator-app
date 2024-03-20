package com.uninavigator.uninavigatorapp.services;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.controllers.User;
import utils.SessionContext;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

//import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private DBHandler dbHandler;

    public UserService(DBHandler dbHandler) {
        this.dbHandler = DBHandler.getInstance();
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
            preparedStatement.setDate(7, Date.valueOf(dob));

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

    public User getCurrentUserDetails() {
        User currentUser = null;
        String currentUsername = SessionContext.getCurrentUsername();

        String query = "SELECT * FROM User WHERE Username = ?";

        try(Connection conn = dbHandler.connect();
            PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String dob = rs.getString("DOB");
                String role = rs.getString("Role");


                currentUser = new User(userId,username,email,firstName,lastName,role,dob);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return currentUser;

    }

    public User getUserDetailsByUsername(String currentUsername) {
        User user = null;
        String query = "SELECT * FROM User WHERE Username = ?";

        try (Connection conn = dbHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String role = rs.getString("Role");
                // Format the DOB to a string for the User constructor
                String dob = rs.getDate("DOB").toLocalDate().toString();

                user = new User(userId, username, email, firstName, lastName, role, dob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}


