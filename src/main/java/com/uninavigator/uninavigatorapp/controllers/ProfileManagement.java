package com.uninavigator.uninavigatorapp.controllers;

import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.SessionContext;
import utils.StageHandler;

import java.time.LocalDate;

/**
 * Controller for managing user profile updates.
 * Allows users to edit and save their profile information.
 */

public class ProfileManagement {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField plainTextField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private CheckBox showPasswordCheckbox;
    private StageHandler stageHandler;

    private UserService userService;

    /**
     * Constructor initializes the UserService with a database handler instance.
     */

    public ProfileManagement() {
        this.userService = new UserService(DBHandler.getInstance());
    }

    /**
     * Saves the edited user profile information to the database.
     * Validates the fields before saving and updates the SessionContext with the new user details.
     * @param actionEvent The action event that triggered this method.
     */

    public void handleSaveAction(ActionEvent actionEvent) {
        // Extract and trim input from form fields
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        LocalDate dob = dobDatePicker.getValue();

        // Retrieve current user details
        User currentUser = SessionContext.getCurrentUser();
        int currentUserId = currentUser.getUserId();
        String originalUsername = currentUser.getUsername();

        // Check if the username is changed to a new one that already exists in the database
        if (!originalUsername.equalsIgnoreCase(username) && userService.doesUsernameExist(username)) {
            showAlert("Username Exists", "This username is already taken. Please choose another one.","");
            return;
        }

        // Validate registration fields; if validation fails, exit the method
        if (!validateRegistrationFields(username, email, password, firstName, lastName, dob)) {
            return;
        }

        // Update user information in the database
        boolean success = userService.updateUser(currentUserId, username, email, password, firstName, lastName, dob);

        if(success) {
            showAlert("Update Success", "Profile updated successfully.","CONFIRMATION");
            User updatedUser = new User(currentUserId, username, email, firstName, lastName,SessionContext.getCurrentUser().getRole(), dob.toString());
            SessionContext.setCurrentUser(updatedUser);
            stageHandler.switchScene("/com/uninavigator/uninavigatorapp/profileView.fxml","Profile");

        } else {
            showAlert("Update Failure", "Failed to update profile.","");
        }
    }

    /**
     * Cancels the profile editing and returns to the profile view.
     * @param actionEvent The action event that triggered this method.
     */

    public void handleCancelAction(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if(stageHandler == null) {
            stageHandler = new StageHandler(currentStage);
        }
        try {
            stageHandler.switchScene("/com/uninavigator/uninavigatorapp/profileView.fxml", "Profile");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to cancel","");
        }
    }

    /**
     *
     * @param title alert title
     * @param content content of alert
     * @param type type of alert
     */
    private void showAlert(String title, String content, String type) {
        Alert.AlertType alertType = "CONFIRMATION".equals(type) ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param dob
     * @return true if fields are truely set , false otherwise
     */
    public boolean validateRegistrationFields(String username, String email, String password, String firstName, String lastName, LocalDate dob) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob == null) {
            showAlert("Missing Information", "Please fill in all fields.","");
            return false;
        }

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String namePattern = "^[a-zA-Z\\s]+";

        if (!email.matches(emailPattern)) {
            showAlert("Invalid Email", "Email does not match the required format.","");
            return false;
        }
        if (!password.matches(passwordPattern)) {
            showAlert("Weak Password", "Password must be at least 8 characters long and include a number, a symbol, an uppercase and a lowercase letter.","");
            return false;
        }
        if (!firstName.matches(namePattern) || !lastName.matches(namePattern)) {
            showAlert("Invalid Name", "First name and last name must only contain letters.","");
            return false;
        }

        return true;
    }


    /**
     * Initializes the controller. Sets up the password visibility toggle based on the checkbox.
     */

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

    /**
     * Sends a request to change the current user's status to instructor.
     * This method should be bound to an appropriate UI element if such functionality is desired.
     * @param actionEvent The action event that triggered this method.
     */

    @FXML
    public void handleVerifyAction(ActionEvent actionEvent) {
        User currentUser = SessionContext.getCurrentUser();
        userService.requestInstructorStatus(currentUser.getUserId());
        showAlert("Request Sent", "Your request to become an instructor has been sent to the admin.", "INFORMATION");
    }



}
