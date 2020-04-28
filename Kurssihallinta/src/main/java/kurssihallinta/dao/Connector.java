/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import kurssihallinta.ui.KurssihallintaUi;

/**
 *
 * @author ogkuisma
 */
public class Connector {
    private Connection db;  
    
    public void initDb() {
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
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
            Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
