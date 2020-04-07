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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.KurssihallintaService;
import kurssihallinta.domain.Student;


/**
 *
 * @author okkokuisma
 */
public class KurssihallintaUi extends Application {
    private KurssihallintaService service;
    private Scene courseScene;
    private Scene regScene;
    private TableView table;
    private TableViewController tableControl;

    
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
            Logger.getLogger(KurssihallintaUi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        service = new KurssihallintaService();
//        tableControl = new TableViewController();
//        table = new TableView<>();
    }

    @Override
    public void start(Stage stage) {   
        service = new KurssihallintaService();
        tableControl = new TableViewController();
        table = new TableView<>();
        
        
        // COURSE SCENE
        BorderPane courseSceneCorePane = new BorderPane();
        BorderPane courseSceneInnerPane = new BorderPane();
        
        // Upper navigation panel for navigating between scenes
        HBox courseNavigationPanel = new HBox();
        courseNavigationPanel.setSpacing(15);

        Button coursesButton = new Button("Courses");
        Button regButton = new Button("Registrations");

        courseNavigationPanel.getChildren().addAll(coursesButton, regButton);
        
        coursesButton.setOnMouseClicked((event) -> {
            stage.setScene(courseScene);
        });
        
        regButton.setOnMouseClicked((event) -> {
            stage.setScene(regScene);
        });
        
        courseSceneCorePane.setTop(courseNavigationPanel);
        courseSceneCorePane.setCenter(courseSceneInnerPane);
        
        // Controls for searching courses
        HBox courseSceneUpperPanel = new HBox();
        courseSceneUpperPanel.setAlignment(Pos.CENTER);
        TextField courseSearch = new TextField("Search by name"); 
        
        courseSearch.setOnMouseClicked((event) -> {
            courseSearch.clear(); 
        });
        
        courseSearch.setOnKeyTyped((event) -> {          
            table = tableControl.getCourseTable(service.searchCourses(courseSearch.getText()));
            courseSceneInnerPane.setCenter(table);
        });      
        
        courseSceneUpperPanel.getChildren().add(courseSearch);
        courseSceneInnerPane.setTop(courseSceneUpperPanel);
        
        // Pane for adding courses
        
        GridPane courseAddNode = new GridPane();
        courseAddNode.setVgap(10);
        courseAddNode.setHgap(10);
        courseAddNode.setAlignment(Pos.CENTER);
        
        TextField nameTextfield = new TextField();
        courseAddNode.addRow(0, new Label("Name:"), nameTextfield);        
        DatePicker startDate = new DatePicker();
        courseAddNode.addRow(1, new Label("Starting day:"), startDate); 
        DatePicker endDate = new DatePicker();
        courseAddNode.addRow(2, new Label("Ending date:"), endDate);
        TextField teacherTextfield = new TextField();
        courseAddNode.addRow(3, new Label("Teacher:"), teacherTextfield);
        Spinner<Integer> maxStudentsSpinner = new Spinner(0, 60, 0);
        courseAddNode.addRow(4, new Label("Maximum number of students:"), maxStudentsSpinner);
        
        Button addButton = new Button("Add to database");
        Label message = new Label();
        courseAddNode.addRow(5, addButton, message);
        
        addButton.setOnMouseClicked((event) -> { 
            if (service.addCourse(nameTextfield.getText(), startDate.getValue(), endDate.getValue(), teacherTextfield.getText(), maxStudentsSpinner.getValue())) {
                nameTextfield.clear();
                teacherTextfield.clear();
                message.setText("Added successfully to database");
            } else {
                message.setText("Error: ");
            }       
        });
        
        // Bottom navigation panel for controls
        HBox courseSceneControls = new HBox();
        courseSceneControls.setPadding(new Insets(0, 15, 30, 15));
        Button addCourseButton = new Button("Add a new course");
        
        addCourseButton.setOnMouseClicked((event) -> { 
            courseSceneInnerPane.setCenter(courseAddNode);
        });
        
        courseSceneControls.getChildren().add(addCourseButton);
        courseSceneInnerPane.setBottom(courseSceneControls);
        
        courseScene = new Scene(courseSceneCorePane, 900, 700);
        
        // REGISTRATION SCENE
        BorderPane regSceneCorePane = new BorderPane();
        BorderPane regSceneInnerPane = new BorderPane();
        
        // Upper navigation panel for navigating between scenes
        HBox regNavigationPanel = new HBox();
        regNavigationPanel.setSpacing(15);

        Button courses = new Button("Courses");
        Button reg = new Button("Registrations");

        regNavigationPanel.getChildren().addAll(courses, reg);
        
        courses.setOnMouseClicked((event) -> {
            stage.setScene(courseScene);
        });
        
        reg.setOnMouseClicked((event) -> {
            stage.setScene(regScene);
        });
        
        regSceneCorePane.setTop(regNavigationPanel);
        regSceneCorePane.setCenter(regSceneInnerPane);
        
        // Controls for individual student view
        Button studentViewButton = new Button("Go to student page");
        studentViewButton.setVisible(false);
        
        studentViewButton.setOnMouseClicked((event) -> {
            Student student = (Student) table.getSelectionModel().getSelectedItem();
            regSceneInnerPane.setCenter(getIndividualStudentView(student));
            studentViewButton.setVisible(false);
        });
        
        // Controls for searching students
        HBox regSceneUpperPanel = new HBox();
        regSceneUpperPanel.setAlignment(Pos.CENTER);
        TextField studentSearch = new TextField("Search by name"); 
        
        studentSearch.setOnMouseClicked((event) -> {
            studentSearch.clear(); 
        });
        
        studentSearch.setOnKeyTyped((event) -> {          
            table = tableControl.getStudentTable(service.searchStudents(studentSearch.getText()));
            regSceneInnerPane.setCenter(table);
            studentViewButton.setVisible(true);
        });      
        
        regSceneUpperPanel.getChildren().add(studentSearch);
        regSceneInnerPane.setTop(regSceneUpperPanel);
        
        // Pane for adding new students
        GridPane studentAddView = new GridPane();
        studentAddView.setVgap(10);
        studentAddView.setHgap(10);
        studentAddView.setAlignment(Pos.CENTER);
        
        TextField firstNameTextfield = new TextField();
        studentAddView.addRow(0, new Label("First name:"), firstNameTextfield);
        TextField surnameTextfield = new TextField();
        studentAddView.addRow(1, new Label("Surname:"), surnameTextfield);
        TextField idTextfield = new TextField();
        studentAddView.addRow(2, new Label("Personal ID number:"), idTextfield);
        TextField addressTextfield = new TextField();
        studentAddView.addRow(3, new Label("Home address:"), addressTextfield);
        TextField zipTextfield = new TextField();
        studentAddView.addRow(4, new Label("ZIP code:"), zipTextfield);
        TextField cityTextfield = new TextField();
        studentAddView.addRow(5, new Label("City:"), cityTextfield);
        TextField countryTextfield = new TextField();
        studentAddView.addRow(6, new Label("Country:"), countryTextfield);
        TextField emailTextfield = new TextField();
        studentAddView.addRow(7, new Label("Email:"), emailTextfield);
        
        Label regSceneMessage = new Label();
        
        Button addStudentButton = new Button("Add to database");
        addStudentButton.setOnMouseClicked((event) -> {
            Student student = new Student(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
            service.addStudent(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
            regSceneInnerPane.setCenter(addRegistration(student));
        });

        studentAddView.addRow(8, addStudentButton, regSceneMessage);
        
        // Controls for adding registrations
        Button addStudent = new Button("Add a new student");
        addStudent.setOnMouseClicked((event) -> {
            regSceneInnerPane.setCenter(studentAddView);
        });
        
        Button selectStudent = new Button("Select student");
        selectStudent.setVisible(false);
        selectStudent.setOnMouseClicked((event) -> {
            if (table.getSelectionModel().getSelectedItem() instanceof Student) {
                regSceneInnerPane.setCenter(addRegistration((Student) table.getSelectionModel().getSelectedItem()));
                selectStudent.setVisible(false);
                studentViewButton.setVisible(false);
            }
        });
        
        Button chooseFromDatabase = new Button("Choose a student from database");
        chooseFromDatabase.setOnMouseClicked((event) -> {
            regSceneInnerPane.setCenter(new Label("Search for students."));
            selectStudent.setVisible(true);
        });
        
        // Selection view during registration
        GridPane optionView = new GridPane();
        optionView.setVgap(10);
        optionView.setAlignment(Pos.CENTER);
        optionView.addColumn(0, chooseFromDatabase, new Label("or"), addStudent);
        
        // Bottom navigation panel for controls
        HBox regSceneControls = new HBox();
        regSceneControls.setPadding(new Insets(0, 15, 30, 15));
        
        Button addRegButton = new Button("Add a new registration");
        
        addRegButton.setOnMouseClicked((event) -> { 
            regSceneInnerPane.setCenter(optionView);
            studentViewButton.setVisible(false);
        });
        
        regSceneControls.getChildren().addAll(addRegButton, selectStudent, studentViewButton);
        regSceneInnerPane.setBottom(regSceneControls);
        
        regScene = new Scene(regSceneCorePane, 900, 700);       
        
        // Initial settings
        stage.setScene(courseScene);

        stage.setMinWidth(900);
        stage.setMinHeight(700);
        stage.setTitle("Kurssihallinta");
        stage.show();
       
    }
    
    private Node addRegistration(Student student) {
        BorderPane regPane = new BorderPane();
        Button selectCourse = new Button("Select course");
        TextField searchField = new TextField("Search courses by name");
        Label message = new Label();
        regPane.setTop(searchField);
        
        searchField.setOnMouseClicked((event) -> {
            searchField.clear(); 
        });
        
        searchField.setOnKeyTyped((event) -> {          
            table = tableControl.getCourseTable(service.searchCourses(searchField.getText()));
            regPane.setCenter(table);
        });
        
        HBox regPaneControls = new HBox();
        regPaneControls.setPadding(new Insets(0, 15, 30, 15));
        regPaneControls.getChildren().addAll(selectCourse, message);
        regPane.setBottom(regPaneControls);
        
        selectCourse.setOnMouseClicked((event) -> { 
            Course course = (Course) table.getSelectionModel().getSelectedItem();
            if (service.addRegistration(course.getName(), student.getId())) {
                regPane.setCenter(null);
                regPane.setTop(null);
                message.setText("Added successfully to database");
            } else {
                message.setText("Error while adding to database");
            }
        });
        
        return regPane;
    }
    
    private Node getIndividualStudentView(Student student) {
        BorderPane indStudent = new BorderPane();

        GridPane studentInfo = new GridPane();

        studentInfo.addRow(0, new Label("Name:\t" + student.getFirstName() + " " + student.getSurname()), new Label("Address:\t" + student.getAddress()));
        studentInfo.addRow(1, new Label("Personal ID:\t" + student.getId()), new Label("City and ZIP code:\t" + student.getCity() + " " + student.getZipCode()));
        studentInfo.addRow(2, new Label("Email address:\t" + student.getEmail()), new Label("Country:\t" + student.getCountry()));
        studentInfo.setPadding(new Insets(50, 100, 10, 30));
        studentInfo.setHgap(100);

        indStudent.setTop(studentInfo);
        indStudent.setCenter(tableControl.getCourseTable(service.searchRegistrations(student.getId())));

        return indStudent;
    }
}
