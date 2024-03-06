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

public class Register {
    @FXML
    private PasswordField passwordField;
    @FXML private TextField plainTextField;
    @FXML private CheckBox showPasswordCheckbox;
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
}
