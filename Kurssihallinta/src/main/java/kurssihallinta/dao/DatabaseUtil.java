/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
 * @author okkokuisma
 */
public class DatabaseUtil {
    Connection db;
    String databasePath;
    
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
                Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
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
