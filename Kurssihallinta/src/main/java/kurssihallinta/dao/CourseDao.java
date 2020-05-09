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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Course;

/**
 *
 * Data Access Object used to manage database operations with Course objects.
 */
public class CourseDao implements KurssihallintaDao<Course, String> {
    private Connection db;
    /**
    * Adds the Course object given as a parameter to database.
    *
    * @param    course  Course object to be added to database
    */
    @Override
    public void add(Course course) throws SQLException {
        PreparedStatement ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher,students,max_students) VALUES (?,?,?,?,?,?)");
        ps.setString(1, course.getName());
        ps.setString(2, course.getStartDate());
        ps.setString(3, course.getEndDate());
        ps.setString(4, course.getTeacher());
        ps.setInt(5, 0);
        ps.setInt(6, course.getMaxStudents());
        ps.execute();
        
        db.close();
    }
    
    /**
    * Changes the data in the database of a Course object given as a parameter.
    *
    * @param    course  Course object with the data to be modified
    */
    @Override
    public void update(Course course) throws SQLException {
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
    
    /**
    * Retrieves rows from Courses table filtered by name column.
    *
    * @param    key  Search word given by user
    * 
    * @return   ObservableList of Course objects
    */
    @Override
    public ObservableList search(String key) throws SQLException {
        String searchWord = "%" + key + "%";
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE name LIKE ?");
        ps.setString(1, searchWord);

        ResultSet queryResults = ps.executeQuery();
        
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
    
    /**
    * Retrieves the integer primary key of a row with matching name value.
    *
    * @param    key  Course name
    * 
    * @return   Integer primary key
    */
    @Override
    public int getId(String key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Courses WHERE name = ?");
        ps.setString(1, key);
        ResultSet queryResults = ps.executeQuery();
        int courseId = queryResults.getInt(1);
        db.close();

        return courseId;
    }
    
    /**
    * Retrieves the row from Courses table with a matching integer primary key value.
    *
    * @param    key  Integer primary key
    * 
    * @return   Course object with the retrieved data
    */
    @Override
    public Course get(int key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE id = ?");
        ps.setInt(1, key);
        ResultSet queryResults = ps.executeQuery();
        LocalDate startDate = LocalDate.parse(queryResults.getString(3));
        LocalDate endDate = LocalDate.parse(queryResults.getString(4));
        Course course = new Course(queryResults.getString(2), startDate, endDate, queryResults.getString(5), queryResults.getInt(6), queryResults.getInt(7));
        db.close();
        
        return course;
    }
    
    /**
    * Retrieves all rows from Courses table.
    * 
    * @return   ObservableList of Course objects
    */
    @Override
    public ObservableList getAll() throws SQLException {
        Statement ps = db.createStatement();

        ResultSet queryResults = ps.executeQuery("SELECT * FROM Courses");
        
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
    
    /**
    * Increments the students column value by one of a row with matching integer primary key value.
    * 
    * @param    courseId    Integer primary key
    */
    public void incrementStudentCount(int courseId) throws SQLException {
        PreparedStatement ps = db.prepareStatement("UPDATE Courses SET students = students + 1 WHERE id = ?");
        ps.setInt(1, courseId);
        ps.execute();
        db.close();
    }

    @Override
    public void setConnection(Connection db) throws SQLException {
        this.db = db;
    }

}
