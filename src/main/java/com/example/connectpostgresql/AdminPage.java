package com.example.connectpostgresql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AdminPage implements Initializable {
    Connection connect;
    Statement statement;
    ResultSet result;
    PreparedStatement prepare;
    private Image image;

    @FXML
    private AnchorPane AnchorPane_MainForm;

    @FXML
    private AnchorPane AnchorPane_Photo;

    @FXML
    private Button Button_Add;

    @FXML
    private Button Button_Clear;

    @FXML
    private Button Button_Customers;

    @FXML
    private Button Button_Dashboard;

    @FXML
    private Button Button_Delete;

    @FXML
    private Button Button_Inventory;

    @FXML
    private Button Button_LogOut;

    @FXML
    private Button Button_Menu;

    @FXML
    private Button Button_Update;

    @FXML
    private Button Button_Import;


    @FXML
    private TextField TextField_Stock;
    @FXML
    private ImageView ImageView_Import;
    @FXML
    private AnchorPane AnchorPane_Dashboard;
    @FXML
    private AnchorPane AnchorPane_Inventory;
    @FXML
    private Button Button_logout;
    private Alert alert;
    @FXML
    private AnchorPane main_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button menu_btn;

    @FXML
    private Button customers_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;
    @FXML
    private TableColumn<productData,String> inventory_col_productSize;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private TextField inventory_productSize;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TextField inventory_price;
    @FXML
    private Label menu_total;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.inventoryShowData();
    }
    public void switchForm(ActionEvent event) {
        if (event.getSource() == this.Button_Dashboard) {
            this.AnchorPane_Dashboard.setVisible(true);
            this.AnchorPane_Inventory.setVisible(false);
//            this.menu_form.setVisible(false);
//            this.customers_form.setVisible(false);
//            this.dashboardDisplayNC();
//            this.dashboardDisplayTI();
//            this.dashboardTotalI();
//            this.dashboardNSP();
//            this.dashboardIncomeChart();
//            this.dashboardCustomerChart();
        }
        else if(event.getSource() == this.Button_Inventory){
            this.AnchorPane_Dashboard.setVisible(false);
            this.AnchorPane_Inventory.setVisible(true);
            inventoryShowData();
        }
    }
    public void inventoryImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(AnchorPane_MainForm.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 120, 127, false, true);

            inventory_imageView.setImage(image);
        }
    }

    public void logout() {
        try {
        this.alert = new Alert(Alert.AlertType.CONFIRMATION);
        this.alert.setTitle("Error Message");
        this.alert.setHeaderText((String)null);
        this.alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = this.alert.showAndWait();
        if (((ButtonType)option.get()).equals(ButtonType.OK)) {
            this.Button_logout.getScene().getWindow().hide();
            Parent root = (Parent) FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Sign_In.fxml")));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Cafe Shop Management System");
            stage.setScene(scene);
            stage.show();
        }
    } catch (Exception var5) {
        var5.printStackTrace();
    }
    }
    public void inventoryAddBtn() {

        if (inventory_productID.getText().isEmpty() || inventory_productName.getText().isEmpty()
                || inventory_productSize.getText().isEmpty() || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                ) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            // CHECK PRODUCT ID
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '"+ inventory_productID.getText() + "'";
            connect = DatabaseConnector.getInstance().getConnect();

            try {

                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(inventory_productID.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO product " + "(prod_id, prod_name, size, stock, price, image) " + "VALUES(?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setInt(1, Integer.parseInt(inventory_productID.getText()));
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, inventory_productSize.getText());
                    prepare.setInt(4, Integer.parseInt(inventory_stock.getText()));
                    prepare.setDouble(5, Double.parseDouble(inventory_price.getText()));

                    String path = data.path;
                    path = path.replace("\\", "\\\\");
                    prepare.setString(6, path);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Notifier newNotification = new Notifier();
        newNotification.setAlert("AddAlert");
    }

    public void inventoryUpdateBtn() {

        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                ||inventory_productSize.getText().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || data.path == null || data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET "
//                    + "prod_id = '" + inventory_productID.getText()
                    + "prod_name = '" + inventory_productName.getText()
                    + "', size = '" + inventory_productSize.getText()
                    + "', stock = '" + inventory_stock.getText()
                    + "', price = '" + inventory_price.getText()
                    + "', image = '"+ path + "' WHERE prod_id = " + data.id;

            connect = DatabaseConnector.getInstance().getConnect();
            try {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE PRoduct ID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Notifier newNotification = new Notifier();
        newNotification.setAlert("UpdateAlert");
    }

    public void inventoryDeleteBtn() {
        if (data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE prod_id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }

    public void inventoryClearBtn() {

        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_stock.setText("");
        inventory_price.setText("");
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);

    }

    // MERGE ALL DATAS
    public ObservableList<productData> inventoryDataList() {

        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = DatabaseConnector.getInstance().getConnect();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;

            while (result.next()) {

                prodData = new productData(result.getInt("prod_id"),
                        result.getString("prod_name"),
                        result.getString("size"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"));

                listData.add(prodData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    // TO SHOW DATA ON OUR TABLE
    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_productSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        inventory_tableView.setItems(inventoryListData);

    }

    public void inventorySelectData() {

        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(String.valueOf(prodData.getProductId()));
        inventory_productName.setText(prodData.getProductName());
        inventory_productSize.setText(prodData.getSize());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.id = prodData.getProductId();

        image = new Image(path, 120, 127, false, true);
        inventory_imageView.setImage(image);
    }

}
