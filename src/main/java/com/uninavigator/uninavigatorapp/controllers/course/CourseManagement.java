package com.uninavigator.uninavigatorapp.controllers.course;

import com.uninavigator.uninavigatorapp.ApiServices.CourseService;
import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.SessionContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseManagement {
    @FXML
    private TextField courseNameField;
    @FXML private ComboBox<String> instructorComboBox;
    @FXML private TextField scheduleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField capacityField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private UserService userService;
    private CourseService courseService;

    @FXML
    public void initialize() throws Exception {
        userService = new UserService();  // Initialize your user service
        courseService = new CourseService();  // Initialize your course service
        loadInstructors();

    }

    private void loadInstructors() {
        try {
            List<JSONObject> instructors = userService.getAllInstuctors();
            ObservableList<String> instructorNames = FXCollections.observableArrayList();

            for (JSONObject instructorObject : instructors) {
                String role = instructorObject.getString("role");
                if ("Instructor".equals(role)) {
                    // Only add the user to the dropdown if they are an instructor
                    String fullName = instructorObject.getString("firstName") + " " + instructorObject.getString("lastName");
                    instructorNames.add(fullName);
                }
            }

            instructorComboBox.setItems(instructorNames);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load instructors.");
        }
    }






    public void handleCreateCourseAction(ActionEvent actionEvent) {
        String courseName = courseNameField.getText();
        String schedule = scheduleField.getText();
        String description = descriptionField.getText();
        String capacityText = capacityField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String selectedName = instructorComboBox.getValue();

        if (!SessionContext.getCurrentUser().getRole().equals("Admin")) {
            showAlert("Access Denied", "You are not authorized to create courses.");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Capacity must be a valid integer.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            showAlert("Validation Error", "End date cannot be before start date.");
            return;
        }

        try {
            String[] names = selectedName.split(" ", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "";

            User instructor = userService.findInstructorByName(firstName, lastName);
            if (instructor == null || instructor.getUserId() == 0) {
                showAlert("Instructor Error", "Instructor not found or ID not set.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedStartDate = startDate.format(formatter);
            String formattedEndDate = endDate.format(formatter);

            boolean createdCourse = courseService.createCourse(courseName, instructor.getUserId(), schedule, description, capacity, formattedStartDate, formattedEndDate);

            if (createdCourse) {
                showAlert("Success", "Course created successfully.");
                clearForm(actionEvent);
            } else {
                showAlert("Failure", "Failed to create the course.");
            }
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }


    @FXML
    private void clearForm(ActionEvent actionEvent) {
        courseNameField.clear();
        scheduleField.clear();
        descriptionField.clear();
        capacityField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        instructorComboBox.setValue(null);

    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/profileView.fxml", "Profile");
    }

    private void switchScene(ActionEvent actionEvent, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to " + title);
        }
    }
    private boolean validateInputs() {
        String courseName = courseNameField.getText();
        String schedule = scheduleField.getText();
        String description = descriptionField.getText();
        String capacityText = capacityField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String selectedName = instructorComboBox.getValue();

        if (courseName.isEmpty() || schedule.isEmpty() || description.isEmpty() || capacityText.isEmpty() || startDate == null || endDate == null || selectedName == null) {
            showAlert("Validation Error", "All fields are required and must be properly filled out.");
            return false;
        }

        try {
            int capacity = Integer.parseInt(capacityText);
            if (capacity <= 0) {
                showAlert("Validation Error", "Capacity must be a positive integer.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Capacity must be a valid integer.");
            return false;
        }

        if (endDate.isBefore(startDate)) {
            showAlert("Validation Error", "End date cannot be before the start date.");
            return false;
        }

        return true;
    }


    }




