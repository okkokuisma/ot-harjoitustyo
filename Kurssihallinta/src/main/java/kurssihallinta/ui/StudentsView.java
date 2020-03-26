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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import kurssihallinta.database.Database;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class StudentsView {
    private boolean searchView;
    Database db;
    
    public StudentsView(Database db) {
        searchView = true;
        this.db = db;
    }
    
    public Parent getParent() {
        BorderPane studentsView = new BorderPane();
        
        // PANE FOR CONTROLS USED TO ADD NEW STUDENTS
        GridPane studentAddView = new GridPane();
        studentAddView.setVgap(10);
        studentAddView.setHgap(10);
        studentAddView.setAlignment(Pos.TOP_CENTER);
        
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
        
        Button addButton = new Button("Add to database");
        Label message = new Label();
        studentAddView.addRow(8, addButton, message);
        
        addButton.setOnMouseClicked((event) -> {
            try {
                db.addStudent(firstNameTextfield.getText(), surnameTextfield.getText(), idTextfield.getText(), addressTextfield.getText(), zipTextfield.getText(), cityTextfield.getText(), countryTextfield.getText(), emailTextfield.getText());
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
        });
        
        // PANE FOR CONTROLS USED TO SEARCH FOR STUDENTS
        
        TextField searchTextfield = new TextField("Search by name");
        Button searchButton = new Button("Search");
        
        searchTextfield.setOnMouseClicked((event) -> {
            searchTextfield.clear();
        });
        
        searchButton.setOnMouseClicked((event) -> {
            TableView<Student> table;
            try {
                table = db.searchStudents(searchTextfield.getText());
                studentsView.setCenter(table);
            } catch (SQLException ex) {
                Logger.getLogger(StudentsView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //PANE FOR CONTROLS USED FOR NAVIGATING BETWEEN SEARCH AND ADD PANES
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.TOP_RIGHT);
        navigationBar.setSpacing(10);
        
        Button navigationButton = new Button("Add a new student");
        navigationBar.getChildren().addAll(searchTextfield, searchButton, navigationButton);
        
        navigationButton.setOnMouseClicked((event) -> { 
            if (searchView) {
                studentsView.setCenter(studentAddView);
                navigationButton.setText("Search for students");
                searchTextfield.setVisible(false);
                searchButton.setVisible(false);
                searchView = false;
            } else {
                studentsView.setCenter(null);
                searchTextfield.setVisible(true);
                searchButton.setVisible(true);
                navigationButton.setText("Add a new student");
                searchView = true;
            }
        });
        
        studentsView.setTop(navigationBar);
        
        return studentsView;
    }
}
