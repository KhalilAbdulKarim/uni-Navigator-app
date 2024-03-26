package com.uninavigator.uninavigatorapp.controllers;
import DBConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Controller class for the UserTable view.
 * Manages the display and interaction with the table of users within the application.
 */

public class UserTable {
    @FXML private AnchorPane contentArea;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Number> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> dobColumn;

    /**
     * Initializes the UserTable controller.
     * Sets up the table columns and loads the user data into the table.
     */

    @FXML
    private void initialize() {
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        dobColumn.setCellValueFactory(cellData -> cellData.getValue().dobProperty());

        loadUserData();
    }

    /**
     * Loads user data from the database and adds it to the user table.
     * Queries the database for all users and constructs User objects to display in the table.
     */

    private void loadUserData() {
        ObservableList<User> userData = FXCollections.observableArrayList();
        String query = "SELECT UserID, Username, Email, FirstName, LastName, Role, DOB FROM User";

        try (Connection conn = DBHandler.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Role"),
                        rs.getString("DOB")
                );
                userData.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userTable.setItems(userData);
    }

    /**
     * Handles the event to load the instructor request list view.
     * Clears the current content area and loads the view from a FXML file.
     *
     * @param actionEvent The action event triggered by the user's interaction.
     */

    public void loadInstructorsList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uninavigator/uninavigatorapp/instructorRequest.fxml"));
            Node profileView = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(profileView);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}


