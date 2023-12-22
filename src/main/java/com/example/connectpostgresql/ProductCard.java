package com.example.connectpostgresql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

public class ProductCard implements Initializable{
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private String[] sizes = {"S","M","L","XL","XXL"};

    @FXML private ChoiceBox<String> ChoiceBox_size;

    @FXML
    private AnchorPane AnchorPane_Product;

    @FXML
    private Label Label_ProductName;

    @FXML
    private Label Label_ProductPrice;

    @FXML
    private ImageView ImageView_ProductPhoto;
    @FXML
    private Spinner<Integer> prod_spinner;
    private SpinnerValueFactory<Integer> spin;


    @FXML
    private Button Button_Add;

    private productData prodData;
    private Image image;

    private Integer prodID;
    private String size;
    private String prod_image;


    private Alert alert;
    public void setData(productData prodDatas) {
        this.prodData = prodDatas;

        prod_image = prodData.getImage();
        prodID = prodData.getProductId();
        Label_ProductName.setText(prodData.getProductName());
        Label_ProductPrice.setText("$" + (prodData.getPrice()));
        data.path = prodData.getImage();
        String path = "File:" + prodData.getImage();
        image = new Image(path, 160, 160, false, true);
        ImageView_ProductPhoto.setImage(image);
        pr = prodData.getPrice();;
    }

    private double pr;
    private double totalP;
    private int qty;

    public void addBtn() {

        CustomersPage mForm = new CustomersPage();
        mForm.customerID();

        qty = prod_spinner.getValue();
        String check = "";

        connect = DatabaseConnector.getInstance().getConnect();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM product WHERE prod_id = '" + prodID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("stock");
            }

            if(checkStck == 0){

                String updateStock = "UPDATE product SET prod_name = '" + Label_ProductName.getText()
                        + "', stock = 0, price = " + pr
                        + "image = '" + prod_image
                        + "' WHERE prod_id = '" + prodID + "'";
                prepare = connect.prepareStatement(updateStock);
                prepare.executeUpdate();

            }

            if (qty == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :3");
                alert.showAndWait();
            } else {
                if (checkStck < qty) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This product is Out of stock");
                    alert.showAndWait();
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customer"
                            + "(customer_id, prod_id, prod_name, quantity, price, image, em_username)"
                            + "VALUES(?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);

                    prepare.setInt(1, data.cID);
                    prepare.setInt(2, prodID);
                    prepare.setString(3, Label_ProductName.getText());
                    prepare.setInt(4, qty);

                    totalP = (qty * pr);
                    prepare.setDouble(5,totalP);

                    prepare.setString(6, prod_image);
                    prepare.setString(7, data.username);

                    prepare.executeUpdate();

                    int upStock = checkStck - qty;


                    System.out.println("Image: " + prod_image);

                    String updateStock = "UPDATE product SET "
                            + "prod_name = '" + Label_ProductName.getText()
                            + "', stock = '" + upStock
                            + "', price = '" + pr
                            + "', image = '" + prod_image
                            + "' WHERE prod_id = " + prodID;

                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    mForm.menuGetTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void MySize(ActionEvent event){
        String mySize = ChoiceBox_size.getValue();
//        Label_Customer.setText(mySize);
    }
    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
        ChoiceBox_size.getItems().addAll(sizes);
        ChoiceBox_size.setOnAction(this::MySize);
    }
}
