package com.uninavigator.uninavigatorapp;

import DBConnection.DBHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StartApp extends Application {

    // Start App point
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApp.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("uni-Navigator");
        stage.show();
    }

    public static void main(String[] args) {
        testConnection();
        launch();
    }

    private static void testConnection() {
        try {
            System.out.println("Testing database connection...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBHandler dbConn = new DBHandler();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

}