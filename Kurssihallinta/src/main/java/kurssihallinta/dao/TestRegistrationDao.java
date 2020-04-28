/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.dao;

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
public class TestRegistrationDao {
    private TestCourseDao courses;
    private TestStudentDao students;
    
    public TestRegistrationDao() {
        courses = new TestCourseDao();
        students = new TestStudentDao();
    }
    
    public void add(String courseName, String studentIdNum) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        int courseId = courses.getId(courseName);
        int studentId = students.getId(studentIdNum);
        PreparedStatement ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, studentId);
        ps.execute();
        
        db.close();
    }
    
    public ObservableList searchRegistrationsByStudents(int studentId) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE id IN (SELECT course_id FROM Registrations WHERE student_id = ?)");
        ps.setInt(1, studentId);
        ResultSet queryResults = ps.executeQuery();

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

    public ObservableList searchRegistrationsByCourses(int courseId) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");  
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE id IN (SELECT student_id FROM Registrations WHERE course_id=?)");
        ps.setInt(1, courseId);
        ResultSet queryResults = ps.executeQuery();

        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        
        db.close();
        return students;
    }
}
