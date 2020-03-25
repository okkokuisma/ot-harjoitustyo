/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author okkokuisma
 */
public class StudentsView {
    private boolean searchView;
    
    public StudentsView() {
        searchView = true;
    }
    
    public Parent getParent() {
        BorderPane studentsView = new BorderPane();
        
        // PANE FOR CONTROLS USED TO ADD NEW STUDENTS
        GridPane studentAddView = new GridPane();
        studentAddView.setVgap(10);
        studentAddView.setHgap(10);
        studentAddView.setAlignment(Pos.TOP_CENTER);
        
        studentAddView.add(new Label("First name:"), 0, 0);
        studentAddView.add(new TextField(), 1, 0);
        studentAddView.add(new Label("Surname:"), 0, 1);
        studentAddView.add(new TextField(), 1, 1);
        studentAddView.add(new Label("Personal ID number:"), 0, 2);
        studentAddView.add(new TextField(), 1, 2);
        studentAddView.add(new Label("Home address:"), 0, 3);
        studentAddView.add(new TextField(), 1, 3);
        studentAddView.add(new Label("ZIP code:"), 0, 4);
        studentAddView.add(new TextField(), 1, 4);
        studentAddView.add(new Label("City:"), 0, 5);
        studentAddView.add(new TextField(), 1, 5);
        studentAddView.add(new Label("Country:"), 0, 6);
        studentAddView.add(new TextField(), 1, 6);
        studentAddView.add(new Label("Email:"), 0, 7);
        studentAddView.add(new TextField(), 1, 7);
        
        studentAddView.add(new Button("Add to database"), 0, 8);
        Label message = new Label();
        studentAddView.add(message, 1, 8);
        
        // PANE FOR CONTROLS USED TO SEARCH FOR STUDENTS
        GridPane studentSearchView = new GridPane();
        studentSearchView.setVgap(15);
        studentSearchView.setAlignment(Pos.TOP_CENTER);
        
        TextField textField = new TextField("Search for students");
        Button searchButton = new Button("Search");
        
        studentSearchView.add(textField, 0, 0);
        studentSearchView.add(searchButton, 1, 0);
        
        //PANE FOR CONTROLS USED FOR NAVIGATING BETWEEN SEARCH AND ADD PANES
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.TOP_RIGHT);
        navigationBar.setSpacing(10);
        
        Button navigationButton = new Button("Add a new student");
        navigationBar.getChildren().add(navigationButton);
        
        navigationButton.setOnMouseClicked((event) -> { 
            if (searchView) {
                studentsView.setCenter(studentAddView);
                navigationButton.setText("Search for students");
                searchView = false;
            } else {
                studentsView.setCenter(studentSearchView);
                navigationButton.setText("Add a new student");
                searchView = true;
            }
        });
        
        studentsView.setTop(navigationBar);
        studentsView.setCenter(studentSearchView);
        
        return studentsView;
    }
}
