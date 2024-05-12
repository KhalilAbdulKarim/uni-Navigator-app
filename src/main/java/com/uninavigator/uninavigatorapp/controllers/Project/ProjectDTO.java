package com.uninavigator.uninavigatorapp.controllers.Project;

public class ProjectDTO {
    private String projectName;
    private String description;
    private String instructorName;

    public ProjectDTO(String projectName, String description, String instructorName) {
        this.projectName = projectName;
        this.description = description;
        this.instructorName = instructorName;
    }

    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
}
