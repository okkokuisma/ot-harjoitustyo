/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author okkokuisma
 */
public class Database {
    private Connection db;
    
    public void connect() {    
        try {
            db = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement s = db.createStatement();
            s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT)");
            s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
            s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
            s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean addStudent(String firstName, String surname, String idNumber, String address, String zipCode, String city, String country, String email) throws SQLException { 
            PreparedStatement ps = db.prepareStatement("INSERT INTO Students (first_name,surname,id_number,address,zip,city,country,email) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, firstName);
            ps.setString(2, surname);
            ps.setString(3, idNumber);
            ps.setString(4, address);
            ps.setString(5, zipCode);
            ps.setString(6, city);
            ps.setString(7, country);
            ps.setString(8, email);
            ps.execute();
            return true;
    }
    
    public boolean addCourse(String name, String startDate, String endDate, String teacher) {
        try {     
            PreparedStatement ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher) VALUES (?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setString(4, teacher);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Lisäys tietokantaan ei onnistunut.");
            return false;
        }
    }
}
