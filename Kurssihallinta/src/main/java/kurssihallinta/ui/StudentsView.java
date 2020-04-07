/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.KurssihallintaService;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class StudentsView {
    private TableViewController tableControl;
    private TableView table;
    Course selectedCourse;
    private boolean searchView;
    KurssihallintaService service;
    
    public StudentsView() {
        service = new KurssihallintaService();
        searchView = true;
        tableControl = new TableViewController();
    }
    
    public Parent getParent() {
        BorderPane studentsView = new BorderPane();      
        
        // PANE FOR ADDING NEW STUDENT INFO
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
        
        Button addStudentButton = new Button("Add to database");
        Button addExistingStudent = new Button("Select a student from database");
        Label message = new Label();
        studentAddView.addRow(8, addStudentButton, addExistingStudent, message);
        
        addStudentButton.setOnMouseClicked((event) -> {
            if (firstNameTextfield.getText().isBlank() | surnameTextfield.getText().isBlank() | idTextfield.getText().isBlank()) {
                message.setText("Fill required info");
            } else {
                service.addStudent(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
                
                if (selectedCourse != null) {
                    service.addRegistration(selectedCourse.getName());
                    selectedCourse = null;
                    message.setText("Added successfully to database");
                } else {
                    message.setText("No course selected");
                }
                firstNameTextfield.clear();
                surnameTextfield.clear();
                idTextfield.clear();
                addressTextfield.clear();
                zipTextfield.clear();
                cityTextfield.clear();
                countryTextfield.clear();
                emailTextfield.clear();
            }
        });
        
        // SEARCH CONTROLS      
        TextField searchTextfield = new TextField("Search by name");
        
        searchTextfield.setOnMouseClicked((event) -> {
            searchTextfield.clear();
        });
        
        searchTextfield.setOnKeyTyped((event) -> {
            if (searchView) {
                table = tableControl.getStudentTable(service.searchStudents(searchTextfield.getText()));
                studentsView.setCenter(table);         
            } else {
                table = tableControl.getCourseTable(service.searchCourses(searchTextfield.getText()));
                studentsView.setCenter(table); 
            }
            
        });
        
        
        //PANE FOR CONTROLS USED TO NAVIGATE BETWEEN SEARCH AND ADD PANES
        
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.TOP_RIGHT);
        navigationBar.setSpacing(10);
        
        Button navigationButton = new Button("Add a new registration");
        Button selectButton = new Button("Select");
        selectButton.setVisible(false);
        navigationBar.getChildren().addAll(selectButton, searchTextfield, navigationButton);
        
        navigationButton.setOnMouseClicked((event) -> { 
            if (searchView) {
                studentsView.setCenter(new Label("1. Search for courses\n2. Press select\n3. Fill student info"));
                navigationButton.setText("Search for students");
                selectButton.setVisible(true);
                searchView = false;
            } else {
                studentsView.setCenter(null);
                navigationButton.setText("Add a new student");
                selectButton.setVisible(false);
                searchView = true;
            }
        });
        
        selectButton.setOnMouseClicked((event) -> {
            searchView = true;
            selectedCourse = (Course) table.getSelectionModel().getSelectedItem();
            studentsView.setCenter(studentAddView);
        });
        
        studentsView.setTop(navigationBar);
        
        HBox bottomNavigationBar = new HBox();
        bottomNavigationBar.setPadding(new Insets(0, 15, 30, 15));
        Button studentViewButton = new Button("Go to student page");
        bottomNavigationBar.getChildren().add(studentViewButton);
        
        studentViewButton.setOnMouseClicked((event) -> {
            Student student = (Student) table.getSelectionModel().getSelectedItem();
            studentsView.setCenter(getIndividualStudentView(student));
        });
        
        studentsView.setBottom(bottomNavigationBar);
        
        return studentsView;
    }
    
    public Parent getIndividualStudentView(Student student) {

        BorderPane basis = new BorderPane();
        
        GridPane studentInfo = new GridPane();
        
        studentInfo.addRow(0, new Label("Name:\t" + student.getFirstName() + " " + student.getSurname()), new Label("Address:\t" + student.getAddress()));
        studentInfo.addRow(1, new Label("Personal ID:\t" + student.getId()), new Label("City and ZIP code:\t" + student.getCity() + " " + student.getZipCode()));
        studentInfo.addRow(2, new Label("Email address:\t" + student.getEmail()), new Label("Country:\t" + student.getCountry()));
        studentInfo.setPadding(new Insets(50, 100, 10, 30));
        studentInfo.setHgap(100);
        
        basis.setTop(studentInfo);
        basis.setCenter(tableControl.getCourseTable(service.searchRegistrations(student.getId())));
        
        return basis;
    }
}
