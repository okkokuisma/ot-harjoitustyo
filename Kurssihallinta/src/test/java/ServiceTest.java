/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;
import kurssihallinta.dao.DatabaseUtil;
import kurssihallinta.domain.Classroom;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.KurssihallintaService;
import kurssihallinta.domain.Lesson;
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
 * @author okkokuisma
 */
public class ServiceTest {
    KurssihallintaService service;
    Student student;
    Course course;
    Lesson lesson1;
    Lesson lesson2;
    Classroom classroom;
    
    public ServiceTest() {
    }
    
    @Before
    public void setUp() {
        service = new KurssihallintaService();
        DatabaseUtil dbUtil = new DatabaseUtil(true);
        service.setDbUtil(dbUtil);
        student = new Student("John", "Smith", "id123", "High Road", "20210", "New York", "USA", "john.smith.com");
        course = new Course("German 1", LocalDate.now(), LocalDate.now(), "Matti", 0, 10);
        lesson1 = new Lesson(course, "Classroom 2", LocalDate.now(), LocalTime.now(), LocalTime.now());
        lesson2 = new Lesson(course, "Classroom 2", LocalDate.of(2001, Month.MARCH, 1), LocalTime.now(), LocalTime.now());
        classroom = new Classroom("Classroom 2");
        
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement s = db.createStatement();

            s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER)");
            s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
            s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
            s.execute("CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER)");
            s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
        } catch (SQLException ex) {
            Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        File testFile = new File("test.db");
        testFile.delete();
    }

    @Test
    public void sameCourseNameCantBeAddedTwice() {
        assertTrue(service.addCourse(course));
        assertFalse(service.addCourse(course));
    }
    
    @Test
    public void sameStudentIdCantBeAddedTwice() {
        assertTrue(service.addStudent(student));
        assertFalse(service.addStudent(student));
    }
    
    @Test
    public void addRegistrationAdds() {
        service.addStudent(student);
        service.addCourse(course);
        assertTrue(service.addRegistration(course.getName(), student.getId()));
    }
    
    @Test
    public void addRegistrationIncrementsStudentCount() {
        service.addStudent(student);
        service.addCourse(course);
        service.addRegistration(course.getName(), student.getId());
        Course c = (Course) service.searchCourses("German").get(0);
        assertEquals(1, c.getStudents());
    }
    
    @Test
    public void getLessonsFilteredByClassroom() {
        service.addCourse(course);
        service.addClassroom(classroom);
        service.addLesson(lesson1);
        assertEquals(1, service.getLessonsFilteredByClassroom("Classroom 2").size());
        
        service.addLesson(lesson2);
        assertEquals(2, service.getLessonsFilteredByClassroom("Classroom 2").size());
    }
    
    @Test
    public void getLessonsFilteredByDate() {
        service.addCourse(course);
        service.addClassroom(classroom);
        service.addLesson(lesson1);
        assertEquals(1, service.getLessonsFilteredByDate(LocalDate.now()).size());
        
        service.addLesson(lesson2);
        assertEquals(1, service.getLessonsFilteredByDate(LocalDate.now()).size());
    }
    
    @Test
    public void getLessonsFilteredByClassroomAndDate() {
        service.addCourse(course);
        service.addClassroom(classroom);
        service.addLesson(lesson1);
        assertEquals(1, service.getLessonsFilteredByClassroomAndDate("Classroom 2", LocalDate.now()).size());
        
        service.addLesson(lesson2);
        assertEquals(1, service.getLessonsFilteredByClassroomAndDate("Classroom 2", lesson2.getDate()).size());
        assertEquals(0, service.getLessonsFilteredByClassroomAndDate("Classroom 1", lesson2.getDate()).size());
        assertEquals(0, service.getLessonsFilteredByClassroomAndDate("Classroom 2", LocalDate.of(1999, Month.MARCH, 1)).size());
    }
    
    @Test
    public void getClassroomsReturnsAllClassrooms() {
        assertEquals(0, service.getClassrooms().size());
        service.addClassroom(classroom);
        assertEquals(1, service.getClassrooms().size());
        service.addClassroom(new Classroom("Classroom 3"));
        assertEquals(2, service.getClassrooms().size());
    }
}
