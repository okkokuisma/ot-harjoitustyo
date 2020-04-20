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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Course;
import kurssihallinta.ui.KurssihallintaUi;

/**
 *
 * @author okkokuisma
 */
public class TestCourseDao implements KurssihallintaDao<Course, String> {
    
    @Override
    public void add(Course object) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher,students,max_students) VALUES (?,?,?,?,?,?)");
        ps.setString(1, object.getName());
        ps.setString(2, object.getStartDate());
        ps.setString(3, object.getEndDate());
        ps.setString(4, object.getTeacher());
        ps.setInt(5, 0);
        ps.setInt(6, object.getMaxStudents());
        ps.execute();
        
        db.close();
    }
    
    @Override
    public void update(Course course) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("UPDATE Courses SET "
                + "startdate = ?, "
                + "enddate = ?, "
                + "teacher = ?, "
                + "max_students = ? "
                + "WHERE name = ?");
        ps.setString(1, course.getStartDate());
        ps.setString(2, course.getEndDate());
        ps.setString(3, course.getTeacher());
        ps.setInt(4, course.getMaxStudents());
        ps.setString(5, course.getName());
        ps.execute();
        
        db.close();
    }

    @Override
    public ObservableList search(String key) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE name LIKE '%" + key + "%'");

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Course> courses = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate startDate = LocalDate.parse(queryResults.getString(3));
            LocalDate endDate = LocalDate.parse(queryResults.getString(4));
            Course course = new Course(queryResults.getString(2), startDate, endDate, queryResults.getString(5), queryResults.getInt(6), queryResults.getInt(7));
            courses.add(course);
        }
        
        return courses;
    }
}
