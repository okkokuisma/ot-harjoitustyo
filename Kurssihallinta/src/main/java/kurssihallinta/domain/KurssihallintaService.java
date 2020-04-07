/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.sql.SQLException;
import java.time.LocalDate;
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
    
    public boolean addCourse(String courseName, LocalDate startDate, LocalDate endDate, String teacher, int maxStudents) {
        Course course = new Course(courseName, startDate, endDate, teacher, maxStudents);
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
    
    public ObservableList searchRegistrations(String searchWord) {
        ObservableList<Student> courseList = FXCollections.observableArrayList();
        try {
            courseList = registrations.search(searchWord);
        } catch (SQLException ex) { 
            System.out.println("virhe");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        return courseList;
    }
}
