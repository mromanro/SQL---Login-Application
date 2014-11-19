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
    private Button submitButton;
    
    private OracleJdbc database;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == submitButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            database.validateLogin(username, password);
            setErrorMssg("Submit button clicked");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mssgLabel.setVisible(false);
        
        database = new OracleJdbc();
    }    
    
    public void setErrorMssg(String mssg) {
        mssgLabel.setText(mssg);
        mssgLabel.setVisible(true);
    }
    
}
