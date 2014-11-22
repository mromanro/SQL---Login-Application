/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication4;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author MacAir
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
    
    private OracleJdbc database;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == submitButton) {
            attemptLogin();
        }
        else if(event.getSource() == unlockButton) {
            openUnlockGUI();
        }
    }
    
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            if(passwordField.isFocused()){
                setMssg("Attempting Log In", false);
                attemptLogin();
            }
            else{
                passwordField.requestFocus();
                setMssg("pressed enter", false);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mssgLabel.setVisible(false);
        database = new OracleJdbc();
    }    
    
    private void openUnlockGUI() {
        setMssg("Unlocking" , false);
        
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
    
    private void attemptLogin(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if(username.isEmpty())
            setMssg("Type in Username", true);
        else if(password.isEmpty())
            setMssg("Type in Password", true);
        else
            database.validateLogin(username, password);
    }
    
    private void setMssg(String mssg , boolean error) {
        if(error){
            mssgLabel.setTextFill(Color.RED);
            
        } else {
            mssgLabel.setTextFill(Color.BLUE);
        }
        
        mssgLabel.setText(mssg);
        mssgLabel.setVisible(true);
    }
    
}
