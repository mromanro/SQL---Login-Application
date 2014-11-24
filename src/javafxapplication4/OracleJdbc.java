/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author MacAir
 */
public class OracleJdbc {
    
    public static boolean DEBUG = true;
    private final static String URL = "jdbc:oracle:thin:@cncsidb01.msudenver.edu:1521:DB01";
    private static Connection conn;
    
    private OracleJdbc() {}
    
    public static boolean checkLock(String username) {
        boolean locked = false;
        
        try {
            connect();
            
            String sql = "SELECT DISTINCT LOCKED_YN FROM USERINFO WHERE USERNAME = '"
                                + username + "'";
            
            PreparedStatement preStatement = conn.prepareStatement(sql);
            ResultSet set = preStatement.executeQuery();
            
            if(set != null) {
                set.next();
                if(set.getString("LOCKED_YN").equals("Y"))
                    locked = true;
                else
                    locked = false;
            }
            
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        return locked;
    }
    
    public static boolean validateLogin(String username, String password) {   
        boolean success = false;
        
        try{
            connect();
            
            String sql = "INSERT INTO Loginattempts(USERNAME, PASSWORD) "
                            + "VALUES('" + username + "', '" + password + "')";
            
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.executeQuery();
        
            sql = "SELECT DISTINCT SUCCESS_YN FROM Loginattempts WHERE USERNAME = '" + username
                       + "' AND PASSWORD = '" + password + "'";
            
            preStatement = conn.prepareStatement(sql);
            ResultSet set = preStatement.executeQuery();
            if(set != null) {
                set.next();
                if(set.getString("SUCCESS_YN").equals("Y"))
                    success = true;
                else
                    success =false;
                
                set.close();
            }
            //success = queryLogin(sql);
            
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        
        return success;
    }
    
    public static boolean unlockAccount(String adminUsername, String adminPassword,
                                    String accountUsername) {
        boolean success = false;
        
        try{
            connect();
            
            String sql = "INSERT INTO Unlocking(USERNAME_TO_UNLOCK, ADMIN_USERNAME, ADMIN_PASSWORD) "
                                    + "VALUES('" + accountUsername + "', '" + adminUsername + "', '" + adminPassword
                                    + "')";
       
            System.out.println(sql);
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.executeQuery();
            System.out.println("After");
            
            sql = "SELECT DISTINCT UNLOCK_SUCCESS_YN FROM Unlocking WHERE USERNAME_TO_UNLOCK"
                    + " = '" + accountUsername +"' AND ADMIN_USERNAME = '"
                    + adminUsername + "' AND ADMIN_PASSWORD = '" + adminPassword + "'";
            
            preStatement = conn.prepareStatement(sql);
            ResultSet set = preStatement.executeQuery();
            
            if(set != null){
                set.next();
                if(set.getString("UNLOCK_SUCCESS_YN").equals("Y")){
                    success = true;
                }
                else {
                    success = false;
                }
            }
            
          
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return success;
    }
    
    private static void disconnect() throws SQLException{
        if(conn != null) {
            conn.close();
        }
    }
    
    
    private static void connect() throws SQLException{
        Properties props = new Properties();
        props.setProperty("user", "Team4_Tuesday_Task");
        props.setProperty("password", "Team4");
        
        if(DEBUG) System.out.println(" DEBUG url=" + URL);
        if(DEBUG) System.out.println(" DEBUG Usernamre=" + props.getProperty("user") );
        if(DEBUG) System.out.println(" DEBUG Password=" + props.getProperty("password") );    
        
        //creating connection to Oracle database using JDBC
        conn = DriverManager.getConnection(URL,props);
    }
}
