/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ogkuisma
 */
public class Lesson {
    private Course course;
    private String classroom;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String courseName;

    public Lesson(Course course, String classroom, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.course = course;
        this.classroom = classroom;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        courseName = course.getName();
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
}
