package com.example.connectpostgresql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

public class CustomersPage implements Initializable {
    Connection connect;
    ResultSet result;
    PreparedStatement prepare;
    private ObservableList<productData> cardListData = FXCollections.observableArrayList();


    @FXML
    private AnchorPane AnchorPane_MainForm;

    @FXML
    private AnchorPane AnchorPane_Menu;

    @FXML
    private Button Button_ClearNotification;
    @FXML
    private Button Button_LogOut;

    @FXML
    private Button Button_Menu;
    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private TableColumn<productData, String> menu_col_price;
    @FXML
    private Label menu_total;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private GridPane GridPane_Menu;
    @FXML
    private Button Button_Pay;

    @FXML
    private Button Button_Remove;
    @FXML
    private Button Button_PayPal;

    @FXML
    private Button Button_Receipt;
    @FXML
    private Button Button_Card;
    @FXML
    private TextField TextField_Name;
    @FXML
    private TextField TextField_Amount;
    @FXML
    private AnchorPane AnchorPane_Phone;
    @FXML
    private AnchorPane AnchorPane_Card;
    @FXML
    private TextField TextField_NameEmail;
    @FXML
    private TextField TextField_AmountEmail;
    @FXML
    private TextField TextField_Card;
    @FXML
    private TextField TextField_Email;


    @FXML
    private Label Label_Customer;

    @FXML
    private Label Label_Total;

    @FXML
    private TableView<?> TableView_Check;

    @FXML
    private Button Button_Notification;

    @FXML
    private Text Text_Notifcation;
    @FXML
    private Button Button_logout;
    private Alert alert;


