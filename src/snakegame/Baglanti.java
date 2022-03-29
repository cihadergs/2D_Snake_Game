/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Baglanti {
    
    public static Connection connect() {
        Connection con = null;
        try{
            String url = "jdbc:sqlite:C:\\Users\\Cihad\\Documents\\NetBeansProjects\\SnakeGame\\src\\snakegame\\SnakeDB.db";
            con = DriverManager.getConnection(url);
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return con;
        }
    }
     Connection con;
        ResultSet rs;
        Statement st;
        
    
}
    


