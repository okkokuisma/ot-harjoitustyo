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
import kurssihallinta.domain.Classroom;
import kurssihallinta.domain.Course;

/**
 *
 * Data Access Object used to manage database operations with Classroom objects.
 */
public class ClassroomDao implements KurssihallintaDao<Classroom, String> {
    private Connection db;
    /**
    * Adds the Classroom object given as a parameter to database.
    *
    * @param    classroom  Classroom object to be added to database
    */
    @Override
    public void add(Classroom classroom) throws SQLException {
        PreparedStatement ps = db.prepareStatement("INSERT INTO Classrooms (name) VALUES (?)");
        ps.setString(1, classroom.getName());
        ps.execute();
        
        db.close();
    }
    
    /**
    * Changes the data in the Classrooms table of a Classroom object given as a parameter.
    *
    * @param    classroom  Classroom object with the data to be modified
    */
    @Override
    public void update(Classroom classroom) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
    * Retrieves rows from Classrooms table filtered by name column.
    *
    * @param    key  Search word given by user
    * 
    * @return   ObservableList of Classroom objects
    */
    @Override
    public ObservableList search(String key) throws SQLException {
        String searchWord = "%" + key + "%";
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Classrooms WHERE name LIKE ?");
        ps.setString(1, searchWord);
        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Classroom> classrooms = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Classroom classroom = new Classroom(queryResults.getString(1));
            classrooms.add(classroom);
        }
        
        db.close();
        return classrooms;
    }
    
    /**
    * Retrieves the integer primary key of a row with matching name value.
    *
    * @param    key  The name of a Classroom
    * 
    * @return   Integer primary key
    */
    @Override
    public int getId(String key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Classrooms WHERE name = ?");
        ps.setString(1, key);
        ResultSet queryResults = ps.executeQuery();
        int classroomId = queryResults.getInt(1);
        db.close();
        
        return classroomId;
    }
    
    /**
    * Retrieves the row from Classrooms table with a matching integer primary key value.
    *
    * @param    key  Integer primary key
    * 
    * @return   Classroom object with the retrieved data
    */
    @Override
    public Classroom get(int key) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Classrooms WHERE id = ?");
        ps.setInt(1, key);
        ResultSet queryResults = ps.executeQuery();
        Classroom classroom = new Classroom(queryResults.getString(2));
        db.close();
        
        return classroom;
    }
    
    /**
    * Retrieves all rows from Classrooms table.
    * 
    * @return   ObservableList of Classroom objects
    */
    @Override
    public ObservableList getAll() throws SQLException {
        Statement ps = db.createStatement();

        ResultSet queryResults = ps.executeQuery("SELECT name FROM Classrooms");
        
        ObservableList<Classroom> classrooms = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Classroom classroom = new Classroom(queryResults.getString(1));
            classrooms.add(classroom);
        }
        
        db.close();
        return classrooms;
    }
    
    @Override
    public void setConnection(Connection db) throws SQLException {
        this.db = db;
    }
}
