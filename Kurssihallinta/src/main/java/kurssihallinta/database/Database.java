/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class Database {
    private Connection db;
    
    public void connect() {    
        try {
            db = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement s = db.createStatement();
            ResultSet queryResult = s.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table'");
            if (queryResult.getInt(1) > 0) {
                return;
            }
            
            s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER)");
            s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
            s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
            s.execute("CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER)");
            s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean addStudent(String firstName, String surname, String idNumber, String address, String zipCode, String city, String country, String email) throws SQLException { 
        PreparedStatement ps = db.prepareStatement("INSERT INTO Students (first_name,surname,id_number,address,zip,city,country,email) VALUES (?,?,?,?,?,?,?,?)");
        ps.setString(1, firstName);
        ps.setString(2, surname);
        ps.setString(3, idNumber);
        ps.setString(4, address);
        ps.setString(5, zipCode);
        ps.setString(6, city);
        ps.setString(7, country);
        ps.setString(8, email);
        ps.execute();
        return true;
    }
    
    public boolean addCourse(String name, String startDate, String endDate, String teacher, int maxStudents) throws SQLException {    
        PreparedStatement ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher,students, max_students) VALUES (?,?,?,?,?,?)");
        ps.setString(1, name);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.setString(4, teacher);
        ps.setInt(5, 0);
        ps.setInt(6, maxStudents);
        ps.execute();  
        return true;
    }
    
    public boolean addRegistration(int courseId) throws SQLException {
        Statement s = db.createStatement();
        ResultSet queryResults = s.executeQuery("SELECT id FROM Students ORDER BY id DESC LIMIT 1");
        int studentId = queryResults.getInt(1);
        
        PreparedStatement ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, studentId);
        ps.execute();  
        return true;
    }
    
    public ObservableList searchCourses(String searchWord) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE name LIKE '%" + searchWord + "%'");

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Course> courses = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate startDate = LocalDate.parse(queryResults.getString(3));
            LocalDate endDate = LocalDate.parse(queryResults.getString(4));
            Course course = new Course(queryResults.getInt(1), queryResults.getString(2), startDate, endDate, queryResults.getString(5), queryResults.getInt(7));
            courses.add(course);
        }
        
        return courses;
    }
    
    public ObservableList searchStudents(String searchWord) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE first_name LIKE '%" + searchWord + "%' OR surname LIKE '%" + searchWord + "%'");

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getInt(1),queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        
        return students;
    }
}
