package kurssihallinta.dao;


import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Interface class for DAO objects.
 */
public interface KurssihallintaDao<T, K> {
    /**
    *
    * Adds the given object to database.
    * @param object Object to be added to database
    * @throws java.sql.SQLException
    */
    void add(T object) throws SQLException;
    
    /**
    *
    * Updates the corresponding row in database.
    * @param object Object with the modified data
    * @throws java.sql.SQLException
    */
    void update(T object) throws SQLException;
    
    /**
    *
    * Lists all rows which match with the given key.
    * @param key Search value
    * @throws java.sql.SQLException
    */
    ObservableList search(K key) throws SQLException;
    
    /**
    *
    * Retrieves the integer primary key of a corresponding row.
    * @param key Search value
    * @throws java.sql.SQLException
    */
    int getId(String key) throws SQLException;
    
    /**
    *
    * Retrieves the row with a corresponding integer primary key.
    * @param key Integer primary key
    * @throws java.sql.SQLException
    */
    T get(int key) throws SQLException;
    
    /**
    *
    * Lists all row from database.
    *
    * @throws java.sql.SQLException
    */
    ObservableList getAll() throws SQLException;
    void setConnection(Connection db) throws SQLException;
}
