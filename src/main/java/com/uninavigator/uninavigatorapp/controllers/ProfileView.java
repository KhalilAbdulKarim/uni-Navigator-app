package com.uninavigator.uninavigatorapp.controllers;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.services.UserService;
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
import utils.StageHandler;

import java.io.IOException;

/**
 * Controller for the profile view.
 * Displays user profile information and provides navigation options.
 */

public class ProfileView {

    @FXML
    private Button instructorRequestButton;
    public Button editProfileButton;
    public Label dobLabel;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label usernameLabel;
    public Label roleLabel;
    public Button backButton;
    private StageHandler stageHandler;
    private DBHandler dbHandler;
    private UserService userService = new UserService(dbHandler);

    /**
     * Handles the action to edit the current user's profile.
     * @param actionEvent The event that triggered the action.
     */

    public void handleEditAction(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (stageHandler == null) {
            stageHandler = new StageHandler(currentStage);
        }
        try {
            stageHandler.switchScene("/com/uninavigator/uninavigatorapp/ProfileManagement.fxml", "Edit Profile");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to Edit Profile.");
        }
    }

    /**
     * Handles the action to return to the dashboard.
     * @param actionEvent The event that triggered the action.
     */

    public void handleBackAction(ActionEvent actionEvent) {
        try
             {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/studentDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Dashboard");
        Scene scene = new Scene(root);
        stage.setScene(scene);
            } catch (IOException e){
            e.printStackTrace();
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/instructorRequest.fxml"));
            Parent instructorRequestsView = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(instructorRequestsView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
