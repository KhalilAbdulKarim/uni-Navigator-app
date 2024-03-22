package com.uninavigator.uninavigatorapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import utils.StageHandler;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;


import java.io.IOException;

public class StudentDashboard {
    @FXML
    private StackPane contentArea;
    @FXML
    private TableView<Course> coursesTableView;
    @FXML
    private TableColumn<Course, Integer> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Integer> instructorIdColumn;
    @FXML
    private TableColumn<Course, String> scheduleColumn;
    @FXML
    private TableColumn<Course, String> descriptionColumn;
//    StageHandler stageHandler;

    @FXML
    private void initialize() {
        // Initialize the TableView columns and other necessary setup
        setupCoursesTableView();

        // Initially, you may want to load the courses when the dashboard loads
        // showCoursesTable(); // Uncomment if you want the table to show immediately on dashboard load
    }

    private void setupCoursesTableView() {
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        instructorIdColumn.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    }

    public void showCoursesTable() {
        // Set visibility and managed properties to true to display the table
        coursesTableView.setVisible(true);
        coursesTableView.setManaged(true);

        // Fetch and display the course data
        coursesTableView.setItems(getCoursesForStudent());
    }

    // Dummy method to simulate fetching courses for the student
    private ObservableList<Course> getCoursesForStudent() {
        // This should be replaced with actual data retrieval logic
        ObservableList<Course> courses = FXCollections.observableArrayList();
        // Add courses to the list...
        return courses;
    }

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

    public void loadProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/profileView.fxml"));
            Node profileView = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileView);
//            Node view = loader.load();
//            contentArea.getChildren().setAll(view);

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

    public void handleExit(ActionEvent actionEvent) {
       Platform.exit();
    }

    public void loadDashboard(ActionEvent actionEvent) {
        contentArea.getChildren().clear();
        showCoursesTable();
    }
}
