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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author MacAir
 */
public class OracleJdbc {
    
    public static boolean DEBUG = true;
    private final static String URL = "jdbc:oracle:thin:@cncsidb01.msudenver.edu:1521:DB01";
    private Connection conn;
    
    public OracleJdbc() {
        
    }
    
    public boolean validateLogin(String username, String password) {
        try{
            connect();
            //try log in
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean unlockAccount(String adminUsername, String adminPassword,
                                    String accountUsername) {
        
        return false;
    }
    
    public void disconnect() {
        
    }
    
    
    public void connect() throws SQLException{
        Properties props = new Properties();
        props.setProperty("user", "Lecture");
        props.setProperty("password", "Lecture");
        
        if(DEBUG) System.out.println(" DEBUG url=" + URL);
        if(DEBUG) System.out.println(" DEBUG Usernamre=" + props.getProperty("user") );
        if(DEBUG) System.out.println(" DEBUG Password=" + props.getProperty("password") );    
        
        //creating connection to Oracle database using JDBC
        conn = DriverManager.getConnection(URL,props);

        String sql ="select sysdate as current_day from dual";

        //creating PreparedStatement object to execute query
        PreparedStatement preStatement = conn.prepareStatement(sql);
        
        if(DEBUG) System.out.println(" DEBUG: Display result of SQL=" + sql);
        
        displayResultSet(preStatement.executeQuery());


        // Not rey s more complex quert
        // select a3student.name,a3student.major,a3student.address,a3professor.name
        //select a3student.name,a3student.major,a3student.address,a3professor.name
        // FROM A3TRANSCRIPT,
        //  A3STUDENT,
        //  A3PROFESSOR
        // WHERE A3PROFESSOR.PID = a3transcript.pid
        //  AND
        //       a3student.sid = a3transcript.sid
        //  AND
        //       a3professor.name = 'Artem';

        //sql ="select a3student.name,a3student.major,a3student.address,a3professor.name" +
        // If you do this you will get two Id columns titles name and it will throw an exception
        // try it!
        sql ="select a3student.name,a3student.major,a3student.address, a3professor.name as Pname " +
                    " FROM A3TRANSCRIPT," +
                    "      A3STUDENT," +
                    "      A3PROFESSOR" +
                    " WHERE A3PROFESSOR.PID = a3transcript.pid" +
                    "  AND" +
                    "       a3student.sid = a3transcript.sid" +
                    "  AND" +
                    "       a3professor.name = 'Artem'";
        preStatement = conn.prepareStatement(sql);
        if(DEBUG) System.out.println(" DEBUG: Display result of SQL=" + sql);
        displayResultSetAttrib(preStatement.executeQuery());

        //  --- Additional Student example....
        // try somthing familar liker the fruit,vegtable,vegitarian
        // and do a manual loop thru the result,set looking for valies and ...
        sql = "select name,calories,color,ForV  from vegetarian";
        //creating PreparedStatement object to execute query
        preStatement = conn.prepareStatement(sql);
        if(DEBUG) System.out.println(" DEBUG: Display result of SQL=" + sql);
        
        int tempRow=1;
        int tempCalorieThreshold=100;
        ResultSet tempRS = preStatement.executeQuery();  // store results for executes query..
        // Logic will iterate thru the result set looks for a match
        
        
        //  String 	getNString(String columnLabel)
        //  Retrieves the value of the designated column in the current row of this 
        //  ResultSet object as a String in the Java programming language..
        while(tempRS.next())   // should be about 20 rows....
        {
          System.out.println("looking at row[" + ++tempRow + "]" + 
              tempRS.getString("name") + " | " + 
              tempRS.getString("calories") + " | " + 
              tempRS.getString("color") + " | " + 
              tempRS.getString("ForV") + " | " + 
              ((tempRS.getString("ForV").compareTo("F") == 0) ? "Is a Fruit" : "IS a Veggie") );
        }
        System.out.println("Out of rows in this result set");

    }
    
     public  void displayResultSetAttrib(ResultSet R)
     {
     
 // Either put you SQl in a try/catch block...or...
 // make each throw and SQL___ exception (Ex: throws SQLException)
 // use a try cathc bloc if processing in a loop and want to handle 
 // specific ettror in thier exception catch block

    try
    {
      System.out.println(" JEZ TRYIN");

 // Get the meta data of result set.

      ResultSetMetaData resultSetMetaData = R.getMetaData();
      System.out.println("The number of columns in ResultSet is : " + resultSetMetaData.getColumnCount());
      System.out.println("The table name of column number 1 is : " + resultSetMetaData.getTableName(1));

      for(int i = 1 ;  i <= resultSetMetaData.getColumnCount() ; i++)
      {
          System.out.print(" Result set Entry=" + i + 
                           " Schema=" + resultSetMetaData.getSchemaName(i) +
                           " Table=" + resultSetMetaData.getTableName(i) +
                           " Name=" + resultSetMetaData.getColumnName(i) +
                           " Typr=" + resultSetMetaData.getColumnType(i) + " ..." );
          // now sow increment method to getvalue by Unknown columns name from result set....
     
          if( R.next() )
          System.out.println(R.getString(resultSetMetaData.getColumnName(i)) );
      }
    }
    catch (SQLException e) 
    {
      System.out.println(" JEZ TRYIN DOUGH SQLException");
      e.printStackTrace();
    } 
//    catch (ClassNotFoundException e)
//    {
//      System.out.println(" JEZ TRYIN DOH ClassNotFoundException");
//      e.printStackTrace();
//    }
    finally 
    {
      System.out.println("DOH - FINALLY");
// Finally we have close all the JDBC resources
//     try 
//     {
//        if (R != null) 
//        {
//           R.close();
//        }
//        if (statement != null) 
//        {
//           statement.close();
//        }
//        if (connection != null) 
//        {
//           connection.close();
//        }
     }  // End TRY -CATCH - FINALLY examlple
 
  }  // end metod displayResultSet


  public void displayResultSet(ResultSet R) throws SQLException
  {

   // Either put yu SQl in a try/catch block...or...
   // make each throw and SQL___ exception (Ex: throws SQLException)

      while(R.next())
      {
        System.out.println("Current Date from Oracle : " +
                    R.getString("current_day"));
      }
      System.out.println("done");

  }  // end metod displayResultSet

}
