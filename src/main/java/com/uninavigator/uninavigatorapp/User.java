package com.uninavigator.uninavigatorapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleIntegerProperty userId;
    private final SimpleStringProperty username;
    private final SimpleStringProperty email;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty role;
    private final SimpleStringProperty dob;

    public User(int userId, String username, String email, String firstName, String lastName, String role, String dob) {
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.dob = new SimpleStringProperty(dob);
    }


    public int getUserId() {
        return userId.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getDob() {
        return dob.get();
    }


    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public SimpleStringProperty dobProperty() {
        return dob;
    }


    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }
}
