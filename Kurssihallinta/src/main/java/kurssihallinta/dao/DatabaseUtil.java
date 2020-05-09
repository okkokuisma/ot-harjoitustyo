/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import kurssihallinta.ui.KurssihallintaUi;

/**
 *
 * Utility class for creating database tables and managing connections between DAO objects and the database.
 */
public class DatabaseUtil {
    Connection db;
    String databasePath;
    
    /**
    *
    * If testMode is true the Connection is set to a test database, if testMode is false the Connection is set to database in a file path determined by the config.properties file.
    * @param testMode true for test classes, else false 
    */
    public DatabaseUtil(boolean testMode) {
        if (testMode) {
            databasePath = "jdbc:sqlite:test.db";
        } else {
            try {
                InputStream input = new FileInputStream("config.properties");
                Properties properties = new Properties();
                properties.load(input);
                databasePath = "jdbc:sqlite:" + properties.getProperty("dbFile") + ".db";
            } catch (FileNotFoundException ex) {
                System.out.println("Missing config.properties file.");
            } catch (IOException ex) {
                System.out.println("Missing config.properties file.");
            }

            try {
                db = DriverManager.getConnection(databasePath);
                Statement s = db.createStatement();
                ResultSet queryResult = s.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table'");
                if (queryResult.getInt(1) > 0) {
                    db.close();
                    return;
                }

                s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER)");
                s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
                s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
                s.execute("CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER)");
                s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
                db.close();
            } catch (SQLException ex) {
                Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databasePath);
    }
}
