/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.database.CourseDao;
import kurssihallinta.database.KurssihallintaDao;
import kurssihallinta.database.RegistrationDao;
import kurssihallinta.database.StudentDao;

/**
 *
 * @author okkokuisma
 */
public class KurssihallintaService {
    KurssihallintaDao students;
    KurssihallintaDao courses;
    RegistrationDao registrations;
    
    public KurssihallintaService() {
        students = new StudentDao();
        courses = new CourseDao();
        registrations = new RegistrationDao();
    }
    
    public boolean addStudent(String firstName, String surname, String id, String address, String city, String zipCode, String country, String email) {
        Student student = new Student(firstName, surname, id, address, city, zipCode, country, email);
        try {
            students.add(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addCourse(Course course) {
        try {
            courses.add(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addRegistration(String courseName, String studentIdNum) {
        try {
            registrations.add(courseName, studentIdNum);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public ObservableList searchCourses(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            courseList = courses.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return courseList;
    }
    
    public ObservableList searchStudents(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            studentList = students.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return studentList;
    }
    
    public ObservableList searchRegistrationsByStudentId(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            courseList = registrations.searchRegistrationsByStudents(searchWord);
        } catch (SQLException ex) { 
        }
        
        return courseList;
    }
    
    public ObservableList searchRegistrationsByCourseName(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            studentList = registrations.searchRegistrationsByCourses(searchWord);
        } catch (SQLException ex) { 
        }
        
        return studentList;
    }
    
    public boolean updateCourse(Course course) {
        try {
            courses.update(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean updateStudent(Student student) {
        try {
            students.update(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
}
