package com.uninavigator.uninavigatorapp.controllers.user;

import com.uninavigator.uninavigatorapp.ApiServices.CourseService;
import com.uninavigator.uninavigatorapp.controllers.course.Course;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.SessionContext;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller class for the StudentDashboard view.
 * Manages UI interactions and data display for student users, including course listings and searches.
 */

public class StudentDashboard {
    @FXML
    private StackPane contentArea;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Course> coursesTableView;
    @FXML
    private TableColumn<Course, Integer> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> instructorNameColumn;
    @FXML
    private TableColumn<Course, String> scheduleColumn;
    @FXML
    private TableColumn<Course, String> descriptionColumn;
    @FXML
    private TableColumn<Course, Integer> capacityColumn;
    @FXML
    private TableColumn<Course, String> startDateColumn;
    @FXML
    private TableColumn<Course, String> endDateColumn;
    private CourseService courseService;
    public StudentDashboard() {
        this.courseService = new CourseService();
    }


    /**
     * Initializes the StudentDashboard controller.
     * This method sets up the courses table view and displays the courses table.
     */

    @FXML
    private void initialize() {
        setupCoursesTableView();
        showCoursesTable();
    }

    /**
     * Sets up the courses table view by specifying the cell value factories for each column.
     */

    private void setupCoursesTableView() {
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        instructorNameColumn.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

    }

    /**
     * Shows the courses table by making it visible and managed.
     * Populates the table with courses based on the current user's role.
     */

    public void showCoursesTable() {
        coursesTableView.setVisible(true);
        coursesTableView.setManaged(true);
        coursesTableView.setItems(getCoursesForUser());
    }

    /**
     * Fetches a list of courses for the current user based on their role.
     * Admin and Student roles can see all courses, while Instructors see only their courses.
     *
     * @return An ObservableList of courses for the current user.
     */

    private ObservableList<Course> getCoursesForUser() {
        try {

            JSONArray coursesArray;
            User currentUser = SessionContext.getCurrentUser();

            if (currentUser != null) {
                if ("Admin".equals(currentUser.getRole()) || "Student".equals(currentUser.getRole())) {
                    coursesArray = courseService.getAllCourses();
                } else if ("Instructor".equals(currentUser.getRole())) {
                    coursesArray = courseService.getCoursesByInstructor(currentUser.getUserId());
                } else {
                    coursesArray = new JSONArray();
                }
                return convertJSONArrayToCourses(coursesArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not fetch courses: " + e.getMessage());
            return FXCollections.observableArrayList();

        }
        return FXCollections.observableArrayList();
    }
    /**
     * Helper method to convert JSONArray of courses to ObservableList<Course>.
     *
     * @param coursesArray JSONArray of course JSON objects.
     * @return ObservableList of Course objects.
     */
    private ObservableList<Course> convertJSONArrayToCourses(JSONArray coursesArray) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject courseObject = coursesArray.getJSONObject(i);
            Course course = new Course(
                    courseObject.getInt("courseId"),
                    courseObject.getString("courseName"),
                    courseObject.getJSONObject("instructor").getString("username"),
                    courseObject.getString("schedule"),
                    courseObject.getString("description"),
                    courseObject.getInt("capacity"),
                    dateFormat.parse(courseObject.getString("startDate")),
                    dateFormat.parse(courseObject.getString("endDate"))
            );
        }
        return FXCollections.observableArrayList(courses);
    }

    /**
     *
     * @param actionEvent to handle loading user's course enrollement
     */

    @FXML
    public void loadCourseEnrollment(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/courseEnrollment.fxml"));
//            Node profileView = loader.load();
//            contentArea.getChildren().clear();
//            contentArea.getChildren().add(profileView);
            Node view = loader.load();
            contentArea.getChildren().setAll(view);


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     *
     * @param actionEvent to handle loading user's profile
     */

    public void loadProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/profileView.fxml"));
            Node profileView = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileView);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void loadAcademicRecords(ActionEvent actionEvent) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/.fxml"));
//            Node profileView = loader.load();
//            contentArea.getChildren().clear();
//            contentArea.getChildren().add(profileView);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
    }

    /**
     *
     * @param actionEvent to handle closing the app and stopping run the app
     */
    public void handleExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     *
     * @param actionEvent to handle loading User Dashboard
     */
    public void loadDashboard(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/studentDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Dashboard");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for courses based on the input in the search field.
     * Displays the search results in the courses table.
     *
     * @param event The action event triggered by pressing the search button.
     */


    @FXML
    private void handleSearch(ActionEvent event) {
        String searchQuery = searchField.getText();
        if (!searchQuery.isEmpty()) {
            try {
                List<Course> courses = courseService.searchCourse(searchQuery);
                if (!courses.isEmpty()) {
                    ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
                    coursesTableView.setItems(courseList);
                } else {
                    coursesTableView.setItems(FXCollections.observableArrayList());
                    showAlert("Search Result", "No courses found matching your search.");
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to search courses: " + e.getMessage());
            }
        } else {
            showCoursesTable();
        }
    }

    /**
     *
     * @param actionEvent to handle logging out the user out of the app
     */
    public void handleLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("uni-Navigator");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

