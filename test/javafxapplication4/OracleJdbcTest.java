/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MacAir
 */
public class OracleJdbcTest {
    
    public OracleJdbcTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
     /**
     * Test of unlockAccount method, of class OracleJdbc.
     */
    @Test
    public void testUnlockAccount() {
        System.out.println("Test unlockAccount");
        String adminUsername = "ADMIN";
        String adminPassword = "4321";
        String accountUsername = "USER1";
        boolean expResult = true;
        boolean result = OracleJdbc.unlockAccount(adminUsername, adminPassword, accountUsername);
        assertEquals(expResult, result);
        
        adminUsername = "Peter";
        expResult = false;
        result = OracleJdbc.unlockAccount(adminUsername, adminPassword, accountUsername);
        assertEquals(expResult, result);
        System.out.println("Unlock Account Test Passed");
    }
    
     /**
     * Test of validateLogin method, of class OracleJdbc.
     */
    @Test
    public void testValidateLogin() {
        System.out.println("Test validateLogin");
        String username = "USER1";
        String password = "PASS1";
        boolean expResult = true;
        boolean result = OracleJdbc.validateLogin(username, password);
        assertEquals(expResult, result);
        
        password = "fakePassword";
        expResult = false;
        result = OracleJdbc.validateLogin(username, password);
        assertEquals(expResult, result);
        System.out.println("Validate Log In Test Passed");
    }
    
    /**
     * Test of checkLock method, of class OracleJdbc.
     */
    @Test
    public void testCheckLock() {
        System.out.println("Test checkLock");
        String username = "Michael";
        boolean expResult = true;
        boolean result = OracleJdbc.checkLock(username);
        assertEquals(expResult, result);
    
        username = "USER1";
        expResult = false;
        result = OracleJdbc.checkLock(username);
        assertEquals(expResult, result);
        System.out.println("Check Lock Test Passed");
    }

    
    /**
    * Test that an account locks
    */
    @Test
    public void testAccountLocks() {
        System.out.println("Test lock out an account");
        String username = "USER1";
        String password = "fakePassword";
        boolean expResult = true;
        
        for(int i = 0; i < 3; i++) {
            OracleJdbc.validateLogin(username, password);
        }
        
        boolean result = OracleJdbc.checkLock(username);
        assertEquals(expResult, result);
        System.out.println("Account Locks Test passed");
    }
}
