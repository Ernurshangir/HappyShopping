package com.example.connectpostgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/Javafx";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static class Holder {
        private static final DatabaseConnector INSTANCE = new DatabaseConnector();
    }

    private Connection databaseLink;

    private DatabaseConnector() {
        try {
            databaseLink = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static DatabaseConnector getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (databaseLink != null && !databaseLink.isClosed()) {
                databaseLink.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
