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
 *  Class that connects to the Team4 database.
 */
public class OracleJdbc {

    private final static String URL = "jdbc:oracle:thin:@cncsidb01.msudenver.edu:1521:DB01";
    private static Connection conn;
    
    /**
    * Private Constructor
    */
    private OracleJdbc() {}
    
    /**
     * Checks if a given username is locked out in database.
     * 
     * @param username  The username of account to check
     * @return          True if the account is locked, false otherwise.
     */
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
                if(set.getString("LOCKED_YN").equals("Y")){
                    locked = true;
                }
                else {
                    locked = false;
                }
            }
            
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        return locked;
    }
    
    /**
     * Method that tries to log in with the given username and password.
     * 
     * @param username  The username of account
     * @param password  The password of account
     * @return          True if it was a successful log in, false otherwise.
     */
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
                if(set.getString("SUCCESS_YN").equals("Y")) {
                    success = true;
                }
                else {
                    success =false;
                }
                
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
    
    /**
     * Method used to unlock an account. Requires an administrator account 
     * along with the username of the account to unlock. An administrator 
     * cannot unlock itself.
     * 
     * @param adminUsername     The Username of the administrator
     * @param adminPassword     The Password of the administrator
     * @param accountUsername   The Username of the account to unlock
     * @return                  True if the account was unlocked, false otherwise.
     */
    public static boolean unlockAccount(String adminUsername, String adminPassword,
                                    String accountUsername) {
        boolean success = false;
        
        if(adminUsername.equals(accountUsername)) {
            return success;
        }
        
        try{
            connect();
            
            String sql = "INSERT INTO Unlocking(USERNAME_TO_UNLOCK, ADMIN_USERNAME, ADMIN_PASSWORD) "
                                    + "VALUES('" + accountUsername + "', '" + adminUsername + "', '" + adminPassword
                                    + "')";
       
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.executeQuery();
            
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
    
    /**
     * Private method used to disconnect from database.
     * 
     * @throws SQLException 
     */
    private static void disconnect() throws SQLException{
        if(conn != null) {
            conn.close();
        }
    }
    
    /**
     * Private method used to connect to database
     * 
     * @throws SQLException 
     */
    private static void connect() throws SQLException{
        Properties props = new Properties();
        props.setProperty("user", "Team4_Tuesday_Task");
        props.setProperty("password", "Team4");  
        
        //creating connection to Oracle database using JDBC
        conn = DriverManager.getConnection(URL,props);
    }
}
