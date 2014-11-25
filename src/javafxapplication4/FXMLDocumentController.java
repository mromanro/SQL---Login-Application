/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication4;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller class for the FXMLDocument.fxml GUI.
 * It is used to control the main display.
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label mssgLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button submitButton, unlockButton;
    
    /**
     * Handles the click of buttons
     * 
     * @param event The button clicked
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == submitButton) {
            attemptLogin();
        }
        else if(event.getSource() == unlockButton) {
            openUnlockGUI();
        }
    }
    
    /**
     * Handles the key pressed
     * 
     * @param event The key pressed
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            if(passwordField.isFocused()){
                attemptLogin();
            }
            else{
                passwordField.requestFocus();
            }
        }
    }
    
    /**
     * Initializes the GUI
     * 
     * @param url   Not used
     * @param rb    Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mssgLabel.setVisible(false);
    }    
    
    /**
     * Used to open a second window for the purpose of unlocking accounts
     */
    private void openUnlockGUI() {
        mssgLabel.setVisible(false);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UnlockGUIFXML.fxml"));
        Parent root;
        try {
            root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Attempts to log in to the database.
     * Uses username and password that is typed on the textfields.
     */
    private void attemptLogin(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if(username.isEmpty()) {
            setMssg("Type in Username", true);
        }
        else if(password.isEmpty()) {
            setMssg("Type in Password", true);
        }
        else {
            setMssg("Attempting Log In", false);
            if(OracleJdbc.checkLock(username)) {
                setMssg("Account is Locked", true);
            }
            else if(OracleJdbc.validateLogin(username, password)) {
                setMssg("Successful Log In", false);
            }
            else {
                setMssg("Failed to Log In", true);
            }
        }
    }
    
    /**
     * Sets a message on the GUI.
     * Requires a boolean value so to change the color of the message
     * to indicate an error or not.
     * 
     * @param mssg  The message to display
     * @param error Whether the display is an error or not
     */
    private void setMssg(String mssg , boolean error) {
        if(error){
            mssgLabel.setTextFill(Color.RED);
            
        } 
        else {
            mssgLabel.setTextFill(Color.BLUE);
        }
        
        mssgLabel.setText(mssg);
        mssgLabel.setVisible(true);
    }
    
}
