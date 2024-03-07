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
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.StageHandler;

import java.io.IOException;

public class Login {
    @FXML
    private TextField usernameTextField;

    @FXML
    private CheckBox showPasswordCheckbox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField plainTextField;

    private StageHandler stageHandler;


    private UserService userService = new UserService(DBHandler.getInstance());

    public void handleLoginAction(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Username & Password cannot be empty");
            return;
        }
//        System.out.println("Before Attempting to authenticate user: " + username + " with password: " + password);

        User user = userService.authenticateUser(username, password);

//        System.out.println("After Attempting to authenticate user: " + username + " with password: " + password);

        if (user != null) {
            navigateToDashboard(user.getRole(), actionEvent);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }

    }

    public void handleRegisterAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);


        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    @FXML
    public void initialize() {
        // Sync the password visibility with the checkbox
        showPasswordCheckbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                // Show plainTextField and copy passwordField's content into it
                plainTextField.setText(passwordField.getText());
                plainTextField.setManaged(true);
                plainTextField.setVisible(true);

                passwordField.setManaged(false);
                passwordField.setVisible(false);
            } else {
                // Hide plainTextField and copy its content back to passwordField
                passwordField.setText(plainTextField.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);

                plainTextField.setManaged(false);
                plainTextField.setVisible(false);
            }
        });
        plainTextField.textProperty().bindBidirectional(passwordField.textProperty());

    }

    private void navigateToDashboard(String role, ActionEvent actionEvent) {
//        String fxmlFile = "";
//        switch (role) {
//            //To be changed
//            case "Student":
//                fxmlFile = "userTable.fxml";
//                break;
//            //To be changed
//            case "Instructor":
//                fxmlFile = "userTable.fxml";
//                break;
//            // Data List only for admins
//            case "Admin":
//                fxmlFile = "userTable.fxml";
//                break;
//        }

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stageHandler =  new StageHandler(currentStage);
        stageHandler.switchScene("/com/uninavigator/uninavigatorapp/userTable.fxml","usrTable");


    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
