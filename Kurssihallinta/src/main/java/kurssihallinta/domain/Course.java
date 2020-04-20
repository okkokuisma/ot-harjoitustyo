/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author okkokuisma
 */
public class Course {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String teacher;
    private int students;
    private int maxStudents;
    
    public Course() {       
    }

    public Course(String name, LocalDate startDate, LocalDate endDate, String teacher, int students, int maxStudents) {
        this.students = students;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
        this.maxStudents = maxStudents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getStudents() {
        return students;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    
}
