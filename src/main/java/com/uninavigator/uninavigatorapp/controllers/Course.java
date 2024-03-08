package com.uninavigator.uninavigatorapp.controllers;
import java.util.Date;

public class Course {
    private final int CourseID;
    private final String CourseName;

    private final int InstructorID;

    private final String Schedule;

    private final String Description;

    private final int Capacity;

    private final Date StartDate;

    private final Date EndDate;


    public Course(int courseID, String courseName, int instructorID, String schedule, String description, int capacity, Date startDate, Date endDate) {
        this.CourseID = courseID;
        this.CourseName = courseName;
        this.InstructorID = instructorID;
        this.Schedule = schedule;
        this.Description = description;
        this.Capacity = capacity;
        this.StartDate = startDate;
        this.EndDate = endDate;
    }

    public int getCourseID() {
        return CourseID;
    }

    public String getCourseName() {
        return CourseName;
    }

    public int getInstructorID() {
        return InstructorID;
    }

    public String getSchedule() {
        return Schedule;
    }

    public String getDescription() {
        return Description;
    }

    public int getCapacity() {
        return Capacity;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public Date getEndDate() {
        return EndDate;
    }


}