    public ObservableList<productData> menuGetData() {

        String sql = "SELECT * FROM product";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = DatabaseConnector.getInstance().getConnect();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(
                        result.getInt("prod_id"),
                        result.getString("prod_name"),
                        result.getString("size"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }


    @FXML
    public void initalize(){

        Button_Menu.setOnAction(e -> {
            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        });
        Button_PayPal.setOnAction(e -> {
            AnchorPane_Card.setVisible(false);
            AnchorPane_Phone.setVisible(true);

        });
        Button_Pay.setOnAction(e -> {
            if(AnchorPane_Card.isVisible()==true){
                if(TextField_Amount.getText().isEmpty() || TextField_Card.getText().isEmpty() || TextField_Name.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Blank is empty");
                    alert.setTitle("Error");
                    alert.showAndWait();
                }
                else {
                    PaymentStrategy creditCardPayment = new CreditCardPayment(TextField_Card.getText(), TextField_Name.getText());
                    PaymentContext paymentContext = new PaymentContext(creditCardPayment);
                    paymentContext.processPayment(Integer.parseInt(TextField_Amount.getText()));
                    paymentContext.setPaymentStrategy(creditCardPayment);
                }
            }
            else{
                if(TextField_AmountEmail.getText().isEmpty() || TextField_NameEmail.getText().isEmpty() || TextField_Email.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Blank is empty");
                    alert.setTitle("Error");
                    alert.showAndWait();
                }
                else {
                    PaymentStrategy payPalPayment = new PayPalPayment(TextField_NameEmail.getText());
                    PaymentContext paymentContext = new PaymentContext(payPalPayment);
                    paymentContext.processPayment(Integer.parseInt(TextField_AmountEmail.getText()));
                }
            }
        });
        Button_Card.setOnAction(e -> {
            AnchorPane_Card.setVisible(true);
            AnchorPane_Phone.setVisible(false);
        });
    }
    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        GridPane_Menu.getChildren().clear();
        GridPane_Menu.getRowConstraints().clear();
        GridPane_Menu.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Product.fxml"));
                AnchorPane pane = load.load();
                ProductCard cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 4) {
                    column = 0;
                    row += 1;
                }

                GridPane_Menu.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(8));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<productData> menuGetOrder() {
        customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = '" + cID + "'";

        connect = DatabaseConnector.getInstance().getConnect();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(
                        result.getInt("prod_id"),
                        result.getString("prod_name"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"));
                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private static ObservableList<productData> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuOrderListData);
    }

    private int getid;

    public void menuSelectOrder() {
        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        // TO GET THE ID PER ORDER
        getid = prod.getId();

    }

    private double totalP;

    public void menuGetTotal() {
        customerID();  // Убедитесь, что cID инициализирован корректно
        String total = "SELECT SUM(price) as Total_price FROM customer WHERE customer_id = ?";
        try (Connection connect = DatabaseConnector.getInstance().getConnect();
             PreparedStatement prepare = connect.prepareStatement(total)) {

            prepare.setInt(1, cID);  // Установите значение параметра перед выполнением запроса

            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    totalP = result.getDouble("Total_price");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("$" + totalP);
    }

    private int cID;

    public void customerID() {
        String sql = "SELECT MAX(customer_id) AS max_customer_id FROM customer";
        String checkCID = "SELECT MAX(customer_id) AS max_customer_id FROM receipt";

        try (Connection connect = DatabaseConnector.getInstance().getConnect();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            int maxCustomerID = 0;

            if (result.next()) {
                maxCustomerID = result.getInt("max_customer_id");
            }

            int checkID = 0;

            try (PreparedStatement checkPrepare = connect.prepareStatement(checkCID);
                 ResultSet checkResult = checkPrepare.executeQuery()) {

                if (checkResult.next()) {
                    checkID = checkResult.getInt("max_customer_id");
                }
            }

            if (cID == 0 || cID == checkID) {
                cID = maxCustomerID + 1;
            }

            data.cID = cID;
            System.out.println("customer-id " + cID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private double amount;
    private double change;

    public void menuAmount() {
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalP == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            if (amount < totalP) {
                menu_amount.setText("");
            } else {
                change = (amount - totalP);
                menu_change.setText("$" + change);
            }
        }
    }

    public void menuPayBtn() {

        if (totalP == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customer_id, total, username) "
                    + "VALUES(?,?,?)";

            connect = DatabaseConnector.getInstance().getConnect();

            try {

                if (amount == 0) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Messaged");
                    alert.setHeaderText(null);
                    alert.setContentText("Something wrong :3");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        customerID();
                        menuGetTotal();
                        prepare = connect.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));
                        prepare.setString(3, data.username);

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();

                        menuShowOrderData();

                    } else {
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void menuRemoveBtn() {

        if (getid == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM customer WHERE id = " + getid;
            connect = DatabaseConnector.getInstance().getConnect();
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }

                menuShowOrderData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void menuRestart() {
        totalP = 0;
        change = 0;
        amount = 0;
        menu_total.setText("$0.0");
        menu_amount.setText("");
        menu_change.setText("$0.0");
    }
    public void menuReceiptBtn() {

        if (totalP == 0 || menu_amount.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setContentText("Please order first");
            alert.showAndWait();
        } else {
            HashMap map = new HashMap();
            map.put("getReceipt", (cID - 1));

            try {

                JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\WINDOWS 10\\Documents\\NetBeansProjects\\cafeShopManagementSystem\\src\\cafeshopmanagementsystem\\report.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, map, connect);

                JasperViewer.viewReport(jPrint, false);

                menuRestart();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    public void logout() {

        try {
            this.alert = new Alert(Alert.AlertType.CONFIRMATION);
            this.alert.setTitle("Error Message");
            this.alert.setHeaderText((String) null);
            this.alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = this.alert.showAndWait();
            if (((ButtonType) option.get()).equals(ButtonType.OK)) {
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

    private CustomerPagefacade customersPage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        menuDisplayCard();
//        menuGetOrder();
//        menuDisplayTotal();
//        menuShowOrderData();


        Notifier newNotification = new Notifier();
        Customer customer = new Customer(data.username,newNotification);
        newNotification.notifyUser();


        customersPage = new CustomerPagefacade(this);

        // Use the facade to simplify operations
        customersPage.processOrder();

    }

}





