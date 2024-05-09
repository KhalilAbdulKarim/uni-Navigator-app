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
import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import org.json.JSONObject;
import utils.SessionContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The InstructorRequests controller handles the UI logic for the instructor requests view.
 * It displays a table of instructor requests with options to accept or decline each request.
 * This controller is responsible for populating the table with data, handling user interactions,
 * and navigating between views.
 */
public class InstructorRequests {
    @FXML
    public Button refreshButton;
    public Button myProfile;
    public AnchorPane contentArea;
    public Button usersTable;
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

    public InstructorRequests() {
        this.userService = new UserService(); // Ensure UserService is properly initialized to handle requests
    }


    /**
     * Populates the requests table with instructor requests from the database.
     */

    private void populateRequestsTable() {
        try {
            // Fetch the raw JSON data from the service
            List<JSONObject> jsonRequests = userService.getAllInstructorRequests();
            // Convert the raw JSON data to UserRequestModel objects
            List<UserRequestModel> requests = convertJsonToModel(jsonRequests);
            // Wrap the list in an observable list and set it in the table view
            ObservableList<UserRequestModel> requestData = FXCollections.observableArrayList(requests);
            requestsTable.setItems(requestData);
        } catch (Exception e) {
            showAlert("Error", "Failed to load instructor requests: " + e.getMessage());
        }
    }
    private List<UserRequestModel> convertJsonToModel(List<JSONObject> jsonRequests) {
        List<UserRequestModel> models = new ArrayList<>();
        for (JSONObject json : jsonRequests) {
            UserRequestModel model = new UserRequestModel(
                    json.getInt("userId"),
                    json.getString("username"),
                    json.getString("email"),
                    json.getString("firstName"),
                    json.getString("lastName"),
                    LocalDate.parse(json.getString("dob"))
            );
            models.add(model);
        }
        return models;
    }

    /**
     * Sets up the columns in the requests table, including the action column with accept and decline buttons.
     */

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

    private void handleDecline(UserRequestModel request) {
        if (!isUserAuthorized("Admin")) {
            showAlert("Authorization Error", "You are not authorized to perform this action.");
            return;
        }
        try {
            if (userService.declineInstructorRequest(request.getUserId())) {
                populateRequestsTable(); // Refresh the table
                showAlert("Success", "Instructor request declined successfully.");
            } else {
                showAlert("Error", "Failed to decline the request.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to decline the request: " + e.getMessage());
        }
    }

    private void handleAccept(UserRequestModel request) {
        if (!isUserAuthorized("Admin")) {
            showAlert("Authorization Error", "You are not authorized to perform this action.");
            return;
        }
        try {
            if (userService.approveInstructorRequest(request.getUserId())) {
                populateRequestsTable(); // Refresh the table
                showAlert("Success", "Instructor request approved successfully.");
            } else {
                showAlert("Error", "Failed to approve the request.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to approve the request: " + e.getMessage());
        }
    }

    public void handleRefreshAction(ActionEvent actionEvent) {
        populateRequestsTable();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It sets up the table columns and
     * populates the table with data.
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        populateRequestsTable();
    }

    /**
     *
     * @param requiredRole role of the user
     * @return true of user is eligible , false otherwise
     */
    private boolean isUserAuthorized(String requiredRole) {
        String currentUserRole = SessionContext.getCurrentUserRole();
        return currentUserRole.equals(requiredRole) || currentUserRole.equals("Admin");
    }

    /**
     *
     * @param title alert title
     * @param content type of error
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param actionEvent
     * it navigates to user's profile
     */
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

    /**
     *
     * @param actionEvent
     * it navigates to users table
     */
    public void loadUsersTable(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/userTable.fxml"));
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
