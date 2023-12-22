package com.example.connectpostgresql;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

public interface PaymentStrategy {
    void pay(double amount);
}
    // Concrete implementation of PaymentStrategy for Credit Card payments
     class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String name;
    public CreditCardPayment(String cardNumber, String name) {
        this.cardNumber = cardNumber;
        this.name = name;
    }
    @Override
    public void pay(double amount) {
        // Implement the logic for processing credit card payments
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Paid " + amount + " using credit card ending in " + cardNumber);        alert.setTitle("Notification");
        alert.setX(1170);
        alert.setY(640);
        alert.show();
        Duration duration = Duration.seconds(2);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            alert.close();
        }));
        timeline.play();
        System.out.println("Paid " + amount + " using credit card ending in " + cardNumber);
    }}
// Concrete implementation of PaymentStrategy for PayPal payments

class PayPalPayment implements PaymentStrategy {
    private String email;
    public PayPalPayment(String email) {
        this.email = email;
    }
    @Override
    public void pay(double amount) {        // Implement the logic for processing PayPal payments
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Paid " + amount + " using PayPal account " + email);
        alert.setTitle("Notification");
        alert.setX(1170);
        alert.setY(640);
        alert.show();
        Duration duration = Duration.seconds(4);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            alert.close();
        }));
        timeline.play();
        System.out.println("Paid " + amount + " using PayPal account " + email);
    }
}
// Context class that uses the PaymentStrategy
class PaymentContext {
    private PaymentStrategy paymentStrategy;
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public void processPayment(double amount) {
        paymentStrategy.pay(amount);
    }
}
