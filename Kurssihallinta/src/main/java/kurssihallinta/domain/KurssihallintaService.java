/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.dao.ClassroomDao;
import kurssihallinta.dao.CourseDao;
import kurssihallinta.dao.DatabaseUtil;
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
    DatabaseUtil dbUtil;
    
    public KurssihallintaService() {
        dbUtil = new DatabaseUtil(false);
        students = new StudentDao();
        courses = new CourseDao();
        lessons = new LessonDao(dbUtil);
        classrooms = new ClassroomDao();
        registrations = new RegistrationDao();
    }
    
    public void setDbUtil(DatabaseUtil dbUtil) {
        this.dbUtil = dbUtil;
    }
    
    public boolean addStudent(Student student) {
        try {
            students.setConnection(dbUtil.getConnection());
            students.add(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addCourse(Course course) {
        try {
            courses.setConnection(dbUtil.getConnection());
            courses.add(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean addRegistration(String courseName, String studentIdNum) {
        try {
            students.setConnection(dbUtil.getConnection());
            int studentId = students.getId(studentIdNum);
            courses.setConnection(dbUtil.getConnection());
            int courseId = courses.getId(courseName);
            registrations.setConnection(dbUtil.getConnection());
            registrations.add(courseId, studentId);
            courses.setConnection(dbUtil.getConnection());
            courses.incrementStudentCount(courseId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean addLesson(Lesson lesson) {
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessons.add(lesson);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean addClassroom(Classroom classroom) {
        try {
            classrooms.setConnection(dbUtil.getConnection());
            classrooms.add(classroom);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public ObservableList searchCourses(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            courses.setConnection(dbUtil.getConnection());
            courseList = courses.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return courseList;
    }
    
    public ObservableList searchStudents(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            students.setConnection(dbUtil.getConnection());
            studentList = students.search(searchWord);
        } catch (SQLException ex) {        
        }
        
        return studentList;
    }
    
    public ObservableList searchRegistrationsByStudentId(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            students.setConnection(dbUtil.getConnection());
            int studentId = students.getId(searchWord);
            registrations.setConnection(dbUtil.getConnection());
            courseList = registrations.searchRegistrationsByStudents(studentId);
        } catch (SQLException ex) { 
        }
        
        return courseList;
    }
    
    public ObservableList searchRegistrationsByCourseName(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            courses.setConnection(dbUtil.getConnection());
            int courseId = courses.getId(searchWord);
            registrations.setConnection(dbUtil.getConnection());
            studentList = registrations.searchRegistrationsByCourses(courseId);
        } catch (SQLException ex) { 
        }
        
        return studentList;
    }
    
    public ObservableList searchLessonsByCourseName(String searchWord) {
        ObservableList<Student> lessonList = FXCollections.observableArrayList();
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.search(searchWord);
        } catch (SQLException ex) { 
        }
        
        return lessonList;
    }
    
    public ObservableList<String> getClassrooms() {
        ObservableList<Classroom> classroomList = FXCollections.observableArrayList();
        ObservableList<String> classroomsString = FXCollections.observableArrayList();
        try {
            classrooms.setConnection(dbUtil.getConnection());
            classroomList = classrooms.getAll();
        } catch (SQLException ex) {
            return null;
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
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.getAll();
        } catch (SQLException ex) {        
        }
        
        return lessonList;
    }
    
    public ObservableList getLessonsFilteredByClassroom(String classroom) {
        ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
        ObservableList<Lesson> filteredLessonList = FXCollections.observableArrayList();
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.getAll();
        } catch (SQLException ex) {        
        }       
        for (Lesson l : lessonList) {
            if (l.getClassroom().equals(classroom)) {
                filteredLessonList.add(l);
            }
        }
        return filteredLessonList;
    }
    
    public ObservableList getLessonsFilteredByDate(LocalDate date) {
        ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
        ObservableList<Lesson> filteredLessonList = FXCollections.observableArrayList();
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.getAll();
        } catch (SQLException ex) {        
        }       
        for (Lesson l : lessonList) {
            if (l.getDate().equals(date)) {
                filteredLessonList.add(l);
            }
        }
        return filteredLessonList;
    }
    
    public ObservableList getLessonsFilteredByClassroomAndDate(String classroom, LocalDate date) {
        ObservableList<Lesson> filteredLessonList = FXCollections.observableArrayList();
        ObservableList<Lesson> lessonList = getLessonsFilteredByClassroom(classroom);

        for (Lesson l : lessonList) {
            if (l.getDate().equals(date)) {
                filteredLessonList.add(l);
            }
        }
        return filteredLessonList;
    }
    
    public boolean updateCourse(Course course) {
        try {
            courses.setConnection(dbUtil.getConnection());
            courses.update(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    public boolean updateStudent(Student student) {
        try {
            students.setConnection(dbUtil.getConnection());
            students.update(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
}
