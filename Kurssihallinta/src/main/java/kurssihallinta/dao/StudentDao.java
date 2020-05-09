/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Student;

/**
 *
 * Data Access Object used to manage database operations with Student objects.
 */
public class StudentDao implements KurssihallintaDao<Student, String> {
    private Connection db;
    /**
    * Adds the Student object given as a parameter to database.
    *
    * @param    student  Student object to be added to database
    * @throws java.sql.SQLException
    */
    @Override
    public void add(Student student) throws SQLException {
        PreparedStatement ps = db.prepareStatement("INSERT INTO Students (first_name,surname,id_number,address,zip,city,country,email) VALUES (?,?,?,?,?,?,?,?)");
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getSurname());
        ps.setString(3, student.getId());
        ps.setString(4, student.getAddress());
        ps.setString(5, student.getZipCode());
        ps.setString(6, student.getCity());
        ps.setString(7, student.getCountry());
        ps.setString(8, student.getEmail());
        ps.execute();
        db.close();
    }
    
    /**
    * Changes the data in the Students table of a Student object given as a parameter.
    *
    * @param    student  Student object with the data to be modified
    * @throws java.sql.SQLException
    */
    @Override
    public void update(Student student) throws SQLException {
        PreparedStatement ps = db.prepareStatement("UPDATE Students SET "
                + "first_name = ?, "
                + "surname = ?, "
                + "address = ?, "
                + "zip = ?, "
                + "city = ?, "
                + "country = ?, "
                + "email = ? WHERE id_number = ?");
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getSurname());
        ps.setString(3, student.getAddress());
        ps.setString(4, student.getZipCode());
        ps.setString(5, student.getCity());
        ps.setString(6, student.getCountry());
        ps.setString(7, student.getEmail());
        ps.setString(8, student.getId());
        ps.execute();     
        db.close();
    }
    
    /**
    * Retrieves rows from Students table filtered by first_name and surname columns.
    *
    * @param    key  Search word given by user
    * 
    * @return   ObservableList of Student objects
    * @throws java.sql.SQLException
    */
    @Override
    public ObservableList search(String key) throws SQLException {
        String searchWord = "%" + key + "%";
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE first_name LIKE ? OR surname LIKE ?");
        ps.setString(1, searchWord);
        ps.setString(2, searchWord);

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        db.close();
        return students;
    }
    
    /**
    * Retrieves the integer primary key of a row with matching id_number value.
    *
    * @param    key  The id_number of a student
    * 
    * @return   Integer primary key
    * @throws java.sql.SQLException
    */
    @Override
    public int getId(String key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Students WHERE id_number = ?");
        ps.setString(1, key);
        ResultSet queryResults = ps.executeQuery();
        int studentId = queryResults.getInt(1);
        db.close();

        return studentId;
    }
    
    /**
    * Retrieves the row from Students table with a matching integer primary key value.
    *
    * @param    key  Integer primary key
    * 
    * @return   Student object with the retrieved data
    * @throws java.sql.SQLException
    */
    @Override
    public Student get(int key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE id = ?");
        ps.setInt(1, key);
        ResultSet queryResults = ps.executeQuery();
        Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
        db.close();
        
        return student;
    }
    
    /**
    * Retrieves all rows from Students table.
    * 
    * @return   ObservableList of Student objects
    * @throws java.sql.SQLException
    */
    @Override
    public ObservableList getAll() throws SQLException {
        Statement ps = db.createStatement();

        ResultSet queryResults = ps.executeQuery("SELECT * FROM Students");
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        
        db.close();
        
        return students;
    }
    
    @Override
    public void setConnection(Connection db) throws SQLException {
        this.db = db;
    }
}
