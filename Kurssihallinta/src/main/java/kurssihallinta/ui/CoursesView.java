/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import kurssihallinta.database.CourseDao;
import kurssihallinta.database.KurssihallintaDao;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.KurssihallintaService;

/**
 *
 * @author okkokuisma
 */
public class CoursesView {
    private TableView table;
    private TableViewController tableControl;
    private  boolean searchView;
    KurssihallintaDao db;
    KurssihallintaService service;
    
    public CoursesView() {
        tableControl = new TableViewController();
        searchView = true;
        this.db = new CourseDao();
        service = new KurssihallintaService();
    }
    
    public Parent getParent() {
        BorderPane coursesView = new BorderPane();
        
        // PANE FOR INDIVIDUAL COURSES
        GridPane individualCourse = new GridPane();
        Button returnButton = new Button("Back");
        Button lessonButton = new Button("Add/edit lessons");
        Label courseName = new Label();
        
        
        
        // PANE FOR CONTROLS USED TO ADD NEW COURSES
        
        GridPane courseAddView = new GridPane();
        courseAddView.setVgap(10);
        courseAddView.setHgap(10);
        courseAddView.setAlignment(Pos.CENTER);
        
        TextField nameTextfield = new TextField();
        courseAddView.addRow(0, new Label("Name:"), nameTextfield);        
        DatePicker startDate = new DatePicker();
        courseAddView.addRow(1, new Label("Starting day:"), startDate); 
        DatePicker endDate = new DatePicker();
        courseAddView.addRow(2, new Label("Ending date:"), endDate);
        TextField teacherTextfield = new TextField();
        courseAddView.addRow(3, new Label("Teacher:"), teacherTextfield);
        Spinner<Integer> maxStudentsSpinner = new Spinner(0, 60, 0);
        courseAddView.addRow(4, new Label("Maximum number of students:"), maxStudentsSpinner);
        
        Button addButton = new Button("Add to database");
        Label message = new Label();
        courseAddView.addRow(5, addButton, message);
        
        addButton.setOnMouseClicked((event) -> { 
            if (service.addCourse(nameTextfield.getText(), startDate.getValue(), endDate.getValue(), teacherTextfield.getText(), maxStudentsSpinner.getValue())) {
                nameTextfield.clear();
                teacherTextfield.clear();
                message.setText("Added successfully to database");
            } else {
                message.setText("Error: ");
            }       
        });
        
        // CONTROLS USED TO SEARCH FOR COURSES
        
        TextField searchTextfield = new TextField("Search by name");
        Button searchButton = new Button("Search");
        Button toCoursePage = new Button("Go to selected course page");     
        
        searchTextfield.setOnMouseClicked((event) -> {
            searchTextfield.clear(); 
        });
        
        searchTextfield.setOnKeyTyped((event) -> {          
            table = tableControl.getCourseTable(service.searchCourses(searchTextfield.getText()));
            coursesView.setCenter(table);
            coursesView.setBottom(toCoursePage);
        });
        
        //PANE FOR CONTROLS USED FOR NAVIGATING BETWEEN SEARCH AND ADD VIEWS
        
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.TOP_RIGHT);
        navigationBar.setSpacing(10);
        
        Button navigationButton = new Button("Add a new course");
        navigationButton.setAlignment(Pos.TOP_RIGHT);
        navigationBar.getChildren().addAll(searchTextfield, searchButton, navigationButton);
        
        navigationButton.setOnMouseClicked((event) -> { 
            if (searchView) {
                coursesView.setCenter(courseAddView);
                navigationButton.setText("Search for courses");
                searchTextfield.setVisible(false);
                searchButton.setVisible(false);
                searchView = false;
            } else {
                coursesView.setCenter(null);
                searchTextfield.setVisible(true);
                searchButton.setVisible(true);
                navigationButton.setText("Add a new course");
                searchView = true;
            }
        });
        
        coursesView.setTop(navigationBar);
        
        return coursesView; 
    }
}
