package com.example.connectpostgresql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sign_Up {
    @FXML
    private Button Button_Sign_In;
    @FXML
    private Button Button_Submit;
    @FXML
    private RadioButton RadioButton_Male;
    @FXML
    private RadioButton RadioButton_Female;
    @FXML
    private TextField TextField_Email;
    @FXML
    private TextField TextField_Username;
    @FXML
    private TextField PasswordField_Password;
    @FXML
    private Label Label_Email;
    @FXML
    private Label Label_Username;
    @FXML
    private Label Label_Password;
    @FXML
    private TextField clearPasswordName;
    @FXML
    private TextField clearEmailName;
    @FXML
    private TextField clearUsernameName;
    private boolean checkForReg;
    private Connection connect;



    @FXML
    void initialize() {
        Button_Sign_In.setOnAction(e -> {
            try {
                new Sign_In().openFxmlFile( e,"Sign_In");
                Stage s = (Stage) Button_Submit.getScene().getWindow();
                s.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button_Submit.setOnAction(e -> {
            SignUP(e);
        });
    }
    @FXML
    private void clearPasswordName(MouseEvent event) {
        Label_Password.setText("");
    }
    @FXML
    private void clearUsernameName(MouseEvent event) {
        Label_Username.setText("");
    }
    @FXML
    private void clearEmailName(MouseEvent event) {
        Label_Email.setText("");
    }

    void SignUP(ActionEvent event){
        if(!TextField_Username.getText().isEmpty() && !TextField_Email.getText().isEmpty() && !PasswordField_Password.getText().isEmpty() && (RadioButton_Female.isSelected() || RadioButton_Male.isSelected())){
            Connection connectDb = DatabaseConnector.getInstance().getConnect();

            // query
            String query = "insert into users(name,email,password) VALUES(?, ?, ?)";

            try {
                PreparedStatement pst = connectDb.prepareStatement(query);
                pst.setString(1, TextField_Username.getText());
                pst.setString(2, TextField_Email.getText());
                pst.setString(3, PasswordField_Password.getText());
                pst.executeUpdate();
                System.out.println("Sucessfully created.");

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
    @FXML
    void ToggleGroup(){
        ToggleGroup group = new ToggleGroup();
        RadioButton_Male.setToggleGroup(group);
        RadioButton_Female.setToggleGroup(group);
    }
    void CheckData(){
        if(TextField_Email.getText().isEmpty() || TextField_Username.getText().isEmpty()
            || PasswordField_Password.getText().isEmpty() || (!RadioButton_Male.isSelected() && !RadioButton_Female.isSelected())){
            ShowError("Register is'n correct!");
            checkForReg=false;
        }
        else{
            infoMessage("Succesfully registered!");
            checkForReg = true;
        }
    }
    void ShowError(String str){
        Alert alert = new Alert(Alert.AlertType.ERROR, str);
        alert.setTitle("Error");
        alert.showAndWait();
    }
    void infoMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.setTitle("Information");
        alert.showAndWait();
    }
    void Submit(ActionEvent e){
        CheckData();
    }
}
