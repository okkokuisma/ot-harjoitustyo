package kurssihallinta.database;


import java.sql.SQLException;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author okkokuisma
 */
public interface KurssihallintaDao<T, K> {
    void add(T object) throws SQLException;
    ObservableList search(K key) throws SQLException;
}