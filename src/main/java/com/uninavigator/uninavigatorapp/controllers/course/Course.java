package com.uninavigator.uninavigatorapp.controllers.course;
import java.util.Date;

/**
 * The Course class represents a course entity in the system. It encapsulates all the
 * details related to a course, such as its ID, name, schedule, description, capacity,
 * start date, end date, and the name of the instructor.
 */
public class Course {
    private final int courseId;
    private final String courseName;
    private final String schedule;
    private final String description;
    private final int capacity;
    private final Date startDate;
    private final Date endDate;
    private final String instructorName;

    /**
     * Constructs a Course with the specified details.
     *
     * @param courseId        The ID of the course.
     * @param courseName      The name of the course.
     * @param instructorName  The name of the instructor teaching the course.
     * @param schedule        The schedule of the course.
     * @param description     The description of the course.
     * @param capacity        The maximum number of students that can enroll in the course.
     * @param startDate       The start date of the course.
     * @param endDate         The end date of the course.
     */

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

    // Getter methods for each property.

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

