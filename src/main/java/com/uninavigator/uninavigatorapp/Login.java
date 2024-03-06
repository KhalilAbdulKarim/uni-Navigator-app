package com.uninavigator.uninavigatorapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField plainTextField;
    @FXML
    private CheckBox showPasswordCheckbox;
    public void handleLoginAction(ActionEvent actionEvent) {
    }

    public void handleRegisterAction(ActionEvent actionEvent) {
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("register.fxml"));
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
        // Sync the password visibility with the checkbox
        showPasswordCheckbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                // Show the plain text field and hide the password field
                plainTextField.setText(passwordField.getText());
                plainTextField.setVisible(true);
                plainTextField.setManaged(true);

                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                // Show the password field and hide the plain text field
                passwordField.setText(plainTextField.getText());
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                plainTextField.setVisible(false);
                plainTextField.setManaged(false);
            }
        });

        // Bind the plain text field and password field text properties bidirectionally
        plainTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }

}
