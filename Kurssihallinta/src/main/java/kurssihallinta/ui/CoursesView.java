/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import java.util.Calendar;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author okkokuisma
 */
public class CoursesView {
    private  boolean searchView;
    private Calendar calendar;
    
    public CoursesView() {
        searchView = true;
        calendar = Calendar.getInstance();
    }
    
    public Parent getParent() {
        BorderPane coursesView = new BorderPane();
        
        // PANE FOR CONTROLS USED TO ADD NEW COURSES
        GridPane courseAddView = new GridPane();
        courseAddView.setVgap(10);
        courseAddView.setHgap(10);
        courseAddView.setAlignment(Pos.TOP_CENTER);
        
        courseAddView.addRow(0, new Label("Name:"), new TextField());        
        DatePicker startDate = new DatePicker();
        courseAddView.addRow(1, new Label("Starting day:"), startDate); 
        DatePicker endDate = new DatePicker();
        courseAddView.addRow(2, new Label("Ending date:"), endDate);
        courseAddView.addRow(3, new Label("Teacher:"), new TextField());
        
        courseAddView.add(new Button("Add to database"), 0, 4);
        Label message = new Label();
        courseAddView.add(message, 1, 4);
        
        // PANE FOR CONTROLS USED TO SEARCH FOR COURSES
        GridPane courseSearchView = new GridPane();
        courseSearchView.setVgap(15);
        courseSearchView.setAlignment(Pos.TOP_CENTER);
        
        TextField textField = new TextField("Search for students");
        Button searchButton = new Button("Search");
        
        courseSearchView.add(textField, 0, 0);
        courseSearchView.add(searchButton, 1, 0);
        
        //PANE FOR CONTROLS USED FOR NAVIGATING BETWEEN SEARCH AND ADD PANES
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.TOP_RIGHT);
        navigationBar.setSpacing(10);
        
        Button navigationButton = new Button("Add a new course");
        navigationBar.getChildren().add(navigationButton);
        
        navigationButton.setOnMouseClicked((event) -> { 
            if (searchView) {
                coursesView.setCenter(courseAddView);
                navigationButton.setText("Search for courses");
                searchView = false;
            } else {
                coursesView.setCenter(courseSearchView);
                navigationButton.setText("Add a new course");
                searchView = true;
            }
        });
        
        coursesView.setTop(navigationBar);
        coursesView.setCenter(courseSearchView);
        
        return coursesView;
    }
}
