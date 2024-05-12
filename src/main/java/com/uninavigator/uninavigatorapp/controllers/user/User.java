package com.uninavigator.uninavigatorapp.controllers.user;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a User entity with properties for user attributes.
 * Utilizes JavaFX properties for easy binding and observation.
 */



public class User {

    private int userId;

    private final SimpleStringProperty username;
    private final SimpleStringProperty email;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty role;
    private final SimpleStringProperty dob;

    /**
     * Constructs a new User with specified attributes.
     *
     * @param userId The user's ID.
     * @param username The user's username.
     * @param email The user's email address.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param role The user's role (e.g., Student, Instructor).
     * @param dob The user's date of birth as a string.
     */

    public User(int userId, String username, String email, String firstName, String lastName, String role, String dob) {
        this.userId = userId;
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.dob = new SimpleStringProperty(dob);
    }



    /**
     *  Getters and setters for each property are provided below.
      */

    public Integer getUserId() {
        return userId;
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
        return new SimpleIntegerProperty(userId);
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
        this.userId = userId;
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

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public int getId() {
        return userId;
    }
}