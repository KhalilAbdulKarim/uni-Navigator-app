package com.uninavigator.uninavigatorapp.controllers.user;

import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.SessionContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the profile view.
 * Displays user profile information and provides navigation options.
 */

public class ProfileView {

    @FXML
    private Button instructorRequestButton;

    @FXML
    private Button createCourseButton;
    public Button editProfileButton;
    public Label dobLabel;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label usernameLabel;
    public Label roleLabel;
    public Button backButton;

    private UserService userService;

    public ProfileView() {
        this.userService = new UserService();
    }

    /**
     * Handles the action to edit the current user's profile.
     * @param actionEvent The event that triggered the action.
     */

    public void handleEditAction(ActionEvent actionEvent) {
        switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/ProfileManagement.fxml", "Edit Profile");
    }

    /**
     * Handles the action to return to the dashboard.
     * @param actionEvent The event that triggered the action.
     */

    public void handleBackAction(ActionEvent actionEvent) {
        switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/studentDashboard.fxml", "Dashboard");
    }

    /**
     * Displays an alert dialog.
     * @param title The title of the alert.
     * @param content The message content of the alert.
     */

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the controller.
     * Sets visibility of certain UI elements based on user role and loads user profile data.
     */

    @FXML
    public void initialize() {
        User currentUser = SessionContext.getCurrentUser();

        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            instructorRequestButton.setVisible(true);
            createCourseButton.setVisible(true);

        }

        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("Email: " + currentUser.getEmail());
            firstNameLabel.setText("First Name: " + currentUser.getFirstName());
            lastNameLabel.setText("Last Name: " + currentUser.getLastName());
            dobLabel.setText("Date of Birth: " + currentUser.getDob());
            roleLabel.setText("Role: " + currentUser.getRole());
        } else {
            showAlert("Session Error", "No user is currently logged in.");

        }


    }

    /**
     * Handles the action to view instructor requests (Admin role only).
     * @param actionEvent The event that triggered the action.
     */

    public void InstructorButtonHandling(ActionEvent actionEvent) {
        if ("Admin".equals(SessionContext.getCurrentUser().getRole())) {
            switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/instructorRequest.fxml", "Instructor Requests");
        } else {
            showAlert("Access Denied", "You are not authorized to view this page.");
        }
    }

    /**
     * Switches the scene to a new FXML view.
     * @param actionEvent The event that triggered the action.
     * @param fxmlPath The path to the FXML file.
     * @param title The title of the new scene.
     */
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

    private void populateUserProfile() {
        var currentUser = SessionContext.getCurrentUser();

        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("Email: " + currentUser.getEmail());
            firstNameLabel.setText("First Name: " + currentUser.getFirstName());
            lastNameLabel.setText("Last Name: " + currentUser.getLastName());
//            dobLabel.setText("Date of Birth: " + currentUser.getDob());
            LocalDate dob = LocalDate.parse(currentUser.getDob(), DateTimeFormatter.ISO_LOCAL_DATE);
            dobLabel.setText("Date of Birth: " + dob.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
            roleLabel.setText("Role: " + currentUser.getRole());

            // Only show the instructor request button for Admin users
            instructorRequestButton.setVisible("Admin".equals(currentUser.getRole()));
        } else {
            showAlert("Session Error", "No user is currently logged in.");
        }
    }

    public void createCourseButtonHandling(ActionEvent actionEvent) {
        if ("Admin".equals(SessionContext.getCurrentUser().getRole())) {
            switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/courseManagement.fxml", "Course Management");
        } else {
            showAlert("Access Denied", "You are not authorized to view this page.");
        }
    }
}
