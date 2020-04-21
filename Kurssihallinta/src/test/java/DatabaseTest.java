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
                ps.setString(3, "id" + i);
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
                ps.setInt(5, 0);
                ps.setInt(6, i);
                ps.execute();
            }
            
            // ADD 10 REGISTRATIONS 
            ps = db.prepareStatement("INSERT INTO Registrations (course_id,student_id) VALUES (1,?)");
            for (int i = 1; i <= 10; i++) {
                ps.setInt(1, i);
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
        file.delete();
    }
    

    @Test
    public void courseDaoAdd() throws SQLException {     
        for (int i = 0; i < 10; i++) {
            LocalDate date = LocalDate.now();
            Course course = new Course("name" + i, date, date, "teacher" + i, i, i);
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
    public void courseDaoUpdate() throws SQLException {
        LocalDate date = LocalDate.of(2000, 1, 1);
        Course before = new Course("update", date, date, "teacher", 0, 0);
        courseDao.add(before);
        
        date = LocalDate.of(2001, 1, 1);
        Course after = new Course("update", date, date, "anotherTeacher", 0, 10);
        courseDao.update(after);
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM Courses WHERE name = 'update'");

        assertEquals(after.getStartDate(), rs.getString(3));
        assertEquals(after.getEndDate(), rs.getString(4));
        assertEquals(after.getTeacher(), rs.getString(5));
        assertEquals(after.getMaxStudents(), rs.getInt(7));
        
        db.close();
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
            Student student = new Student("firstName" + i,"surname" + i,"idNum" + i,"address" + i,"city" + i,"zipcode" + i,"country" + i,"email" + i);
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
    public void studentDaoUpdate() throws SQLException {
        Student before = new Student("firstName", "surname", "update", "address", "city", "zipCode", "country", "email");
        studentDao.add(before);
        
        Student after = new Student("anotherFirstName", "anotherSurname", "update", "anotherAddress", "anotherCity", "anotherZip", "anotherCountry", "anotherEmail");
        studentDao.update(after);
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM Students WHERE id_number = 'update'");
        
        assertEquals(after.getFirstName(), rs.getString(2));
        assertEquals(after.getSurname(), rs.getString(3));
        assertEquals(after.getAddress(), rs.getString(5));
        assertEquals(after.getZipCode(), rs.getString(6));
        assertEquals(after.getCity(), rs.getString(7));
        assertEquals(after.getCountry(), rs.getString(8));
        assertEquals(after.getEmail(), rs.getString(9));
       
        db.close();
    }
    
    @Test
    public void studentDaoSearch() throws SQLException {
        ObservableList students = studentDao.search("xxx");
        assertEquals(0, students.size());
        
        students = studentDao.search("name");
        assertEquals(10, students.size());
    }
    
    @Test
    public void registrationDaoAdd() throws SQLException {
        
        for (int i = 0; i < 10; i++) {
            registrationDao.add("course0", "id" + i);
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Registrations");
        int registrations = rs.getInt(1);
        rs = s.executeQuery("SELECT COUNT(*) FROM Registrations WHERE course_id = 1");
        int registrationsByCourse = rs.getInt(1);
        db.close();
        
        assertEquals(20, registrations);
        assertEquals(20, registrationsByCourse);
    }
    
    @Test
    public void registrationDaoSearchByStudents() throws SQLException {      
        ObservableList registrations = registrationDao.searchRegistrationsByStudents("xxx");
        assertTrue(registrations == null);
        
        registrations = registrationDao.searchRegistrationsByStudents("id0");
        assertEquals(1, registrations.size());
    }
    
    @Test
    public void registrationDaoSearchByCourses() throws SQLException {        
        ObservableList registrations = registrationDao.searchRegistrationsByCourses("xxx");
        assertTrue(registrations == null);
        
        registrations = registrationDao.searchRegistrationsByCourses("course0");
        assertEquals(10, registrations.size());
    }
    
    @Test
    public void addingRegistrationsIncrementsStudentCountInCourses() throws SQLException {  
        for (int i = 0; i < 10; i++) {
            registrationDao.add("course1", "id" + i);
        }
        
        Course course = (Course) courseDao.search("course1").get(0);
        assertEquals(10, course.getStudents());
    }
}
