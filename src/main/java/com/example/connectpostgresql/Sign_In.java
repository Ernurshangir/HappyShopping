package com.example.connectpostgresql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Sign_In {
    @FXML
    private Button Button_Sign_In;
    @FXML
    private Button Button_Sign_Up;
    @FXML
    private Button Button_Submit;
    @FXML
    private Button Button_ForgotPassword;
    @FXML
    private TextField TextField_Username;
    @FXML
    private PasswordField PasswordField_Password;
    @FXML
    private Label Label_Username;
    @FXML
    private Label Label_Password;
    @FXML
    private TextField clearPasswordName;
    @FXML
    private TextField clearEmailName;
    @FXML
    void initialize() {
        Button_Sign_Up.setOnAction(e -> {
            try{
                openFxmlFile(e,"Sign_Up");
                Stage stage = (Stage) Button_Submit.getScene().getWindow();
                stage.close();
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }

        });
        Button_Submit.setOnAction(e -> {
            try {
                loginButtonOnAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
    public void openFxmlFile(ActionEvent e,String name_fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(name_fxml + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void clearPasswordName(MouseEvent event) {
        Label_Password.setText("");
    }
    @FXML
    private void clearEmailName(MouseEvent event){
        Label_Username.setText("");
    }




    public void loginButtonOnAction() throws IOException {
        if (!TextField_Username.getText().isBlank() && !PasswordField_Password.getText().isBlank()){
            if (TextField_Username.getText().equals("Admin") && PasswordField_Password.getText().equals("root")){
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Admin.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }else {
                validateLogin();
            }
        }
        else {
            ShowError("Please enter username and password");
        }
    }

    public void validateLogin(){
        Connection connectDb = DatabaseConnector.getInstance().getConnect();

        String verifyLogin = "select count(1) from users where name = '"+TextField_Username.getText()+"' and password = '"+PasswordField_Password.getText()+"'";
        try{
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while (queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    data.username = TextField_Username.getText();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Customer.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                }else {
                    ShowError("Invalid login. Please try again.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void ShowError(String str){
        Alert alert = new Alert(Alert.AlertType.ERROR, str);
        alert.setTitle("Error");
        alert.showAndWait();
    }
}

