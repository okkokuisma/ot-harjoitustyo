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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Classroom;

/**
 *
 * @author ogkuisma
 */
public class TestClassroomDao implements KurssihallintaDao<Classroom, String> {
    @Override
    public void add(Classroom classroom) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("INSERT INTO Classrooms (name) VALUES (?)");
        ps.setString(1, classroom.getName());
        ps.execute();
        
        db.close();
    }

    @Override
    public void update(Classroom object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList search(String key) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement ps = db.createStatement();

        ResultSet queryResults = ps.executeQuery("SELECT name FROM Classrooms");
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Classroom> classrooms = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Classroom classroom = new Classroom(queryResults.getString(1));
            classrooms.add(classroom);
        }
        
        db.close();
        return classrooms;
    }

    @Override
    public int getId(String key) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("SELECT id FROM Classrooms WHERE name = ?");
        ps.setString(1, key);
        ResultSet queryResults = ps.executeQuery();
        int classroomId = queryResults.getInt(1);
        db.close();
        
        return classroomId;
    }

    @Override
    public Classroom get(int key) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Classrooms WHERE id = ?");
        ps.setInt(1, key);
        ResultSet queryResults = ps.executeQuery();
        Classroom classroom = new Classroom(queryResults.getString(2));
        db.close();
        
        return classroom;
    }

    @Override
    public ObservableList getAll() throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement ps = db.createStatement();

        ResultSet queryResults = ps.executeQuery("SELECT name FROM Classrooms");
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Classroom> classrooms = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Classroom classroom = new Classroom(queryResults.getString(1));
            classrooms.add(classroom);
        }
        
        db.close();
        return classrooms;
    }
}
