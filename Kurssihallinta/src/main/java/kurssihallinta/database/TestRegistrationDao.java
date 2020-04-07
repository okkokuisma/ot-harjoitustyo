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

/**
 *
 * @author okkokuisma
 */
public class TestRegistrationDao implements KurssihallintaDao<String, String> {

    @Override
    public void add(String courseName) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet queryResults = s.executeQuery("SELECT id FROM Students ORDER BY id DESC LIMIT 1");
        int studentId = queryResults.getInt(1);
        queryResults = s.executeQuery("SELECT id FROM Courses WHERE name = " + courseName);
        int courseId = queryResults.getInt(1);
        
        PreparedStatement ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, studentId);
        ps.execute();  
    }
    
    public void add(String courseName, String studentIdNum) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet queryResults = s.executeQuery("SELECT id FROM Students WHERE id_number = " + studentIdNum);
        int studentId = queryResults.getInt(1);
        queryResults = s.executeQuery("SELECT id FROM Courses WHERE name = " + courseName);
        int courseId = queryResults.getInt(1);
        
        PreparedStatement ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, studentId);
        ps.execute();
        
        db.close();
    }

    @Override
    public ObservableList search(String studentIdNum) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet queryResults = s.executeQuery("SELECT id FROM Students WHERE id_number = " + studentIdNum);
        int studentId = queryResults.getInt(1);
        
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE id = (SELECT course_id FROM Registrations WHERE student_id=?);");
        ps.setInt(1, studentId);
        queryResults = ps.executeQuery();

        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Course> courses = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate startDate = LocalDate.parse(queryResults.getString(3));
            LocalDate endDate = LocalDate.parse(queryResults.getString(4));
            Course course = new Course(queryResults.getString(2), startDate, endDate, queryResults.getString(5), queryResults.getInt(7));
            courses.add(course);
        }

        return courses;
    }
}
