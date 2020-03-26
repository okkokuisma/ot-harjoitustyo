/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kurssihallinta.database.Database;


/**
 *
 * @author okkokuisma
 */
public class MainScene extends Application {
    private Database db;
    
    @Override
    public void init() {
        db = new Database();
        db.connect();
    }

    @Override
    public void start(Stage stage) {
        CoursesView coursesView = new CoursesView(db);
        StudentsView studentsView = new StudentsView(db);
        
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
