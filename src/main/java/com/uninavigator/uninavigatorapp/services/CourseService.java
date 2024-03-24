package com.uninavigator.uninavigatorapp.services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.controllers.Course;

public class CourseService {
    private DBHandler dbHandler;
    public CourseService(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String query = "SELECT Course.CourseID, Course.CourseName, CONCAT(User.firstName, ' ', User.lastName) AS InstructorName, " +
                "Course.Schedule, Course.Description, Course.Capacity, Course.StartDate, Course.EndDate " +
                "FROM Course " +
                "JOIN User ON Course.InstructorID = User.UserID WHERE User.Role = 'Instructor'";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("InstructorName"),
                        rs.getString("Schedule"),
                        rs.getString("Description"),
                        rs.getInt("Capacity"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"));
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
    public List<Course> getCoursesByInstructor(int instructorId) {
        List<Course> courseList = new ArrayList<>();
        String query = "SELECT c.CourseID, c.CourseName, CONCAT(u.firstName, ' ', u.lastName) AS InstructorName, c.Schedule, c.Description, c.Capacity, c.StartDate, c.EndDate "
                        + "FROM Course c "
                        + "JOIN User u ON c.InstructorID = u.UserID WHERE u.UserID = ?";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getInt("courseId"),
                            rs.getString("courseName"),
                            rs.getString("InstructorName"),
                            rs.getString("schedule"),
                            rs.getString("description"),
                            rs.getInt("Capacity"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate")
                    );
                    courseList.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }


    public Course getCourseByName(String courseName) {
        String query = "SELECT c.CourseID, c.CourseName, CONCAT(u.firstName, ' ', u.lastName) AS InstructorName, " +
                "c.Schedule, c.Description, c.Capacity, c.StartDate, c.EndDate " +
                "FROM Course c " +
                "JOIN User u ON c.InstructorID = u.UserID " +
                "WHERE c.CourseName LIKE ?";
        try (Connection conn = dbHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + courseName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("InstructorName"),
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

}
