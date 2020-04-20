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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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
        service = new KurssihallintaService();
        tableControl = new TableViewController();
        table = new TableView<>();
        
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
    }

    @Override
    public void start(Stage stage) {   
 
        // COURSE SCENE
        BorderPane courseSceneCorePane = new BorderPane();
        courseSceneCorePane.setPadding(new Insets(10, 10, 10, 10));
        BorderPane courseSceneInnerPane = new BorderPane();
        courseSceneInnerPane.setPadding(new Insets(0, 40, 20, 40));
        
        // Upper navigation panel for navigating between scenes
        HBox courseNavigationPanel = new HBox();
        courseNavigationPanel.setSpacing(10);

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
        
        // Controls for individual course view
        Button courseViewButton = new Button("Go to course page");
        courseViewButton.setVisible(false);
        
        courseViewButton.setOnMouseClicked((event) -> {
            Course course = (Course) table.getSelectionModel().getSelectedItem();
            courseSceneInnerPane.setCenter(getIndividualCourseView(course));
            courseViewButton.setVisible(false);
        });
        
        // Controls for searching courses
        HBox courseSceneUpperPanel = new HBox();
        courseSceneUpperPanel.setAlignment(Pos.CENTER);
        TextField courseSearch = new TextField("Search by name"); 
        courseSearch.setPrefSize(350, 30);
        
        courseSearch.setOnMouseClicked((event) -> {
            courseSearch.clear(); 
        });
        
        courseSearch.setOnKeyTyped((event) -> {          
            table = tableControl.getCourseTable(service.searchCourses(courseSearch.getText()));
            courseSceneInnerPane.setCenter(table);
            courseViewButton.setVisible(true);
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
            Course course = new Course(nameTextfield.getText(), startDate.getValue(), endDate.getValue(), teacherTextfield.getText(), 0, maxStudentsSpinner.getValue());
            if (service.addCourse(course)) {
                nameTextfield.clear();
                teacherTextfield.clear();
                message.setText("Added successfully to database");
            } else {
                message.setText("Error: ");
            }       
        });
        
        // Bottom navigation panel for controls
        HBox courseSceneControls = new HBox();
        Button addCourseButton = new Button("Add a new course");
        
        addCourseButton.setOnMouseClicked((event) -> { 
            courseSceneInnerPane.setCenter(courseAddNode);
        });
        
        courseSceneControls.getChildren().addAll(addCourseButton, courseViewButton);
        courseSceneInnerPane.setBottom(courseSceneControls);
        
        courseScene = new Scene(courseSceneCorePane, 900, 700);
        
        // REGISTRATION SCENE
        BorderPane regSceneCorePane = new BorderPane();
        BorderPane regSceneInnerPane = new BorderPane();
        
        // Upper navigation panel for navigating between scenes
        HBox regNavigationPanel = new HBox();
        regNavigationPanel.setSpacing(10);

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
        regSceneCorePane.setPadding(new Insets(10, 10, 10, 10));
        regSceneCorePane.setCenter(regSceneInnerPane);
        regSceneInnerPane.setPadding(new Insets(0, 40, 20, 40));
        
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
        studentSearch.setPrefSize(350, 30);
        
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
        Button addRegButton = new Button("Add a new registration");
        
        addRegButton.setOnMouseClicked((event) -> { 
            table.setItems(null);
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
        regPane.setPadding(new Insets(70, 0, 15, 0));
        Button selectCourse = new Button("Select course");
        TextField searchField = new TextField("Search courses by name");
        Label message = new Label();
        regPane.setTop(new HBox(searchField));
        
        searchField.setOnMouseClicked((event) -> {
            searchField.clear(); 
        });
        
        searchField.setOnKeyTyped((event) -> {          
            table = tableControl.getCourseTable(service.searchCourses(searchField.getText()));
            regPane.setCenter(table);
        });
        
        HBox regPaneControls = new HBox();
        regPaneControls.getChildren().addAll(selectCourse, message);
        regPane.setBottom(regPaneControls);
        
        selectCourse.setOnMouseClicked((event) -> { 
            Course course = (Course) table.getSelectionModel().getSelectedItem();
            if (course.getStudents() >= course.getMaxStudents()) {
                message.setText("Selected course is full.");
            } else {
                if (service.addRegistration(course.getName(), student.getId())) {
                    regPane.setCenter(null);
                    regPane.setTop(null);
                    message.setText("Added successfully to database");
                } else {
                    message.setText("Error while adding to database");
                }              
            }
        });
        
        return regPane;
    }
    
    private Node getIndividualStudentView(Student student) {
        BorderPane indStudent = new BorderPane();
        GridPane studentInfo = new GridPane();
        
        Button editButton = new Button("Edit student info");
        Font headerFont = new Font("Arial", 20);
        Label header = new Label(student.getFirstName() + " " + student.getSurname());
        Label tableHeader = new Label("Registrations: ");
        header.setFont(headerFont);
        tableHeader.setFont(headerFont);
        studentInfo.addRow(0, header, editButton);
        studentInfo.add(tableHeader, 0, 9);
        
        Label studentName = new Label(student.getFirstName() + " " + student.getSurname());
        studentInfo.addRow(2, new Label("Name:"), studentName);
        Label studentId = new Label(student.getId());
        studentInfo.addRow(3, new Label("Personal ID:"), studentId);
        Label studentAddress = new Label(student.getAddress());
        studentInfo.addRow(4, new Label("Address:"), studentAddress);
        Label studentCityZip = new Label(student.getCity() + " " + student.getZipCode());
        studentInfo.addRow(5, new Label("City and ZIP code:"), studentCityZip);
        Label studentCountry = new Label(student.getCountry());
        studentInfo.addRow(6, new Label("Country:"), studentCountry);
        Label studentEmail = new Label(student.getEmail());
        studentInfo.addRow(7, new Label("Email:"), studentEmail);
        
        Pane spring = new Pane();
        spring.setPrefSize(20, 0);
        studentInfo.add(spring, 0, 8);
        indStudent.setPadding(new Insets(10, 0, 0, 0));
        studentInfo.setHgap(10);
        studentInfo.setVgap(10);
        
        // Controls for updating student info
        TextField firstNameEdit = new TextField();
        TextField surnameEdit = new TextField();
        TextField addressEdit = new TextField();
        TextField cityEdit = new TextField();
        TextField zipEdit = new TextField();
        TextField countryEdit = new TextField();
        TextField emailEdit = new TextField();
        
        Button saveButton = new Button("Save changes");
        saveButton.setOnMouseClicked((event) -> {
            Student update = new Student(firstNameEdit.getText(), surnameEdit.getText(), student.getId(), addressEdit.getText(), cityEdit.getText(), zipEdit.getText(), countryEdit.getText(), emailEdit.getText());
            service.updateStudent(update);
            
            student.setFirstName(firstNameEdit.getText());
            studentInfo.getChildren().remove(firstNameEdit);
            student.setSurname(surnameEdit.getText());
            studentInfo.getChildren().remove(surnameEdit);
            studentName.setText(firstNameEdit.getText() + " " + surnameEdit.getText());
            studentInfo.add(studentName, 1, 2);
            
            student.setAddress(addressEdit.getText());
            studentAddress.setText(addressEdit.getText());
            studentInfo.getChildren().remove(addressEdit);
            studentInfo.add(studentAddress, 1, 4);
            
            student.setZipCode(zipEdit.getText());
            studentInfo.getChildren().remove(zipEdit);
            student.setCity(cityEdit.getText());
            studentInfo.getChildren().remove(cityEdit);
            studentCityZip.setText(cityEdit.getText() + " " + zipEdit.getText());
            studentInfo.add(studentCityZip, 1, 5);
            
            student.setCountry(countryEdit.getText());
            studentCountry.setText(countryEdit.getText());
            studentInfo.getChildren().remove(countryEdit);
            studentInfo.add(studentCountry, 1, 6);
            
            student.setEmail(emailEdit.getText());
            studentEmail.setText(emailEdit.getText());
            studentInfo.getChildren().remove(emailEdit);
            studentInfo.add(studentEmail, 1, 7);
            
            studentInfo.getChildren().remove(saveButton);
        });
        
        editButton.setOnMouseClicked((event) -> {
            firstNameEdit.setText(student.getFirstName());
            surnameEdit.setText(student.getSurname());
            studentInfo.getChildren().remove(studentName);
            studentInfo.add(firstNameEdit, 1, 2);
            studentInfo.add(surnameEdit, 2, 2);
            
            addressEdit.setText(student.getAddress());
            studentInfo.getChildren().remove(studentAddress);
            studentInfo.add(addressEdit, 1, 4);
            
            zipEdit.setText(student.getZipCode());
            cityEdit.setText(student.getCity());
            studentInfo.getChildren().remove(studentCityZip);
            studentInfo.add(cityEdit, 1, 5);
            studentInfo.add(zipEdit, 2, 5);
            
            countryEdit.setText(student.getCountry());
            studentInfo.getChildren().remove(studentCountry);
            studentInfo.add(countryEdit, 1, 6);
            
            emailEdit.setText(student.getEmail());
            studentInfo.getChildren().remove(studentEmail);
            studentInfo.add(emailEdit, 1, 7);
            
            studentInfo.add(saveButton, 2, 0);
        });

        indStudent.setTop(studentInfo);
        indStudent.setCenter(tableControl.getCourseTable(service.searchRegistrationsByStudentId(student.getId())));

        return indStudent;
    }
    
    private Node getIndividualCourseView(Course course) {
        BorderPane indCourse = new BorderPane();
        GridPane courseInfo = new GridPane();    
        
        Button editButton = new Button("Edit course info");
        Font headerFont = new Font("Arial", 20);
        Label header = new Label(course.getName());
        Label tableHeader = new Label("Registered students: ");
        header.setFont(headerFont);
        tableHeader.setFont(headerFont);
        courseInfo.addRow(0, header, editButton);
        courseInfo.add(tableHeader, 0, 6);
        
        Label startDate = new Label(course.getStartDate());
        courseInfo.addRow(2, new Label("Start date:"), startDate);
        Label endDate = new Label(course.getEndDate());
        courseInfo.addRow(3, new Label("End date:"), endDate);
        Label teacher = new Label(course.getTeacher());
        courseInfo.addRow(4, new Label("Teacher:"), teacher);
        Label students = new Label(course.getStudents() + " / " + course.getMaxStudents());
        courseInfo.addRow(5, new Label("Students registered / max:"), students);

//        Pane spring = new Pane();
//        spring.setPrefSize(0, 20);
//        courseInfo.add(spring, 0, 5);
        indCourse.setPadding(new Insets(20, 0, 0, 0));
        courseInfo.setVgap(15);
        courseInfo.setHgap(10);
          
        // Controls for updating course info
        DatePicker startDateEdit = new DatePicker();
        DatePicker endDateEdit = new DatePicker();
        TextField teacherEdit = new TextField();
        Spinner<Integer> maxStudentsEdit = new Spinner<>();
        SpinnerValueFactory spinnerValues = new IntegerSpinnerValueFactory(0, 50, course.getMaxStudents());
        
        Button saveButton = new Button("Save changes");
        saveButton.setOnMouseClicked((event) -> {
            Course update = new Course(course.getName(), startDateEdit.getValue(), endDateEdit.getValue(), teacherEdit.getText(), course.getStudents(), maxStudentsEdit.getValue());
            service.updateCourse(update);
            
            course.setStartDate(startDateEdit.getValue());
            startDate.setText(startDateEdit.getValue().format(DateTimeFormatter.ISO_DATE));
            courseInfo.getChildren().remove(startDateEdit);
            courseInfo.add(startDate, 1, 2);
            
            course.setEndDate(endDateEdit.getValue());
            endDate.setText(endDateEdit.getValue().format(DateTimeFormatter.ISO_DATE));
            courseInfo.getChildren().remove(endDateEdit);
            courseInfo.add(endDate, 1, 3);
            
            course.setTeacher(teacherEdit.getText());
            teacher.setText(teacherEdit.getText());
            courseInfo.getChildren().remove(teacherEdit);
            courseInfo.add(teacher, 1, 4);
            
            course.setMaxStudents(maxStudentsEdit.getValue());
            students.setText(course.getStudents() + " / " + maxStudentsEdit.getValue());
            courseInfo.getChildren().remove(maxStudentsEdit);
            courseInfo.add(students, 1, 5);
        });
        
        editButton.setOnMouseClicked((event) -> {
            startDateEdit.setValue(LocalDate.parse(course.getStartDate()));
            courseInfo.getChildren().remove(startDate);
            courseInfo.add(startDateEdit, 1, 2);
            
            endDateEdit.setValue(LocalDate.parse(course.getEndDate()));
            courseInfo.getChildren().remove(endDate);
            courseInfo.add(endDateEdit, 1, 3);
            
            teacherEdit.setText(course.getTeacher());
            courseInfo.getChildren().remove(teacher);
            courseInfo.add(teacherEdit, 1, 4);
            
            spinnerValues.setValue(course.getMaxStudents());
            maxStudentsEdit.setValueFactory(spinnerValues);
            courseInfo.getChildren().remove(students);
            courseInfo.add(maxStudentsEdit, 1, 5);
            
            courseInfo.add(saveButton, 2, 0);
        });
        
        indCourse.setTop(courseInfo);
        indCourse.setCenter(tableControl.getStudentTable(service.searchRegistrationsByCourseName(course.getName())));

        return indCourse;
    }
}
