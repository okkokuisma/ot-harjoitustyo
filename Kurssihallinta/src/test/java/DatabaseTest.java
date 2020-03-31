/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import kurssihallinta.database.Database;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class DatabaseTest {
    Database db;
    Connection conn;
    
    public DatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        db = new Database();
    }
    
    @After
    public void tearDown() {
    }
    
//    @Test
//    public void addCourseAddsCourses() {
//        int coursesBefore = 0;
//        try {
//            conn  = DriverManager.getConnection("jdbc:sqlite:database.db");
//            Statement s = conn.createStatement();
//            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Courses");
//            coursesBefore = rs.getInt(1);
//            conn.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(NewEmptyJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        db.connect();
//        
//        String courseName = "course";
//        String date = "date";
//        String teacher = "teacher";
//        int maxNumber = 0;
//        
//        for (int i = 0; i < 10; i++) {
//            courseName = courseName + i;
//            date = date + i;
//            teacher = teacher + i;
//            maxNumber += i;
//            try {
//                db.addCourse(courseName, date, date, teacher, maxNumber);
//            } catch (SQLException ex) {
//                Logger.getLogger(NewEmptyJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        
//        int coursesAfter = 0;
//        try {
//            conn  = DriverManager.getConnection("jdbc:sqlite:database.db");
//            Statement s = conn.createStatement();
//            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Courses");
//            coursesAfter = rs.getInt(1);
//            conn.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(NewEmptyJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        assertEquals(10, coursesAfter - coursesBefore);
//    }

    @Test
    public void rightNumberTablesAfterConnecting() {
        db.connect();
        try {
            conn  = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table'");
            assertEquals(5, rs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
