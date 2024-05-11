package com.uninavigator.uninavigatorapp.controllers;
import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import com.uninavigator.uninavigatorapp.controllers.user.User;
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
import org.json.JSONException;
import utils.SessionContext;
import utils.StageHandler;

import javax.security.sasl.AuthenticationException;


import java.io.IOException;

/**
 * Controller for the login view.
 * Handles user authentication, password visibility toggle, and navigation based on user role.
 */

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

    private UserService userService;

    public Login() {
        this.userService = new UserService();
    }

    /**
     * Attempts to authenticate the user. If successful, navigates to the appropriate dashboard
     * based on the user's role. Shows an alert on authentication failure or error.
     * @param actionEvent The action event triggered by the login button.
     */

//    public void handleLoginAction(ActionEvent actionEvent) {
//        String username = usernameTextField.getText().trim();
//        String password = passwordField.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Login Error", "Username & Password cannot be empty");
//            return;
//        }
//
//        try {
//            User authenticatedUser = userService.authenticateUser(username, password);
//            if (authenticatedUser != null) {
//                SessionContext.clear();
//                SessionContext.setCurrentUser(authenticatedUser);//
//                navigate(authenticatedUser.getRole(), actionEvent);
//            } else {
//                showAlert("Login Error", "Invalid username or password. Please try again.");
//            }
//        } catch (Exception e) {
//            showAlert("Login Error", "An error occurred while attempting to log in. Please try again later.");
//            e.printStackTrace();
//        }
//    }

//    public void handleLoginAction(ActionEvent actionEvent) {
//        String username = usernameTextField.getText().trim();
//        String password = passwordField.isVisible() ? passwordField.getText() : plainTextField.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Login Error", "Username & Password cannot be empty");
//            return;
//        }
//
//        try {
//            User authenticatedUser = userService.authenticateUser(username, password);
//            if (authenticatedUser != null) {
//                SessionContext.clear();
//                SessionContext.setCurrentUser(authenticatedUser);
//                navigate(authenticatedUser.getRole(), actionEvent);
//            } else {
//                showAlert("Login Error", "Invalid username or password. Please try again.");
//            }
//        } catch (JSONException je) {
//            showAlert("Login Error", "Failed to parse server response: " + je.getMessage());
//        } catch (Exception e) {
//            showAlert("Login Error", "An error occurred while attempting to log in. Please try again later.");
//            e.printStackTrace();
//        }
//    }

    public void handleLoginAction(ActionEvent actionEvent) {
        String username = usernameTextField.getText().trim();
        String password = passwordField.isVisible() ? passwordField.getText() : plainTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Username and Password cannot be empty");
            return;
        }

        try {
            User authenticatedUser = userService.authenticateUser(username, password);
            SessionContext.clear();
            SessionContext.setCurrentUser(authenticatedUser);
            navigate(authenticatedUser.getRole(), actionEvent);
        } catch (AuthenticationException ae) {
            // This catch block handles incorrect credentials
            showAlert("Login Error", "Invalid username or password. Please try again.");
        } catch (JSONException je) {
            // This catch block handles JSON parsing errors
            showAlert("Login Error", "Failed to parse server response: " + je.getMessage());
        } catch (IOException ie) {
            // This handles network-related errors
            showAlert("Login Error", "Network error: Please check your connection and try again.");
        } catch (Exception e) {
            // Generic catch block for any other unexpected exceptions
            showAlert("Login Error", "An error occurred while attempting to log in. Please try again later.");
            e.printStackTrace();
        }
    }




    /**
     * Navigates to the registration view.
     * @param actionEvent The action event triggered by the register button.
     */

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

    /**
     * Navigates the user to their respective dashboard based on their role.
     * @param role The role of the authenticated user.
     * @param actionEvent The action event for context.
     */

    private void navigate(String role, ActionEvent actionEvent) {
        String fxmlFile = "";
        switch (role) {
            case "Student":
                fxmlFile = "/com/uninavigator/uninavigatorapp/studentDashboard.fxml";
                break;
            case "Instructor":
                fxmlFile = "/com/uninavigator/uninavigatorapp/studentDashboard.fxml";
                break;
            case "Admin":
                fxmlFile = "/com/uninavigator/uninavigatorapp/instructorRequest.fxml";
                break;
        }
        if (!isUserAuthorized(role)) {
            showAlert("Authorization Error", "You are not authorized to access this page.");
            return;
        }

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if(stageHandler == null) {
            stageHandler = new StageHandler(currentStage);
        }
        try {
            stageHandler.switchScene(fxmlFile, "uni-Navigator");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to the dashboard.");
        }
    }

    /**
     * Shows an alert dialog with the given title and content.
     * @param title The title for the alert dialog.
     * @param content The content/message for the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the controller. Sets up the password visibility toggle.
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
     * Checks if the current user has the required role for an action.
     * @param requiredRole The role required to perform the action.
     * @return true if the user has the required role or is an admin; false otherwise.
     */

    private boolean isUserAuthorized(String requiredRole) {
        String currentUserRole = SessionContext.getCurrentUserRole();
        return currentUserRole.equals(requiredRole) || currentUserRole.equals("Admin");
    }
}
