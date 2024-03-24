package com.uninavigator.uninavigatorapp.services;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.UserRequestModel;
import com.uninavigator.uninavigatorapp.controllers.User;
import utils.SessionContext;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    // update user , except his role
    public boolean updateUser(int userId, String username, String email, String password, String firstName, String lastName, LocalDate dob) {
        String updateSQL = "UPDATE User SET Username = ?, Email = ?, Password = ?, FirstName = ?, LastName = ?, DOB = ? WHERE UserID = ?;";

        try (Connection connection = dbHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, firstName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setDate(6, java.sql.Date.valueOf(dob));
            preparedStatement.setInt(7, userId);

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


    public boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) FROM User WHERE Username = ?";

        try (Connection connection = dbHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If count is greater than 0, then the username exists
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean requestInstructorStatus(int userId) {
        String updateSQL = "UPDATE User SET RequestStatus = 'Requested' WHERE UserID = ?;";

        try (Connection connection = dbHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setInt(1, userId);
            int result = preparedStatement.executeUpdate();

            return result > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    public boolean declineInstructorRequest(int userId) {
        String sql = "UPDATE User SET Role = 'Student', RequestStatus = 'Declined' WHERE UserID = ?;";

        try (Connection connection = dbHandler.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean approveInstructorRequest(int userId) {
        String sql = "UPDATE User SET Role = 'Instructor', RequestStatus = 'Approved' WHERE UserID = ?;";

        try (Connection connection = dbHandler.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserRequestModel> getAllInstructorRequests() {
        String sql = "SELECT * FROM User WHERE RequestStatus = 'Requested';";
        List<UserRequestModel> requests = new ArrayList<>();

        try (Connection connection = dbHandler.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserRequestModel request = new UserRequestModel(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getDate("DOB").toLocalDate()); // Assuming DOB is stored as a String
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }


//    public User getUserDetailsByUsername(String currentUsername) {
//        User user = null;
//        String query = "SELECT * FROM User WHERE Username = ?";
//
//        try (Connection conn = dbHandler.connect();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//            pstmt.setString(1, currentUsername);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                int userId = rs.getInt("UserID");
//                String username = rs.getString("Username");
//                String email = rs.getString("Email");
//                String firstName = rs.getString("FirstName");
//                String lastName = rs.getString("LastName");
//                String role = rs.getString("Role");
//                // Format the DOB to a string for the User constructor
//                String dob = rs.getDate("DOB").toLocalDate().toString();
//
//                user = new User(userId, username, email, firstName, lastName, role, dob);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return user;
//    }

}


