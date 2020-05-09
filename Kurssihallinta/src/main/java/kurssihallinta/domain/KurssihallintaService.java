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
 * Service class that acts as a layer between the UI and DAO objects. 
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
    
    /**
    *
    * Setter method used primarily to configure tests. 
    * @param dbUtil DatabaseUtil object which is connected to a test database
    */
    public void setDbUtil(DatabaseUtil dbUtil) {
        this.dbUtil = dbUtil;
        lessons = new LessonDao(dbUtil);
    }
    
    /**
    *
    * Calls StudentDao's add method. 
    * @param student A Student object to be added to the database
    * @return true if student was added successfully
    */
    public boolean addStudent(Student student) {
        try {
            students.setConnection(dbUtil.getConnection());
            students.add(student);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls CourseDao's add method. 
    * @param course A Course object to be added to the database
    * @return true if course was added successfully
    */
    public boolean addCourse(Course course) {
        try {
            courses.setConnection(dbUtil.getConnection());
            courses.add(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls StudentDao and CourseDao's getId methods to retrieve ids corresponding with given course name and student id number and then calls RegistrationDao's add method. Finally increments the student count of the corresponding course. 
    * @param courseName The name of a course
    * @param studentIdNum The personal id number of a student
    * @return true if course was added successfully
    */
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
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls LessonDao's add method. 
    * @param lesson A Lesson object to be added to the database
    * @return true if lesson was added successfully
    */
    public boolean addLesson(Lesson lesson) {
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessons.add(lesson);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls ClassroomDao's add method. 
    * @param classroom A Classroom object to be added to the database
    * @return true if classroom was added successfully
    */
    public boolean addClassroom(Classroom classroom) {
        try {
            classrooms.setConnection(dbUtil.getConnection());
            classrooms.add(classroom);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls CourseDao's search method. 
    * @param searchWord A search word given by user
    * @return ObservableList of Course objects if courses were found, an empty list if not
    */
    public ObservableList searchCourses(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            courses.setConnection(dbUtil.getConnection());
            courseList = courses.search(searchWord);
        } catch (SQLException ex) {       
            return courseList;
        }
        
        return courseList;
    }
    
    /**
    *
    * Calls StudentDao's search method. 
    * @param searchWord A search word given by user
    * @return ObservableList of Student objects if courses were found, an empty list if not
    */
    public ObservableList searchStudents(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            students.setConnection(dbUtil.getConnection());
            studentList = students.search(searchWord);
        } catch (SQLException ex) {    
            return studentList;
        }
        
        return studentList;
    }
    
    /**
    *
    * Calls StudentDao's getId method to retrieve the corresponding integer primary key value and then calls RegistrationDao's searchRegistrationsByStudents method to retrieve the courses where the corresponding student has been registered into. 
    * @param searchWord A search word given by user
    * @return ObservableList of Course objects if courses were found, an empty list if not
    */
    public ObservableList searchRegistrationsByStudentId(String searchWord) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            students.setConnection(dbUtil.getConnection());
            int studentId = students.getId(searchWord);
            registrations.setConnection(dbUtil.getConnection());
            courseList = registrations.searchRegistrationsByStudents(studentId);
        } catch (SQLException ex) { 
            return courseList;
        }
        
        return courseList;
    }
    
    /**
    *
    * Calls CourseDao's getId method to retrieve the corresponding integer primary key value and then calls RegistrationDao's searchRegistrationsByCourses method to retrieve the students that have been registered into the corresponding course. 
    * @param searchWord A search word given by user
    * @return ObservableList of Course objects if courses were found, an empty list if not
    */
    public ObservableList searchRegistrationsByCourseName(String searchWord) {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try {
            courses.setConnection(dbUtil.getConnection());
            int courseId = courses.getId(searchWord);
            registrations.setConnection(dbUtil.getConnection());
            studentList = registrations.searchRegistrationsByCourses(courseId);
        } catch (SQLException ex) { 
            return studentList;
        }
        
        return studentList;
    }
    
    /**
    *
    * Calls LessonDao's search method. 
    * @param courseName The name of a course
    * @return ObservableList of Lesson objects if lessons were found for the corresponding course, an empty list if not
    */
    public ObservableList searchLessonsByCourseName(String courseName) {
        ObservableList<Student> lessonList = FXCollections.observableArrayList();
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.search(courseName);
        } catch (SQLException ex) { 
            return lessonList;
        }
        
        return lessonList;
    }
    
    /**
    *
    * Calls ClassroomDao's getAll method to retrieve all classrooms from the database and returns an ObservableList object of their names. 
    * 
    * @return ObservableList of String objects if classrooms were found, an empty list if not
    */
    public ObservableList<String> getClassrooms() {
        ObservableList<Classroom> classroomList = null;
        ObservableList<String> classroomsString = FXCollections.observableArrayList();
        try {
            classrooms.setConnection(dbUtil.getConnection());
            classroomList = classrooms.getAll();
        } catch (SQLException ex) {
            return classroomsString;
        }
        
        for (Classroom c : classroomList) {
            String name = c.getName();
            classroomsString.add(name);
        }
        return classroomsString;
    }
    
    /**
    *
    * Calls LessonDao's getAll method to retrieve all lessons from the database.
    * 
    * @return ObservableList of Lesson objects if lessons were found, an empty list if not
    */
    public ObservableList getLessons() {
        ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
        try {
            lessons.setConnection(dbUtil.getConnection());
            lessonList = lessons.getAll();
        } catch (SQLException ex) {    
            return lessonList;
        }
        
        return lessonList;
    }
    
    /**
    *
    * Retrieves all lessons from the database and filters them by classroom.
    * 
    * @param classroom Classroom name given by the user
    * @return ObservableList of Lesson objects with a given classroom value if any were found, an empty list if not
    */
    public ObservableList getLessonsFilteredByClassroom(String classroom) {
        ObservableList<Lesson> lessonList = getLessons();
        ObservableList<Lesson> filteredLessonList = FXCollections.observableArrayList();
   
        for (Lesson l : lessonList) {
            if (l.getClassroom().equals(classroom)) {
                filteredLessonList.add(l);
            }
        }
        return filteredLessonList;
    }
    
    /**
    *
    * Retrieves all lessons from the database and filters them by date.
    * 
    * @param date Date given by the user
    * @return ObservableList of Lesson objects at a given date if any were found, an empty list if not
    */
    public ObservableList getLessonsFilteredByDate(LocalDate date) {
        ObservableList<Lesson> lessonList = getLessons();
        ObservableList<Lesson> filteredLessonList = FXCollections.observableArrayList();

        for (Lesson l : lessonList) {
            if (l.getDate().equals(date)) {
                filteredLessonList.add(l);
            }
        }
        return filteredLessonList;
    }
    
    /**
    *
    * Retrieves all lessons from the database and filters them by classroom and date.
    * 
    * @param classroom Classroom name given by the user
    * @param date Date given by the user
    * @return ObservableList of Lesson objects with a given classroom value at a given date if any were found, an empty list if not
    */
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
    
    /**
    *
    * Calls CourseDao's update method. 
    * @param course A Course object with the modified data
    * @return true if the corresponding row was updated successfully
    */
    public boolean updateCourse(Course course) {
        try {
            courses.setConnection(dbUtil.getConnection());
            courses.update(course);
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
    *
    * Calls CourseDao's update method. 
    * @param student A Student object with the modified data
    * @return true if the corresponding row was updated successfully
    */
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
