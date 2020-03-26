/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Student;

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
    
    public boolean addCourse(String name, String startDate, String endDate, String teacher) throws SQLException {    
        PreparedStatement ps = db.prepareStatement("INSERT INTO Courses (name,startdate,enddate,teacher) VALUES (?,?,?,?)");
        ps.setString(1, name);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.setString(4, teacher);
        ps.execute();
        return true;
    }
    
    public TableView searchCourses(String searchWord) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Courses WHERE name LIKE '%" + searchWord + "%'");

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A TABLEVIEW OBJECT FROM QUERY RESULTS
        TableView<Course> table = new TableView<>();
        ObservableList<Course> courses = FXCollections.observableArrayList();
        while (queryResults.next()) {
            LocalDate startDate = LocalDate.parse(queryResults.getString(3));
            LocalDate endDate = LocalDate.parse(queryResults.getString(4));
            Course course = new Course(queryResults.getString(2), startDate, endDate, queryResults.getString(5));
            courses.add(course);
        }
        table.setItems(courses);
        
        TableColumn<Course,String> nameCol = new TableColumn<Course,String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Course,String> startDateCol = new TableColumn<Course,String>("Start date");
        startDateCol.setCellValueFactory(new PropertyValueFactory("startDate"));
        TableColumn<Course,String> endDateCol = new TableColumn<Course,String>("End date");
        endDateCol.setCellValueFactory(new PropertyValueFactory("endDate"));
        TableColumn<Course,String> teacherCol = new TableColumn<Course,String>("Teacher");
        teacherCol.setCellValueFactory(new PropertyValueFactory("teacher"));
        table.getColumns().setAll(nameCol, startDateCol, endDateCol, teacherCol);
        
        return table;
    }
    
    public TableView searchStudents(String searchWord) throws SQLException {
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE first_name LIKE '%" + searchWord + "%' OR surname LIKE '%" + searchWord + "%'");

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A TABLEVIEW OBJECT FROM QUERY RESULTS
        TableView<Student> table = new TableView<>();
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        table.setItems(students);
        
        TableColumn<Student,String> firstNameCol = new TableColumn<>("First name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        TableColumn<Student,String> surnameCol = new TableColumn<>("Surname");
        surnameCol.setCellValueFactory(new PropertyValueFactory("surname"));
        TableColumn<Student,String> idCol = new TableColumn<>("Personal ID");
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn<Student,String> addressCol = new TableColumn<>("Home address");
        addressCol.setCellValueFactory(new PropertyValueFactory("address"));
        TableColumn<Student,String> zipCol = new TableColumn<>("ZIP code");
        zipCol.setCellValueFactory(new PropertyValueFactory("zipCode"));
        TableColumn<Student,String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory("city"));
        TableColumn<Student,String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory("country"));
        TableColumn<Student,String> emailCol = new TableColumn<>("Email address");
        emailCol.setCellValueFactory(new PropertyValueFactory("email"));
        table.getColumns().setAll(firstNameCol, surnameCol, idCol, addressCol, zipCol, cityCol, countryCol, emailCol);
        
        return table;
    }
}
