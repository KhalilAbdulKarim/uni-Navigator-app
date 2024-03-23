package com.uninavigator.uninavigatorapp.controllers;

import DBConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import com.uninavigator.uninavigatorapp.UserRequestModel;
import com.uninavigator.uninavigatorapp.services.UserService;
import utils.SessionContext;

import java.io.IOException;
import java.util.List;

public class InstructorRequests {
    @FXML
    public Button refreshButton;
    public Button myProfile;
    public AnchorPane contentArea;
    @FXML
    private TableColumn<UserRequestModel, Number> userIdColumn;
    @FXML
    private TableColumn<UserRequestModel, String> usernameColumn;
    @FXML
    private TableColumn<UserRequestModel, String> emailColumn;
    @FXML
    private TableColumn<UserRequestModel, String> firstNameColumn;
    @FXML
    private TableColumn<UserRequestModel, String> lastNameColumn;
    @FXML
    private TableColumn<UserRequestModel, String> dobColumn;
    @FXML
    private TableColumn<UserRequestModel, Void> actionColumn;
    @FXML
    private TableView<UserRequestModel> requestsTable;
    private UserService userService;


    private void populateRequestsTable() {
        List<UserRequestModel> requests = userService.getAllInstructorRequests();
        ObservableList<UserRequestModel> requestData = FXCollections.observableArrayList(requests);
        requestsTable.setItems(requestData);
    }

    private void setupTableColumns() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");
            private final Button declineButton = new Button("Decline");
            private final HBox hbox = new HBox(acceptButton, declineButton);

            {
                hbox.setSpacing(10);
                acceptButton.setOnAction(event -> handleAccept(getTableView().getItems().get(getIndex())));
                declineButton.setOnAction(event -> handleDecline(getTableView().getItems().get(getIndex())));

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }

    private void handleDecline(UserRequestModel userRequestModel) {
        if (!isUserAuthorized("Admin")) {
            showAlert("Authorization Error", "You are not authorized to perform this action.");
            return;
        }
        boolean success = userService.declineInstructorRequest(userRequestModel.getUserId());
        if (success) {
            populateRequestsTable(); // Refresh the table data
        }
    }

    private void handleAccept(UserRequestModel userRequestModel) {
        if (!isUserAuthorized("Admin")) {
            showAlert("Authorization Error", "You are not authorized to perform this action.");
            return;
        }
        boolean success = userService.approveInstructorRequest(userRequestModel.getUserId());
        if (success) {
            populateRequestsTable();
        }
    }

    public void handleRefreshAction(ActionEvent actionEvent) {
        populateRequestsTable();
    }

    @FXML
    public void initialize() {
        userService = new UserService(DBHandler.getInstance());
        setupTableColumns();
        populateRequestsTable();
    }

    private boolean isUserAuthorized(String requiredRole) {
        String currentUserRole = SessionContext.getCurrentUserRole();
        return currentUserRole.equals(requiredRole) || currentUserRole.equals("Admin");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void loadProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/profileView.fxml"));
            Node profileView = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileView);
//            Node view = loader.load();
//            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
