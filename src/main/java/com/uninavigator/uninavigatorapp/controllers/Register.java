package com.uninavigator.uninavigatorapp.controllers;
import DBConnection.DBHandler;
import com.uninavigator.uninavigatorapp.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class Register {

    @FXML private TextField plainTextField;
    @FXML private CheckBox showPasswordCheckbox;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker dobDatePicker;

    private UserService userService = new UserService(DBHandler.getInstance());


    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate dob = dobDatePicker.getValue();

        // Assume 'role' is determined or chosen in your form. Example provided:
        String role = "Student";

        boolean success = userService.createUser(username, password, email, firstName, lastName, role, dob);

        if (success) {
            try{
                FXMLLoader loader =  new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            }catch(IOException e){
                e.printStackTrace();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error with your registration. Please try again.");
            alert.showAndWait();
        }
    }

    public void handleLoginAction(ActionEvent actionEvent) {
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }catch(IOException e){
            e.printStackTrace();

        }
    }




    @FXML
    public void initialize() {
        // Bind properties of plainTextField to the inverse of the CheckBox's selected property
        plainTextField.managedProperty().bind(showPasswordCheckbox.selectedProperty());
        plainTextField.visibleProperty().bind(showPasswordCheckbox.selectedProperty());

        // Bind properties of passwordField to the CheckBox's selected property
        passwordField.managedProperty().bind(showPasswordCheckbox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckbox.selectedProperty().not());

        // Synchronize the text in both fields
        plainTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }

//    public String hashPassword(String plainTextPassword){
//        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
//    }
//
//    // Verify a password
//    public boolean checkPass(String plainPassword, String hashedPassword) {
//        return BCrypt.checkpw(plainPassword, hashedPassword);
//    }
}
