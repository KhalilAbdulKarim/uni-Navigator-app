package com.uninavigator.uninavigatorapp.controllers;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.services.CourseService;
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
import utils.SessionContext;
import utils.StageHandler;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;


import java.io.IOException;
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

    /**
     * Constructor that initializes the CourseService with a DBHandler instance.
     */

    public StudentDashboard() {
        this.courseService = new CourseService(DBHandler.getInstance());
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
        List<Course> coursesList = new ArrayList<>();
        User currentUser = SessionContext.getCurrentUser();

        if (currentUser != null) {
            if ("Admin".equals(currentUser.getRole()) || "Student".equals(currentUser.getRole())) {
                coursesList = courseService.getAllCourses();
            } else if ("Instructor".equals(currentUser.getRole())) {
                coursesList = courseService.getCoursesByInstructor(currentUser.getUserId());
            }
        }
        return FXCollections.observableArrayList(coursesList);
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
            Course searchedCourse = courseService.getCourseByName(searchQuery);
            if (searchedCourse != null) {
                ObservableList<Course> courseList = FXCollections.observableArrayList(searchedCourse);
                coursesTableView.setItems(courseList);
            } else {
                coursesTableView.setItems(FXCollections.observableArrayList());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Result");
                alert.setHeaderText(null);
                alert.setContentText("No course was found matching your search.");
                alert.showAndWait();
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
}

