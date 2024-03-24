package com.uninavigator.uninavigatorapp.controllers;
import java.util.Date;

public class Course {
    private final int courseId;
    private final String courseName;
    private final String schedule;
    private final String description;
    private final int capacity;
    private final Date startDate;
    private final Date endDate;
    private final String instructorName;

    public Course(int courseId, String courseName,String instructorName, String schedule, String description, int capacity, Date startDate, Date endDate) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.schedule = schedule;
        this.description = description;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

