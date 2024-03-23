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

public class ProfileView {

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        User currentUser = SessionContext.getCurrentUser();

        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("Email: " + currentUser.getEmail());
            firstNameLabel.setText("First Name: " + currentUser.getFirstName());
            lastNameLabel.setText("Last Name: " + currentUser.getLastName());
            dobLabel.setText("Date of Birth: " + currentUser.getDob());
            roleLabel.setText("Role: " + currentUser.getRole());
        } else {
            showAlert("Session Error", "No user is currently logged in.");
            // Optionally navigate back to login screen or handle the error as needed
        }



    }


}
