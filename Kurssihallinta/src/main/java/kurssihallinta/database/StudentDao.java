/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kurssihallinta.domain.Student;

/**
 *
 * @author okkokuisma
 */
public class StudentDao implements KurssihallintaDao<Student, String> {

    @Override
    public void add(Student student) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        PreparedStatement ps = db.prepareStatement("INSERT INTO Students (first_name,surname,id_number,address,zip,city,country,email) VALUES (?,?,?,?,?,?,?,?)");
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getSurname());
        ps.setString(3, student.getId());
        ps.setString(4, student.getAddress());
        ps.setString(5, student.getZipCode());
        ps.setString(6, student.getCity());
        ps.setString(7, student.getCountry());
        ps.setString(8, student.getEmail());
        ps.execute();
        db.close();
    }
    
    @Override
    public void update(Student student) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        PreparedStatement ps = db.prepareStatement("UPDATE Students SET "
                + "first_name = ?, "
                + "surname = ?, "
                + "address = ?, "
                + "zip = ?, "
                + "city = ?, "
                + "country = ?, "
                + "email = ? WHERE id_number = ?");
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getSurname());
        ps.setString(3, student.getAddress());
        ps.setString(4, student.getZipCode());
        ps.setString(5, student.getCity());
        ps.setString(6, student.getCountry());
        ps.setString(7, student.getEmail());
        ps.setString(8, student.getId());
        ps.execute();     
        db.close();
    }

    @Override
    public ObservableList search(String key) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:database.db");
        String searchWord = "%" + key + "%";
        PreparedStatement ps = db.prepareStatement("SELECT * FROM Students WHERE first_name LIKE ? OR surname LIKE ?");
        ps.setString(1, searchWord);
        ps.setString(2, searchWord);

        ResultSet queryResults = ps.executeQuery();
        
        // CREATE A LIST OBJECT FROM QUERY RESULTS
        ObservableList<Student> students = FXCollections.observableArrayList();
        while (queryResults.next()) {
            Student student = new Student(queryResults.getString(2), queryResults.getString(3), queryResults.getString(4), queryResults.getString(5), queryResults.getString(6), queryResults.getString(7), queryResults.getString(8), queryResults.getString(9));
            students.add(student);
        }
        
        return students;
    }
    
}
