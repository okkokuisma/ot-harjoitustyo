/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kurssihallinta.domain.Course;
import kurssihallinta.domain.Lesson;
import kurssihallinta.domain.Student;

/**
 *
 * A utility class used to control the contents of a TableView object.
 */
public class TableViewController {
    private TableView table;
    
    /**
    * Creates and returns a TableView object of Student objects.
    * 
    * @param    students    ObservableList of Student objects
    * 
    * @return TableView object of the Student objects
    */
    public TableView getStudentTable(ObservableList students) {
        table = new TableView<Student>();
        TableColumn<Student, String> firstNameCol = new TableColumn<>("First name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        TableColumn<Student, String> surnameCol = new TableColumn<>("Surname");
        surnameCol.setCellValueFactory(new PropertyValueFactory("surname"));
        TableColumn<Student, String> idCol = new TableColumn<>("Personal ID");
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn<Student, String> addressCol = new TableColumn<>("Home address");
        addressCol.setCellValueFactory(new PropertyValueFactory("address"));
        TableColumn<Student, String> zipCol = new TableColumn<>("ZIP code");
        zipCol.setCellValueFactory(new PropertyValueFactory("zipCode"));
        TableColumn<Student, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory("city"));
        TableColumn<Student, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory("country"));
        TableColumn<Student, String> emailCol = new TableColumn<>("Email address");
        emailCol.setCellValueFactory(new PropertyValueFactory("email"));
        table.getColumns().setAll(firstNameCol, surnameCol, idCol, addressCol, zipCol, cityCol, countryCol, emailCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxSize(800, 300);
        table.setItems(students);
        return table;
    }
    
    /**
    * Creates and returns a TableView object of Course objects.
    * 
    * @param    courses    ObservableList of Course objects
    * 
    * @return TableView object of the Course objects
    */
    public TableView getCourseTable(ObservableList courses) {
        table = new TableView<Course>();       
        TableColumn<Course, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Course, String> startDateCol = new TableColumn<>("Start date");
        startDateCol.setCellValueFactory(new PropertyValueFactory("startDate"));
        TableColumn<Course, String> endDateCol = new TableColumn<>("End date");
        endDateCol.setCellValueFactory(new PropertyValueFactory("endDate"));
        TableColumn<Course, String> teacherCol = new TableColumn<>("Teacher");
        teacherCol.setCellValueFactory(new PropertyValueFactory("teacher"));
        table.getColumns().setAll(nameCol, startDateCol, endDateCol, teacherCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxSize(800, 300);
        table.setItems(courses);
        return table;
    }
    
    /**
    * Creates and returns a TableView object of Lesson objects.
    * 
    * @param    lessons    ObservableList of Lesson objects
    * 
    * @return TableView object of the Lesson objects
    */
    public TableView getLessonTable(ObservableList lessons) {
        table = new TableView<Lesson>();       
        TableColumn<Lesson, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(new PropertyValueFactory("courseName"));
        TableColumn<Lesson, String> classroomCol = new TableColumn<>("Classroom");
        classroomCol.setCellValueFactory(new PropertyValueFactory("classroom"));
        TableColumn<Course, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        TableColumn<Course, String> startTimeCol = new TableColumn<>("Starts at");
        startTimeCol.setCellValueFactory(new PropertyValueFactory("startTime"));
        TableColumn<Course, String> endTimeCol = new TableColumn<>("Ends at");
        endTimeCol.setCellValueFactory(new PropertyValueFactory("endTime"));
        table.getColumns().setAll(courseCol, classroomCol, dateCol, startTimeCol, endTimeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxSize(800, 300);
        table.setItems(lessons);
        return table;
    }

}
