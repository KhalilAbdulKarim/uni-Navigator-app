package com.uninavigator.uninavigatorapp.controllers.Project;

import com.uninavigator.uninavigatorapp.controllers.user.User;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;
import java.util.Set;

public class Project {
    private LongProperty projectId = new SimpleLongProperty();
    private StringProperty projectName = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private Set<User> users = new HashSet<>();

    // Constructors
    public Project() {
    }

    public Project(long projectId, String projectName, String description) {
        this.projectId.set(projectId);
        this.projectName.set(projectName);
        this.description.set(description);
    }


    // Getters and Setters
    public long getProjectId() {
        return projectId.get();
    }

    public LongProperty projectIdProperty() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId.set(projectId);
    }

    public String getProjectName() {
        return projectName.get();
    }

    public StringProperty projectNameProperty() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName.set(projectName);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
