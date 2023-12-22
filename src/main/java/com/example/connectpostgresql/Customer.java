package com.example.connectpostgresql;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

// Concrete Observer class
public class Customer implements User {
    private String name;
    private Notifier alert ;

    public Customer(String name, Notifier alert) {
        this.name = name;
        this.alert = alert;
        alert.addUser(this);
    }

    @Override
    public void update() {
        System.out.println("Observer " + name + " updated. New notifier: " + data.alert);

        if (data.alert == "UpdateAlert") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There is a super discount on clothes ─=≡Σ((( つ◕ل͜◕)つ");
            alert.setTitle("Notification");
            alert.setX(1170);
            alert.setY(640);
            alert.show();
            Duration duration = Duration.seconds(10);
            Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
                alert.close();
            }));
            timeline.play();
        }
        if(data.alert == "AddAlert"){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "New clothes have arrived (˵ ͡° ͜ʖ ͡°˵)");
            alert.setTitle("Notification");
            alert.setX(1170);
            alert.setY(640);
            alert.show();
            Duration duration = Duration.seconds(10);
            Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
                alert.close();
            }));
            timeline.play();
        }
    }
}
