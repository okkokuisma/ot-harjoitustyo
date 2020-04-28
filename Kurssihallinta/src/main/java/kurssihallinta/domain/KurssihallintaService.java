/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import kurssihallinta.dao.ClassroomDao;
import kurssihallinta.dao.CourseDao;
import kurssihallinta.dao.KurssihallintaDao;
import kurssihallinta.dao.LessonDao;
import kurssihallinta.dao.RegistrationDao;
import kurssihallinta.dao.StudentDao;

/**
 *
 * @author okkokuisma
 */
public class KurssihallintaService {
    KurssihallintaDao students;
    CourseDao courses;
    KurssihallintaDao lessons;
    KurssihallintaDao classrooms;
    RegistrationDao registrations;
    
    public KurssihallintaService() {
        students = new StudentDao();
        courses = new CourseDao();
        lessons = new LessonDao();
        classrooms = new ClassroomDao();
        registrations = new RegistrationDao();
    }
    
    public boolean addStudent(String firstName, String surname, String id, String address, String city, String zipCode, String country, String email) {
        Student student = new Student(firstName, surname, id, address, city, zipCode, country, email);
        try {
            students.add(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addCourse(Course course) {
        try {
            courses.add(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addRegistration(String courseName, String studentIdNum) {
        try {
//            int studentId = students.getId(studentIdNum);
//            int courseId = courses.getId(courseName);
            registrations.add(courseName, studentIdNum);
//            courses.incrementStudentCount(courseId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean addLesson(Lesson lesson) {
        try {
            lessons.add(lesson);
        } catch (SQLException ex) {
            System.out.println("virhe");
            ex.printStackTrace();
        }
        
        return true;
    }
    
    public boolean addClassroom(Classroom classroom) {
        try {
            classrooms.add(classroom);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    public ObservableList searchCourses(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            courseList = courses.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return courseList;
    }
    
    public ObservableList searchStudents(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            studentList = students.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return studentList;
    }
    
    public ObservableList searchRegistrationsByStudentId(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            int studentId = students.getId(searchWord);
            courseList = registrations.searchRegistrationsByStudents(studentId);
        } catch (SQLException ex) { 
        }
        
        return courseList;
    }
    
    public ObservableList searchRegistrationsByCourseName(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            int courseId = courses.getId(searchWord);
            studentList = registrations.searchRegistrationsByCourses(courseId);
        } catch (SQLException ex) { 
        }
        
        return studentList;
    }
    
    public ObservableList searchLessonsByCourseName(String searchWord) {
        ObservableList<Student> lessonList = FXCollections.observableArrayList();
        try {
            
            lessonList = lessons.search(searchWord);
        } catch (SQLException ex) { 
        }
        
        return lessonList;
    }
    
    public ObservableList<String> getClassrooms() {
        ObservableList<Classroom> classroomList = FXCollections.observableArrayList();
        ObservableList<String> classroomsString = FXCollections.observableArrayList();
        try {
            classroomList = classrooms.getAll();
        } catch (SQLException ex) {
            Logger.getLogger(KurssihallintaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Classroom c : classroomList) {
            String name = c.getName();
            classroomsString.add(name);
        }
        return classroomsString;
    }
    
    public ObservableList getLessons() {
        ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
        try {
            lessonList = lessons.getAll();
        } catch (SQLException ex) {        
        }
        
        return lessonList;
    }
    
    public boolean updateCourse(Course course) {
        try {
            courses.update(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean updateStudent(Student student) {
        try {
            students.update(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
//    
//    public boolean getCourseId(Course course) {
//        try {
//            courses.getId(course);
//        } catch (SQLException ex) {
//            return false;
//        }
//        
//        return true;
//    }
//    
//    public boolean getStudentId(Student student) {
//        try {
//            students.getId(student);
//        } catch (SQLException ex) {
//            return false;
//        }
//        
//        return true;
//    }
}
