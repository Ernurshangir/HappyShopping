package com.example.connectpostgresql;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

// Concrete Subject class
public class Notifier implements NewProduct {
    private String alert;
    private List<User> users = new ArrayList<>();

    public String getAlert() {
        return alert;
    }
    public void setAlert(String alert) {
        this.alert = alert;
        data.alert = alert;
    }
    @Override
    public void addUser(User customer) {
        users.add(customer);
    }

    @Override
    public void removeUser(User customer) {
        users.remove(customer);
    }

    @Override
    public void notifyUser() {
        for (User client : users) {
            client.update();
        }
    }
}
