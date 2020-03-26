/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import kurssihallinta.ui.MainScene;

/**
 *
 * @author okkokuisma
 */
public class Main {
    
    public static void main(String[] args) {
        Application.launch(MainScene.class);
    }

}
