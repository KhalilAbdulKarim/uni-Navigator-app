package com.uninavigator.uninavigatorapp.controllers.user;
import com.uninavigator.uninavigatorapp.ApiServices.UserService;
import com.uninavigator.uninavigatorapp.controllers.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


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

    private UserService userService;

    public UserTable() {
        this.userService = new UserService();
    }

    /**
     * Initializes the UserTable controller.
     * Sets up the table columns and loads the user data into the table.
     */

    @FXML
    private void initialize() throws Exception {
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

    private void loadUserData() throws Exception {
        ObservableList<User> userData = FXCollections.observableArrayList();
        List<JSONObject> usersJson = userService.getAllUsers();
        for (JSONObject userJson : usersJson) {
            User user = new User(
                    userJson.getInt("userId"),
                    userJson.getString("username"),
                    userJson.getString("email"),
                    userJson.getString("firstName"),
                    userJson.getString("lastName"),
                    userJson.getString("role"),
                    userJson.getString("dob")
            );
            userData.add(user);
        }
          userTable.setItems(userData);
    }

    /**
     * Handles the event to load the instructor request list view.
     * Clears the current content area and loads the view from a FXML file.
     *
     * @param actionEvent The action event triggered by the user's interaction.
     */

    public void loadInstructorsRequestList(ActionEvent actionEvent) {
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


