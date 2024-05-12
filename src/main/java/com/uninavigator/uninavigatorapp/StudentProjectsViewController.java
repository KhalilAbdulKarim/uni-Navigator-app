package com.uninavigator.uninavigatorapp;

import com.uninavigator.uninavigatorapp.ApiServices.ProjectService;
import com.uninavigator.uninavigatorapp.controllers.Project.ProjectDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.SessionContext;

import java.io.IOException;
import java.util.List;

public class StudentProjectsViewController {
    @FXML
    private TableView<ProjectDTO> projectsTable;
    @FXML
    private TableColumn<ProjectDTO, String> projectNameColumn;
    @FXML
    private TableColumn<ProjectDTO, String> descriptionColumn;
    @FXML
    private TableColumn<ProjectDTO, String> instructorNameColumn;

    public void initialize() {
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        instructorNameColumn.setCellValueFactory(new PropertyValueFactory<>("instructorName"));

        loadProjects();
    }

    private void loadProjects() {
        ProjectService projectService = new ProjectService();
        List<ProjectDTO> projects = projectService.getProjectsForStudent(SessionContext.getCurrentUser().getId());
        ObservableList<ProjectDTO> projectData = FXCollections.observableArrayList(projects);
        projectsTable.setItems(projectData);
    }

    public void handleBackButton(ActionEvent actionEvent) {
        switchScene(actionEvent, "/com/uninavigator/uninavigatorapp/StudentDashboard.fxml", "Dashboard");

    }

    private void switchScene(ActionEvent actionEvent, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to " + title);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
