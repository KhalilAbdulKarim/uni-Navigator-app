package com.uninavigator.uninavigatorapp.controllers;
import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import utils.StageHandler;
import javafx.scene.control.Alert;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller class for the registration view.
 * Handles user registration functionality including form validation and user creation.
 */

public class Register {

    @FXML private TextField plainTextField;
    @FXML private CheckBox showPasswordCheckbox;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker dobDatePicker;

    private StageHandler stageHandler;
    private UserService userService;



    /**
     * Handles the register button action. Validates form fields, creates a new user account,
     * and navigates to the login view upon successful registration.
     * @param actionEvent The event that triggered the action.
     */

    @FXML
    private void handleRegisterButtonAction(ActionEvent actionEvent) throws Exception {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate dob = dobDatePicker.getValue();

        // Validate registration fields
        if (!validateRegistrationFields(username, email, password, firstName, lastName, dob)) {
            return;
        }
        String role = "Student";
        String requestStatus = "None";


        boolean success = userService.createUser(username,password,email,firstName,lastName,role,dob,requestStatus);

        if (success) {
            // Registration successful, navigate to the login view
            try {
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                if (stageHandler == null) {
                    stageHandler = new StageHandler(currentStage);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText(null);
                    alert.setContentText("You have registered successfully");
                    alert.showAndWait();
                    stageHandler.switchScene("/com/uninavigator/uninavigatorapp/login.fxml", "");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            // Registration failed, show error message
            showAlert("Registration Error", "There was an error with your registration. Please try again.");
        }
    }

    /**
     * After the user logs in successfully , will be redirected to Dashboard
     * @param actionEvent The event that triggered the action.
     */

    public void handleLoginAction(ActionEvent actionEvent) {
        try {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            if (stageHandler == null) {
                stageHandler = new StageHandler(currentStage);
                stageHandler.switchScene("/com/uninavigator/uninavigatorapp/login.fxml", "uni-Navigator");
            }
        }
        catch(Exception e){
            e.printStackTrace();

        }
    }


    /**
     * Initializes the controller. Sets up the password visibility toggle.
     */

    @FXML
    public void initialize() {
        userService = new UserService();
        // Bind properties of plainTextField to the inverse of the CheckBox's selected property
        plainTextField.managedProperty().bind(showPasswordCheckbox.selectedProperty());
        plainTextField.visibleProperty().bind(showPasswordCheckbox.selectedProperty());

        // Bind properties of passwordField to the CheckBox's selected property
        passwordField.managedProperty().bind(showPasswordCheckbox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckbox.selectedProperty().not());

        // Synchronize the text in both fields
        plainTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    /**
     * Validates the registration fields. Checks for empty fields and pattern matching for email, password, and names.
     * @return true if validation succeeds, false otherwise.
     */

    public boolean validateRegistrationFields(String username, String email, String password, String firstName, String lastName, LocalDate dob) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob == null) {
            showAlert("Missing Information", "Please fill in all fields.");
            return false;
        }

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String namePattern = "^[a-zA-Z\\s]+";

        if (!email.matches(emailPattern)) {
            showAlert("Invalid Email", "Email does not match the required format.");
            return false;
        }
        if (!password.matches(passwordPattern)) {
            showAlert("Weak Password", "Password must be at least 8 characters long and include a number, a symbol, an uppercase and a lowercase letter.");
            return false;
        }
        if (!firstName.matches(namePattern) || !lastName.matches(namePattern)) {
            showAlert("Invalid Name", "First name and last name must only contain letters.");
            return false;
        }

        return true;
    }

    /**
     * Displays an alert dialog with a specified title and content.
     * @param title The title of the alert dialog.
     * @param content The message content of the alert dialog.
     * @param alertType The type of alert (e.g., ERROR, INFORMATION).
     */

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
