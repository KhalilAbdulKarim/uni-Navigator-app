package com.uninavigator.uninavigatorapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentDashboard {
    @FXML
    private StackPane contentArea;
    @FXML
    public void loadCourseEnrollment(ActionEvent actionEvent) {
        try {
            Node profileManagement = FXMLLoader.load(getClass().getResource("/com/uninavigator/uninavigatorapp/courseEnrollement.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileManagement);


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void loadProfileManagement(ActionEvent actionEvent) {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/ProfileManagement.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/ProfileManagement.fxml"));
            Parent root = loader.load();

            // Instead of casting the event source, use a node that you know is part of the scene
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(root);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void loadAcademicRecords(ActionEvent actionEvent) {
    }

    public void handleExit(ActionEvent actionEvent) {
    }
}
