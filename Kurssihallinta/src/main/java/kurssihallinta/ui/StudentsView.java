/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import kurssihallinta.database.Database;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class StudentsView {
    final TableView<Student> tableStudents;
    final TableView<Course> tableCourses;
    Course selectedCourse;
    private boolean searchView;
    Database db;
    
    public StudentsView(Database db) {
        searchView = true;
        this.db = db;
        
        // INITIALIZE STUDENT TABLE
        tableStudents = new TableView<>();
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
        
        //INITIALIZE COURSE TABLE
        tableStudents.getColumns().setAll(firstNameCol, surnameCol, idCol, addressCol, zipCol, cityCol, countryCol, emailCol);
        tableCourses = new TableView<>();
        TableColumn<Course,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Course,String> startDateCol = new TableColumn<>("Start date");
        startDateCol.setCellValueFactory(new PropertyValueFactory("startDate"));
        TableColumn<Course,String> endDateCol = new TableColumn<>("End date");
        endDateCol.setCellValueFactory(new PropertyValueFactory("endDate"));
        TableColumn<Course,String> teacherCol = new TableColumn<>("Teacher");
        teacherCol.setCellValueFactory(new PropertyValueFactory("teacher"));
        tableCourses.getColumns().setAll(nameCol, startDateCol, endDateCol, teacherCol);     
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
                try {
                    db.addStudent(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
                    db.addRegistration(selectedCourse.getDbId());
                    firstNameTextfield.clear();
                    surnameTextfield.clear();
                    idTextfield.clear();
                    addressTextfield.clear();
                    zipTextfield.clear();
                    cityTextfield.clear();
                    countryTextfield.clear();
                    emailTextfield.clear();
                    message.setText("Added successfully to database");
                } catch (SQLException ex) {
                    message.setText("Error: ");
                }
            }
        });
        
        // SEARCH CONTROLS      
        TextField searchTextfield = new TextField("Search by name");
        
        searchTextfield.setOnMouseClicked((event) -> {
            searchTextfield.clear();
        });
        
        searchTextfield.setOnKeyTyped((event) -> {
            if (searchView) {
                try {
                    tableStudents.setItems(db.searchStudents(searchTextfield.getText()));
                    tableStudents.setMaxSize(800, 300);
                    studentsView.setCenter(tableStudents);
                } catch (SQLException ex) {
                    Logger.getLogger(StudentsView.class.getName()).log(Level.SEVERE, null, ex);
                }         
            } else {
                try {
                    tableCourses.setItems(db.searchCourses(searchTextfield.getText()));
                    tableCourses.setMaxSize(400, 200);
                    studentsView.setCenter(tableCourses);
                } catch (SQLException ex) {
                    Logger.getLogger(StudentsView.class.getName()).log(Level.SEVERE, null, ex);
                } 
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
            selectedCourse = tableCourses.getSelectionModel().getSelectedItem();
            studentsView.setCenter(studentAddView);
        });
        
        studentsView.setTop(navigationBar);
        
        return studentsView;
    }
}
