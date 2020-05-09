/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import kurssihallinta.domain.Classroom;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.KurssihallintaService;
import kurssihallinta.domain.Lesson;
import kurssihallinta.domain.Student;


/**
 *
 * The graphic UI.
 */
public class KurssihallintaUi extends Application {
    private KurssihallintaService service;
    private Scene courseScene;
    private Scene regScene;
    private Scene lessonsScene;
    private TableView table;
    private TableViewController tableControl;

    
    @Override
    public void init() {
        service = new KurssihallintaService();
        tableControl = new TableViewController();
        table = new TableView<>();       
    }

    @Override
    public void start(Stage stage) { 
        // Root panes
        BorderPane courseSceneCorePane = new BorderPane();
        BorderPane regSceneCorePane = new BorderPane();
        BorderPane lessonsSceneCorePane = new BorderPane();
        BorderPane lessonsSceneInnerPane = new BorderPane();
 
        // COURSE SCENE
        courseSceneCorePane.setPadding(new Insets(10, 10, 10, 10));
        BorderPane courseSceneInnerPane = new BorderPane();
        courseSceneInnerPane.setPadding(new Insets(0, 40, 20, 40));
        
        // Upper navigation panel for navigating between scenes
        HBox navigationPanel = new HBox();
        navigationPanel.setSpacing(10);

        Button coursesButton = new Button("Courses");
        Button regButton = new Button("Registrations");
        Button lessonsButton = new Button("Lessons");

        navigationPanel.getChildren().addAll(coursesButton, regButton, lessonsButton);
        coursesButton.setOnMouseClicked((event) -> {
            Pane root = (Pane) stage.getScene().getRoot();
            root.getChildren().remove(navigationPanel);
            courseSceneCorePane.setTop(navigationPanel);
            stage.setScene(courseScene);
        });
        
        regButton.setOnMouseClicked((event) -> {
            Pane root = (Pane) stage.getScene().getRoot();
            root.getChildren().remove(navigationPanel);
            regSceneCorePane.setTop(navigationPanel);
            stage.setScene(regScene);
        });
        lessonsButton.setOnMouseClicked((event) -> {
            Pane root = (Pane) stage.getScene().getRoot();
            root.getChildren().remove(navigationPanel);
            lessonsSceneCorePane.setTop(navigationPanel);
            stage.setScene(lessonsScene);
            table = tableControl.getLessonTable(service.getLessons());
            lessonsSceneInnerPane.setCenter(table);
        });
        
        courseSceneCorePane.setTop(navigationPanel);
        courseSceneCorePane.setCenter(courseSceneInnerPane);
        
        // Controls for individual course view
        Button indCourseViewButton = new Button("Go to course page");
        indCourseViewButton.setVisible(false);
        
        indCourseViewButton.setOnMouseClicked((event) -> {
            if (table.getSelectionModel().getSelectedItem() == null | !(table.getSelectionModel().getSelectedItem() instanceof Course)) {
                return;
            }
            Course course = (Course) table.getSelectionModel().getSelectedItem();
            courseSceneInnerPane.setCenter(getIndividualCourseView(course));
            indCourseViewButton.setVisible(false);
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
            indCourseViewButton.setVisible(true);
        });      
        
        courseSceneUpperPanel.getChildren().add(courseSearch);
        courseSceneInnerPane.setTop(courseSceneUpperPanel);
        
        // Pane for adding courses
        
        GridPane courseAddNode = new GridPane();
        courseAddNode.setVgap(10);
        courseAddNode.setHgap(10);
        courseAddNode.setAlignment(Pos.CENTER);
        
        TextField courseNameTextfield = new TextField();
        courseAddNode.addRow(0, new Label("Name:"), courseNameTextfield);        
        DatePicker courseStartDatePicker = new DatePicker();
        courseAddNode.addRow(1, new Label("Start date:"), courseStartDatePicker); 
        DatePicker courseEndDatePicker = new DatePicker();
        courseAddNode.addRow(2, new Label("End date:"), courseEndDatePicker);
        TextField courseTeacherTextfield = new TextField();
        courseAddNode.addRow(3, new Label("Teacher:"), courseTeacherTextfield);
        Spinner<Integer> maxStudentsSpinner = new Spinner(0, 60, 0);
        courseAddNode.addRow(4, new Label("Maximum number of students:"), maxStudentsSpinner);
        
        Button addCourseButton = new Button("Add to database");
        Label courseAddmessage = new Label();
        courseAddNode.addRow(5, addCourseButton, courseAddmessage);
        addCourseButton.setOnMouseClicked((event) -> { 
            if (courseNameTextfield.getText().isBlank() | courseStartDatePicker.getValue() == null | courseEndDatePicker.getValue() == null | courseTeacherTextfield.getText().isBlank()) {
                courseAddmessage.setText("Add required fields");
                return;
            }           
            Course course = new Course(courseNameTextfield.getText(), courseStartDatePicker.getValue(), courseEndDatePicker.getValue(), courseTeacherTextfield.getText(), 0, maxStudentsSpinner.getValue());
            if (service.addCourse(course)) {
                courseNameTextfield.clear();
                courseTeacherTextfield.clear();
                courseAddmessage.setText("Added successfully to database");
            } else {
                courseAddmessage.setText("Error");
            }       
        });
        
        // Bottom navigation panel for controls
        HBox courseSceneControls = new HBox();
        courseSceneControls.setSpacing(10);
        Button courseAddViewButton = new Button("Add a new course");
        
        courseAddViewButton.setOnMouseClicked((event) -> { 
            courseSceneInnerPane.setCenter(courseAddNode);
        });
        
        courseSceneControls.getChildren().addAll(courseAddViewButton, indCourseViewButton);
        courseSceneInnerPane.setBottom(courseSceneControls);
        
        courseScene = new Scene(courseSceneCorePane, 900, 700);
        
        // REGISTRATION SCENE
        BorderPane regSceneInnerPane = new BorderPane();

        regSceneCorePane.setPadding(new Insets(10, 10, 10, 10));
        regSceneCorePane.setCenter(regSceneInnerPane);
        regSceneInnerPane.setPadding(new Insets(0, 40, 20, 40));      
       
        // Controls for individual student view
        Button indStudentViewButton = new Button("Go to student page");
        indStudentViewButton.setVisible(false);
        
        indStudentViewButton.setOnMouseClicked((event) -> {
            if (table.getSelectionModel().getSelectedItem() == null | !(table.getSelectionModel().getSelectedItem() instanceof Student)) {
                return;
            }
            Student student = (Student) table.getSelectionModel().getSelectedItem();
            regSceneInnerPane.setCenter(getIndividualStudentView(student));
            indStudentViewButton.setVisible(false);
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
            indStudentViewButton.setVisible(true);
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
            if (firstNameTextfield.getText().isBlank() | surnameTextfield.getText().isBlank() | idTextfield.getText().isBlank()) {
                regSceneMessage.setText("Fill required fields");
                return;
            }
            Student student = new Student(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
            service.addStudent(student);
            regSceneInnerPane.setCenter(addRegistration(student));
        });
        
        studentAddView.addRow(8, addStudentButton, regSceneMessage);
        
        // Controls for adding registrations
        Button addStudent = new Button("Add a new student");
        addStudent.setOnMouseClicked((event) -> {
            regSceneInnerPane.setCenter(studentAddView);
        });
        
        Button selectStudentButton = new Button("Select student");
        selectStudentButton.setVisible(false);
        selectStudentButton.setOnMouseClicked((event) -> {
            if (table.getSelectionModel().getSelectedItem() instanceof Student) {
                regSceneInnerPane.setCenter(addRegistration((Student) table.getSelectionModel().getSelectedItem()));
                selectStudentButton.setVisible(false);
                indStudentViewButton.setVisible(false);
            }
        });
        
        Button chooseFromDatabaseButton = new Button("Choose a student from database");
        chooseFromDatabaseButton.setOnMouseClicked((event) -> {
            regSceneInnerPane.setCenter(new Label("Search for students."));
            selectStudentButton.setVisible(true);
        });
        
        // Selection view during registration
        GridPane optionView = new GridPane();
        optionView.setVgap(10);
        optionView.setAlignment(Pos.CENTER);
        optionView.addColumn(0, chooseFromDatabaseButton, new Label("or"), addStudent);
        
        // Bottom navigation panel for controls
        HBox regSceneControls = new HBox();
        regSceneControls.setSpacing(10);
        Button addRegButton = new Button("Add a new registration");
        
        addRegButton.setOnMouseClicked((event) -> { 
            table.setItems(null);
            regSceneInnerPane.setCenter(optionView);
            indStudentViewButton.setVisible(false);
        });
        
        regSceneControls.getChildren().addAll(addRegButton, indStudentViewButton, selectStudentButton);
        regSceneInnerPane.setBottom(regSceneControls);
        
        regScene = new Scene(regSceneCorePane, 900, 700);      
        
        // Lesson scene
        
        lessonsSceneCorePane.setPadding(new Insets(10, 10, 10, 10));
        lessonsSceneInnerPane.setPadding(new Insets(10, 40, 20, 40));   
        GridPane lessonsSceneControls = new GridPane();
        lessonsSceneControls.setHgap(10);
        lessonsSceneControls.setVgap(10);
        
        Button addClassroomButton = new Button("Add a new classroom");
        Button saveClassroom = new Button("Save");
        Button filterLessons = new Button("Filter lessons");
        TextField classroomTextField = new TextField();
        ChoiceBox<String> selectClassroom = new ChoiceBox();
        DatePicker selectDate = new DatePicker();
        selectClassroom.getItems().addAll(service.getClassrooms());
        lessonsSceneControls.addRow(0, addClassroomButton);
        lessonsSceneControls.addRow(1, selectClassroom, selectDate, filterLessons);
        
        saveClassroom.setOnMouseClicked((event) -> {
            if (!classroomTextField.getText().isBlank()) {
                Classroom classroom = new Classroom(classroomTextField.getText());
                service.addClassroom(classroom);            
            }
            
            lessonsSceneControls.getChildren().removeAll(classroomTextField, saveClassroom);
            lessonsSceneControls.addRow(0, addClassroomButton);
        });
        
        selectClassroom.setOnMouseClicked((event) -> {
            selectClassroom.getItems().clear();
            selectClassroom.getItems().addAll(service.getClassrooms());
        });
        
        addClassroomButton.setOnMouseClicked((event) -> {
            lessonsSceneControls.getChildren().remove(addClassroomButton);
            lessonsSceneControls.addRow(0, classroomTextField, saveClassroom);           
        });
        
        filterLessons.setOnMouseClicked((event) -> { 
            if (selectClassroom.getValue() != null & selectDate.getValue() != null) {
                lessonsSceneInnerPane.getChildren().remove(table);
                table = tableControl.getLessonTable(service.getLessonsFilteredByClassroomAndDate(selectClassroom.getValue(), selectDate.getValue()));
                lessonsSceneInnerPane.setCenter(table);
            } else if (selectDate.getValue() != null) {
                lessonsSceneInnerPane.getChildren().remove(table);
                table = tableControl.getLessonTable(service.getLessonsFilteredByDate(selectDate.getValue()));
                lessonsSceneInnerPane.setCenter(table);
            } else if (selectClassroom.getValue() != null) {
                lessonsSceneInnerPane.getChildren().remove(table);
                table = tableControl.getLessonTable(service.getLessonsFilteredByClassroom(selectClassroom.getValue()));
                lessonsSceneInnerPane.setCenter(table);
            }
        });
        
        lessonsSceneCorePane.setCenter(lessonsSceneInnerPane);
        
        lessonsSceneInnerPane.setTop(lessonsSceneControls);
        lessonsScene = new Scene(lessonsSceneCorePane, 900, 700);
        
        // Initial settings
        stage.setScene(courseScene);
        stage.setMinWidth(900);
        stage.setMinHeight(700);
        stage.setTitle("Kurssihallinta");
        stage.show(); 
    }
    
    /**
    * Creates a registration view based on a given Student object.
    * @param student A Student object chosen by the user
    * @return Node object of the registration view
    */
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
        regPaneControls.setSpacing(10);
        regPaneControls.getChildren().addAll(selectCourse, message);
        regPane.setBottom(regPaneControls);
        
        selectCourse.setOnMouseClicked((event) -> { 
            if (table.getSelectionModel().getSelectedItem() == null | !(table.getSelectionModel().getSelectedItem() instanceof Course)) {
                return;
            }
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
    
    /**
    * Creates an individual student view based on a given Student object.
    * @param student A Student object chosen by the user
    * @return Node object of the individual student view
    */
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
            if (firstNameEdit.getText().isBlank() | surnameEdit.getText().isBlank()) {
                return;
            }
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
    
    /**
    * Creates an individual course view based on a given Course object.
    * @param course A Course object chosen by the user
    * @return Node object of the individual course view
    */
    private Node getIndividualCourseView(Course course) {
        BorderPane indCourse = new BorderPane();
        GridPane courseInfo = new GridPane();
        BorderPane lessonPane = new BorderPane();
        GridPane lessonInfo = new GridPane();
        
        Button editButton = new Button("Edit course info");
        Button lessonButton = new Button("Edit lessons");
        Font headerFont = new Font("Arial", 20);
        Label header = new Label(course.getName());
        Label tableHeader = new Label("Registered students: ");
        header.setFont(headerFont);
        tableHeader.setFont(headerFont);
        courseInfo.addRow(0, header, editButton, lessonButton);
        courseInfo.add(tableHeader, 0, 6);
        
        Label startDate = new Label(course.getStartDate());
        courseInfo.addRow(2, new Label("Start date:"), startDate);
        Label endDate = new Label(course.getEndDate());
        courseInfo.addRow(3, new Label("End date:"), endDate);
        Label teacher = new Label(course.getTeacher());
        courseInfo.addRow(4, new Label("Teacher:"), teacher);
        Label students = new Label(course.getStudents() + " / " + course.getMaxStudents());
        courseInfo.addRow(5, new Label("Students registered / max:"), students);

        indCourse.setPadding(new Insets(20, 0, 0, 0));
        courseInfo.setVgap(15);
        courseInfo.setHgap(10);
          
        // Controls for updating course info
        DatePicker startDateEdit = new DatePicker();
        DatePicker endDateEdit = new DatePicker();
        TextField teacherEdit = new TextField();
        Spinner<Integer> maxStudentsEdit = new Spinner<>();
        SpinnerValueFactory spinnerValues = new IntegerSpinnerValueFactory(0, 50, course.getMaxStudents());
        
        // Controls for adding lessons
        Label errorMessage = new Label("Fill required fields");
        errorMessage.setVisible(false);
        Spinner<Integer> startHoursSpinner = new Spinner(5, 23, 12);
        Spinner<Integer> startMinsSpinner = new Spinner(0, 45, 0, 15);
        Spinner<Integer> endHoursSpinner = new Spinner(5, 23, 12);
        Spinner<Integer> endMinsSpinner = new Spinner(0, 45, 0, 15);
        DatePicker lessonDatePicker = new DatePicker();
        lessonDatePicker.setMaxWidth(100);
        startHoursSpinner.setMaxWidth(100);
        startMinsSpinner.setMaxWidth(100);
        endHoursSpinner.setMaxWidth(100);
        endMinsSpinner.setMaxWidth(100);
        
        ChoiceBox<String> classrooms = new ChoiceBox();
        classrooms.getItems().addAll(service.getClassrooms());
        
        Button addLessonButton = new Button("Add lesson");
        addLessonButton.setOnMouseClicked((event) -> {
            LocalTime startTime = LocalTime.of(startHoursSpinner.getValue(), startMinsSpinner.getValue());
            LocalTime endTime = LocalTime.of(endHoursSpinner.getValue(), endMinsSpinner.getValue());
            if (classrooms.getValue() == null | lessonDatePicker.getValue() == null | startTime == null | endTime == null) {
                errorMessage.setVisible(true);
                return;
            }
            Lesson lesson = new Lesson(course, classrooms.getValue(), lessonDatePicker.getValue(), startTime, endTime);
            service.addLesson(lesson);
            table.getItems().add(lesson);
            table.refresh();
            errorMessage.setVisible(false);
        });
        
        lessonInfo.addRow(1, new Label("Classroom:"), classrooms);
        lessonInfo.addRow(2, new Label("Date:"), lessonDatePicker);
        lessonInfo.addRow(3, new Label("Starts at:"), startHoursSpinner, new Label(":"), startMinsSpinner);
        lessonInfo.addRow(4, new Label("Ends at:"), endHoursSpinner, new Label(":"), endMinsSpinner);
        lessonInfo.addRow(5, addLessonButton, errorMessage);
        lessonInfo.setVgap(5);
        lessonInfo.setHgap(5);
        lessonInfo.setPadding(new Insets(0, 0, 10, 0));
        Scene lessonScene = new Scene(lessonPane);
        Stage lessonEditWindow = new Stage();
        lessonEditWindow.initModality(Modality.WINDOW_MODAL);
        
        lessonButton.setOnMouseClicked((event) -> {
            table = tableControl.getLessonTable(service.searchLessonsByCourseName(course.getName()));
            lessonPane.setCenter(table);
            lessonPane.setTop(lessonInfo);
            lessonEditWindow.setScene(lessonScene);
            lessonEditWindow.show();
        });
        
        Button saveButton = new Button("Save changes");
        saveButton.setOnMouseClicked((event) -> {
            if (startDateEdit.getValue() == null | endDateEdit.getValue() == null | teacherEdit.getText().isBlank()) {
                return;
            }
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
