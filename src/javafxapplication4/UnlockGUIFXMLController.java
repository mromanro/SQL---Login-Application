/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication4;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author MacAir
 */
public class UnlockGUIFXMLController implements Initializable {

    @FXML
    private Label mssgLabel;
    @FXML
    private Button submitButton;
    @FXML
    private TextField accountUserField, adminUserField, adminPassField;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == submitButton) {
            unlock();
        }
    }
    
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            if(adminUserField.isFocused()) {
                adminPassField.requestFocus();
            }
            else if(adminPassField.isFocused()) {
                accountUserField.requestFocus();
            }
            else {
                unlock();
            }
        }
    }
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mssgLabel.setVisible(false);
    }    
    
    private void unlock() {
        String adminUsername = adminUserField.getText();
        String adminPassword = adminPassField.getText();
        String accountUsername = accountUserField.getText();
        
        if(adminUsername.isEmpty()) {
            setMssg("Enter Admin Username", true);
        }
        else if(adminPassword.isEmpty()) {
            setMssg("Enter Admin Password", true);
        }
        else if(accountUsername.isEmpty()) {
            setMssg("Enter Username of account to unlock", true);
        }
        else {
            setMssg("Attempting Unlock", false);
            boolean unlocked = OracleJdbc.unlockAccount(adminUsername, 
                                            adminPassword, accountUsername);
            if(unlocked) {
                setMssg("Account Unlocked", false);
            }
            else {
                setMssg("Failure to unlock", true);
            }
        }
    }
    
    private void setMssg(String mssg, boolean error) {
        if(error) {
            mssgLabel.setTextFill(Color.RED);
        }
        else {
            mssgLabel.setTextFill(Color.BLUE);
        }
        
        mssgLabel.setText(mssg);
        mssgLabel.setVisible(true);
    }
    
}
