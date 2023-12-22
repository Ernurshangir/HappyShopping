module com.example.connectpostgresql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;


    opens com.example.connectpostgresql to javafx.fxml;
    exports com.example.connectpostgresql;
}