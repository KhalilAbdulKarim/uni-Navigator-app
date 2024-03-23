package com.uninavigator.uninavigatorapp;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

import java.time.LocalDate;

public class UserRequestModel {
    private final SimpleIntegerProperty userId;
    private final SimpleStringProperty username;
    private final SimpleStringProperty email;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleObjectProperty<LocalDate> dob;

    public UserRequestModel(int userId, String username, String email, String firstName, String lastName, LocalDate dob) {
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.dob = new SimpleObjectProperty<>(dob);
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public LocalDate getDob() {
        return dob.get();
    }

    public SimpleObjectProperty<LocalDate> dobProperty() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob.set(dob);
    }



}


