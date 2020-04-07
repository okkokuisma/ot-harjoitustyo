/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 *
 * @author okkokuisma
 */
public class MainScene extends Application {
    
    @Override
    public void init() {
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement s = db.createStatement();
            ResultSet queryResult = s.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table'");
            if (queryResult.getInt(1) > 0) {
                return;
            }

            s.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER)");
            s.execute("CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT)");
            s.execute("CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE)");
            s.execute("CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER)");
            s.execute("CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT)");
        } catch (SQLException ex) {
            Logger.getLogger(MainScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage stage) {
        CoursesView coursesView = new CoursesView();
        StudentsView studentsView = new StudentsView();
        
        BorderPane corePane = new BorderPane();
        
        // Navigation panel
        HBox navigationPanel = new HBox();
        navigationPanel.setSpacing(15);
        
        // Buttons
        Button coursesButton = new Button("Courses");
        Button studentsButton = new Button("Students");
        Button calendarButton = new Button("Calendar");
        navigationPanel.getChildren().addAll(coursesButton, studentsButton, calendarButton);
        corePane.setTop(navigationPanel);
        
        coursesButton.setOnMouseClicked((event) -> {
            corePane.setCenter(coursesView.getParent());
        });
        
        studentsButton.setOnMouseClicked((event) -> {
            corePane.setCenter(studentsView.getParent());
        });
        
        corePane.setCenter(coursesView.getParent());
        Scene scene = new Scene(corePane);
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(700);
        stage.setTitle("Kurssihallinta");
        stage.show();
       
    }
    
}
