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
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Classroom;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Lesson;

/**
 *
 * @author ogkuisma
 */
public class TestLessonDao implements KurssihallintaDao<Lesson, String> {
    private TestCourseDao courses;
    private TestClassroomDao classrooms;
    
    public TestLessonDao() {
        courses = new TestCourseDao();
        classrooms = new TestClassroomDao();
    }
    
    @Override
    public void add(Lesson lesson) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        int courseId = courses.getId(lesson.getCourse().getName());
        int classroomId = classrooms.getId(lesson.getClassroom());
        PreparedStatement ps = db.prepareStatement("INSERT INTO Lessons (course_id,classroom_id,date,starttime,endtime) VALUES (?,?,?,?,?)");
        ps.setInt(1, courseId);
        ps.setInt(2, classroomId);
        ps.setString(3, lesson.getDate().toString());
        ps.setString(4, lesson.getStartTime().toString());
        ps.setString(5, lesson.getEndTime().toString());
        ps.execute();
        
        db.close();
    }

    @Override
    public void update(Lesson object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList search(String key) throws SQLException {
        int courseId = courses.getId(key);
        if (courseId == -1) { return null; }
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Lessons WHERE course_id = ?");
        ps.setInt(1, courseId);
        ResultSet queryResults = ps.executeQuery();
        ObservableList<Lesson> lessons = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate date = LocalDate.parse(queryResults.getString(4));
            LocalTime start = LocalTime.parse(queryResults.getString(5));
            LocalTime end = LocalTime.parse(queryResults.getString(6));
            Course course = (Course) courses.get(courseId);
            Classroom classroom = (Classroom) classrooms.get(queryResults.getInt(3));
            Lesson lesson = new Lesson(course, classroom.getName(), date, start, end);
            lessons.add(lesson);
        }       
        db.close();
        return lessons;
    }

    @Override
    public int getId(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Lesson get(int key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList getAll() throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement ps = db.createStatement();
        ResultSet queryResults = ps.executeQuery("SELECT * FROM Lessons");
        ObservableList<Lesson> lessons = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate date = LocalDate.parse(queryResults.getString(4));
            LocalTime start = LocalTime.parse(queryResults.getString(5));
            LocalTime end = LocalTime.parse(queryResults.getString(6));
            Course course = (Course) courses.get(queryResults.getInt(2));
            Classroom classroom = (Classroom) classrooms.get(queryResults.getInt(3));
            Lesson lesson = new Lesson(course, classroom.getName(), date, start, end);
            lessons.add(lesson);
        }
        
        db.close();
        return lessons;
    }
}