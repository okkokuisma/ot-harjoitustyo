package kurssihallinta.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import kurssihallinta.dao.ClassroomDao;
import kurssihallinta.dao.CourseDao;
import kurssihallinta.dao.DatabaseUtil;
import kurssihallinta.dao.LessonDao;
import kurssihallinta.dao.RegistrationDao;
import kurssihallinta.dao.StudentDao;
import kurssihallinta.domain.Classroom;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Lesson;
import kurssihallinta.domain.Student;
import kurssihallinta.ui.KurssihallintaUi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class DaoTest {
    DatabaseUtil dbUtil;
    CourseDao courseDao;
    StudentDao studentDao;
    RegistrationDao registrationDao;
    ClassroomDao classroomDao;
    LessonDao lessonDao;
    
    public DaoTest() {
    }
    
    @Before
    public void setUp() {
        dbUtil = new DatabaseUtil(true);
        courseDao = new CourseDao();
        studentDao = new StudentDao();
        registrationDao = new RegistrationDao();
        classroomDao = new ClassroomDao();
        lessonDao = new LessonDao(dbUtil);
        
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
            
            // Add 10 classrooms
            ps = db.prepareStatement("INSERT INTO Classrooms (name) VALUES (?)");
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "Room" + i); 
                ps.execute();
            }
            
            // Add 10 lessons
            ps = db.prepareStatement("INSERT INTO Lessons (course_id,classroom_id,date,starttime,endtime) VALUES (?,?,?,?,?)");
            LocalTime time = LocalTime.now();
            for (int i = 0; i < 10; i++) {
                ps.setInt(1, 1);
                ps.setInt(2, 1);
                ps.setString(3, date.toString());
                ps.setString(4, time.toString());
                ps.setString(5, time.toString());
                ps.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @After
    public void tearDown() {
        File file = new File("test.db");
        file.delete();
        file = new File("testName.db");
        file.delete();
    }
    

    @Test
    public void courseDaoAdd() throws SQLException {     
        for (int i = 0; i < 10; i++) {
            LocalDate date = LocalDate.now();
            Course course = new Course("name" + i, date, date, "teacher" + i, i, i);
            courseDao.setConnection(dbUtil.getConnection());
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
        courseDao.setConnection(dbUtil.getConnection());
        courseDao.add(before);
        
        date = LocalDate.of(2001, 1, 1);
        Course after = new Course("update", date, date, "anotherTeacher", 0, 10);
        courseDao.setConnection(dbUtil.getConnection());
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
        courseDao.setConnection(dbUtil.getConnection());
        ObservableList courses = courseDao.search("xxx");
        assertEquals(0, courses.size());      
        
        courseDao.setConnection(dbUtil.getConnection());
        courses = courseDao.search("course");
        assertEquals(10, courses.size());
    }
    
    @Test
    public void courseDaoGetId() throws SQLException {
        courseDao.setConnection(dbUtil.getConnection());
        int id = courseDao.getId("course0");
        assertEquals(1, id);
        
        courseDao.setConnection(dbUtil.getConnection());
        id = courseDao.getId("course9");
        assertEquals(10, id);
    }
    
    @Test
    public void courseDaoGetCourse() throws SQLException {
        courseDao.setConnection(dbUtil.getConnection());
        Course course = courseDao.get(1);
        assertEquals("course0", course.getName());
        
        courseDao.setConnection(dbUtil.getConnection());
        course = courseDao.get(10);
        assertEquals("course9", course.getName());
    }
    
    @Test
    public void courseDaoGetAll() throws SQLException {
        courseDao.setConnection(dbUtil.getConnection());
        assertEquals(10, courseDao.getAll().size());
    }

    @Test
    public void studentDaoAdd() throws SQLException {
        for (int i = 0; i < 10; i++) {
            Student student = new Student("firstName" + i,"surname" + i,"idNum" + i,"address" + i,"city" + i,"zipcode" + i,"country" + i,"email" + i);
            studentDao.setConnection(dbUtil.getConnection());
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
        studentDao.setConnection(dbUtil.getConnection());
        studentDao.add(before);
        
        Student after = new Student("anotherFirstName", "anotherSurname", "update", "anotherAddress", "anotherCity", "anotherZip", "anotherCountry", "anotherEmail");
        studentDao.setConnection(dbUtil.getConnection());
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
        studentDao.setConnection(dbUtil.getConnection());
        ObservableList students = studentDao.search("xxx");
        assertEquals(0, students.size());
        
        studentDao.setConnection(dbUtil.getConnection());
        students = studentDao.search("name");
        assertEquals(10, students.size());
    }
    
    @Test
    public void studentDaoGetId() throws SQLException {
        studentDao.setConnection(dbUtil.getConnection());
        int id = studentDao.getId("id0");
        assertEquals(1, id);
        
        studentDao.setConnection(dbUtil.getConnection());
        id = studentDao.getId("id9");
        assertEquals(10, id);
        
    }
    
    @Test
    public void studentDaoGetCourse() throws SQLException {
        studentDao.setConnection(dbUtil.getConnection());
        Student student = studentDao.get(1);
        assertEquals("id0", student.getId());
        
        studentDao.setConnection(dbUtil.getConnection());
        student = studentDao.get(10);
        assertEquals("id9", student.getId());
    }
    
    @Test
    public void studentDaoGetAll() throws SQLException {
        studentDao.setConnection(dbUtil.getConnection());
        assertEquals(10, studentDao.getAll().size());
    }
    
    @Test
    public void registrationDaoSearchByStudents() throws SQLException {      
        registrationDao.setConnection(dbUtil.getConnection());
        ObservableList registrations = registrationDao.searchRegistrationsByStudents(1);
        assertEquals(1, registrations.size());
    }
    
    @Test
    public void registrationDaoSearchByCourses() throws SQLException {      
        registrationDao.setConnection(dbUtil.getConnection());
        ObservableList registrations = registrationDao.searchRegistrationsByCourses(1);
        assertEquals(10, registrations.size());
    }
    
    @Test
    public void classroomDaoAdd() throws SQLException {     
        for (int i = 0; i < 10; i++) {
            Classroom classroom = new Classroom("Classroom" + i);
            classroomDao.setConnection(dbUtil.getConnection());
            classroomDao.add(classroom);
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Classrooms");
        int classrooms = rs.getInt(1);
        db.close();
        
        assertEquals(20, classrooms);
    }
    
    @Test
    public void classroomDaoGetAll() throws SQLException {
        classroomDao.setConnection(dbUtil.getConnection());
        assertEquals(10, classroomDao.getAll().size());
    }
    
    @Test
    public void lessonDaoAdd() throws SQLException {     
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();        
        for (int i = 0; i < 10; i++) {
            Course course = new Course("name" + i, date, date, "teacher" + i, i, i);
            courseDao.setConnection(dbUtil.getConnection());
            courseDao.add(course);
            Lesson lesson = new Lesson(course, "Room" + i, date, time, time);
            lessonDao.setConnection(dbUtil.getConnection());
            lessonDao.add(lesson);
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Lessons");
        int lessons = rs.getInt(1);
        db.close();
        
        assertEquals(20, lessons);
    }
    
    @Test
    public void lessonDaoSearch() throws SQLException {
        lessonDao.setConnection(dbUtil.getConnection());
        assertEquals(10, lessonDao.search("course0").size());
    }
    
    @Test
    public void lessonDaoGetAll() throws SQLException {
        lessonDao.setConnection(dbUtil.getConnection());
        assertEquals(10, lessonDao.getAll().size());
    }
    
    @Test
    public void databaseUtilConfig() throws SQLException {
        String property = "";
        try {
            InputStream input = new FileInputStream("config.properties");
            Properties properties = new Properties();
            properties.load(input);
            property = properties.getProperty("dbFile");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        DatabaseUtil testDbUtil = new DatabaseUtil(false);
        File test = new File(property + ".db");
        assertTrue(test.exists());
        
        Connection db = testDbUtil.getConnection();
        Statement s = db.createStatement();
        ResultSet queryResult = s.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table'");
        assertEquals(5, queryResult.getInt(1));
        db.close();
    }
}
