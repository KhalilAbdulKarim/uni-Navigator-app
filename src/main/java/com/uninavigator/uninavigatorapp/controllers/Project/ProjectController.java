package com.uninavigator.uninavigatorapp.controllers.Project;

import com.uninavigator.uninavigatorapp.ApiServices.ProjectService;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.SessionContext;

import java.io.IOException;
import java.util.List;

public class ProjectController {
    @FXML
    private TableView<Project> projectsTable;
    @FXML
    private TableColumn<Project, String> projectNameColumn;
    @FXML
    private TableColumn<Project, String> descriptionColumn;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Button saveButton, cancelButton, addUserButton;
    @FXML
    private ComboBox<User> studentComboBox;
    @FXML
    private VBox formContainer;

    private final ProjectService projectService = new ProjectService();

    @FXML
    private void initialize() {
        projectNameColumn.setCellValueFactory(cellData -> cellData.getValue().projectNameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        loadProjects();
        loadStudents();
    }

    private void loadProjects() {
        ObservableList<Project> projectData = FXCollections.observableArrayList(projectService.getAllProjects());
        projectsTable.setItems(projectData);
    }

    private void loadStudents() {
        List<User> students = projectService.getAllStudents();
        ObservableList<User> studentData = FXCollections.observableArrayList(students);
        studentComboBox.setItems(studentData);
        studentComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user == null ? null : user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public User fromString(String string) {
                return studentComboBox.getItems().stream()
                        .filter(user -> (user.getUsername().equals(string)))
                        .findFirst().orElse(null);
            }
        });
    }

    @FXML
    private void handleSaveAction() {
        Project project = new Project();
        project.setProjectName(projectNameField.getText());
        project.setDescription(descriptionField.getText());

        if (projectService.saveOrUpdateProject(project)) {
            showAlert("Success", "Project saved successfully.");
            loadProjects();
        } else {
            showAlert("Error", "Failed to save the project.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddUserToProjectAction() {
        User selectedUser = studentComboBox.getValue();
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null || selectedProject == null) {
            showAlert("Error", "Please select a student and a project.");
            return;
        }

        if (projectService.addUserToProject(selectedProject.getProjectId(), selectedUser.getUserId())) {
            showAlert("Success", "Student added to project successfully.");
        } else {
            showAlert("Error", "Failed to add student to project.");
        }
    }


    @FXML
    private void handleCancelAction(ActionEvent actionEvent){
        projectNameField.clear();
        descriptionField.clear();
        switchScene(actionEvent,"/com/uninavigator/uninavigatorapp/studentDashboard.fxml", "Dashboard");
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
}
