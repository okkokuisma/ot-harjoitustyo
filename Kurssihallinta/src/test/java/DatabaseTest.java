/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import kurssihallinta.database.TestCourseDao;
import kurssihallinta.database.TestRegistrationDao;
import kurssihallinta.database.TestStudentDao;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Student;
import kurssihallinta.ui.KurssihallintaUi;
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
    TestCourseDao courseDao;
    TestStudentDao studentDao;
    TestRegistrationDao registrationDao;
    
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
        
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement s = db.createStatement();

            s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER)");
            s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
            s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
            s.execute("CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER)");
            s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
            
            // ADD 10 STUDENTS
            PreparedStatement ps = db.prepareStatement("INSERT INTO Students (first_name,surname,id_number,address,zip,city,country,email) VALUES (?,?,?,?,?,?,?,?)");
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "firstname" + i);
                ps.setString(2, "surname" + i);
                ps.setString(3, "i_d" + i);
                ps.setString(4, "address" + i);
                ps.setString(5, "city" + i);
                ps.setString(6, "zip" + i);
                ps.setString(7, "country" + i);
                ps.setString(8, "email" + i);
                ps.execute();
            }
            
            // ADD 10 COURSES
            ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher,students,max_students) VALUES (?,?,?,?,?,?)");
            LocalDate date = LocalDate.now();
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "course" + i);
                ps.setString(2, date.toString());
                ps.setString(3, date.toString());
                ps.setString(4, "teacher" + i);
                ps.setInt(5, i);
                ps.setInt(6, i);
                ps.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        courseDao = new TestCourseDao();
        studentDao = new TestStudentDao();
        registrationDao = new TestRegistrationDao();
    }
    
    @After
    public void tearDown() {
        File file = new File("test.db");
        System.out.println(file.delete());
    }
    

    @Test
    public void courseDaoAdd() throws SQLException {
        
        for (int i = 0; i < 10; i++) {
            LocalDate date = LocalDate.now();
            Course course = new Course("name" + i, date, date, "teacher" + i, i);
            courseDao.add(course);
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Courses");
        int courses = rs.getInt(1);
        db.close();
        
        assertEquals(20, courses);
    }
    
    @Test
    public void courseDaoSearch() throws SQLException {
        ObservableList courses = courseDao.search("xxx");
        assertEquals(0, courses.size());      
        
        courses = courseDao.search("course");
        assertEquals(10, courses.size());
    }

    @Test
    public void studentDaoAdd() throws SQLException {
        for (int i = 0; i < 10; i++) {
            Student student = new Student("firstName" + i,"surname" + i,"id" + i,"address" + i,"city" + i,"zipcode" + i,"country" + i,"email" + i);
            studentDao.add(student);
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Students");
        int courses = rs.getInt(1);
        db.close();
        
        assertEquals(20, courses);
    }
    
    @Test
    public void studentDaoSearch() throws SQLException {
        ObservableList students = studentDao.search("xxx");
        assertEquals(0, students.size());
        
        students = studentDao.search("name");
        assertEquals(10, students.size());
    }
    
//    @Test
//    public void registrationDaoAdd() throws SQLException {
//        
//        for (int i = 0; i < 10; i++) {
//            registrationDao.add("course" + i);
//        }
//        
//        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
//        Statement s = db.createStatement();
//        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Courses");
//        int courses = rs.getInt(1);
//        db.close();
//        
//        assertEquals(10, courses);
//    }
}
