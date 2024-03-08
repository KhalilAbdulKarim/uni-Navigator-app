package com.uninavigator.uninavigatorapp.services;
import java.sql.*;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.controllers.Course;

public class CourseService {
    private DBHandler dbHandler;
    public CourseService(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public boolean createCourse(String courseName, int instructorId, String schedule, String description, int capacity, Date startDate, Date endDate) {
        String query = "INSERT INTO Course (CourseName, InstructorID, Schedule, Description, Capacity, StartDate, EndDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courseName);
            stmt.setInt(2, instructorId);
            stmt.setString(3, schedule);
            stmt.setString(4, description);
            stmt.setInt(5, capacity);
            stmt.setDate(6, startDate);
            stmt.setDate(7, endDate);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateCourse(int courseId, String courseName, int instructorId, String schedule, String description, int capacity, Date startDate, Date endDate) {
        String query = "UPDATE Course SET CourseName = ?, InstructorID = ?, Schedule = ?, Description = ?, Capacity = ?, StartDate = ?, EndDate = ? WHERE CourseID = ?";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courseName);
            stmt.setInt(2, instructorId);
            stmt.setString(3, schedule);
            stmt.setString(4, description);
            stmt.setInt(5, capacity);
            stmt.setDate(6, startDate);
            stmt.setDate(7, endDate);
            stmt.setInt(8, courseId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String query = "DELETE FROM Course WHERE CourseID = ?";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Course getCourseByName(String courseName) {
        String query = "SELECT * FROM Course WHERE CourseName LIKE ?";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + courseName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming you have a Course class to map the result
                    return new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getInt("InstructorID"),
                            rs.getString("Schedule"),
                            rs.getString("Description"),
                            rs.getInt("Capacity"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }









}
