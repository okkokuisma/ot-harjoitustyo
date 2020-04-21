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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class RegistrationDao {
    
    public void add(String courseName, String studentIdNum) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Students WHERE id_number = ?");
        ps.setString(1, studentIdNum);
        ResultSet queryResults = ps.executeQuery();
        int studentId = queryResults.getInt(1);
        ps = db.prepareStatement("SELECT id FROM Courses WHERE name = ?");
        ps.setString(1, courseName);
        queryResults = ps.executeQuery();
        int courseId = queryResults.getInt(1);
        
        ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, studentId);
        ps.execute();
        
        Statement s = db.createStatement();
        s.execute("UPDATE Courses SET students = students + 1 WHERE id = " + courseId);
        
        db.close();
    }

    public ObservableList searchRegistrationsByStudents(String studentIdNum) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Students WHERE id_number = ?");
        ps.setString(1, studentIdNum);
        ResultSet queryResults = ps.executeQuery();
        if (!queryResults.next()) {
            return null;
        }   
        int studentId = queryResults.getInt(1);
        
        ps = db.prepareStatement("SELECT * FROM Courses WHERE id IN (SELECT course_id FROM Registrations WHERE student_id=?)");
        ps.setInt(1, studentId);
        queryResults = ps.executeQuery();

        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Course> courses = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate startDate = LocalDate.parse(queryResults.getString(3));
            LocalDate endDate = LocalDate.parse(queryResults.getString(4));
            Course course = new Course(queryResults.getString(2), startDate, endDate, queryResults.getString(5), queryResults.getInt(6), queryResults.getInt(7));
            courses.add(course);
        }
        
        db.close();
        return courses;
    }
    
    public ObservableList searchRegistrationsByCourses(String courseName) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Courses WHERE name = ?");
        ps.setString(1, courseName);
        ResultSet queryResults = ps.executeQuery();
        if (!queryResults.next()) {
            return null;
        }
        int courseId = queryResults.getInt(1);
        
        ps = db.prepareStatement("SELECT * FROM Students WHERE id IN (SELECT student_id FROM Registrations WHERE course_id=?)");
        ps.setInt(1, courseId);
        queryResults = ps.executeQuery();

        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        
        db.close();
        return students;
    }
}
