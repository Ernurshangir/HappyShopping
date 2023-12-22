package com.example.connectpostgresql;


public class CustomerPagefacade {
    private CustomersPage customersPage;

    public CustomerPagefacade(CustomersPage customersPage) {
        this.customersPage = customersPage;
    }

    public void processOrder() {
        customersPage.menuDisplayCard();
        customersPage.menuGetOrder();
        customersPage.menuDisplayTotal();
        customersPage.menuShowOrderData();
    }

    public void makePayment(String paymentType, String amount, String cardDetails, String name) {
        if ("CreditCard".equalsIgnoreCase(paymentType)) {
            // Process credit card payment
            PaymentStrategy creditCardPayment = new CreditCardPayment(cardDetails, name);
            PaymentContext paymentContext = new PaymentContext(creditCardPayment);
            paymentContext.processPayment(Integer.parseInt(amount));
            paymentContext.setPaymentStrategy(creditCardPayment);
        } else if ("PayPal".equalsIgnoreCase(paymentType)) {
            // Process PayPal payment
            PaymentStrategy payPalPayment = new PayPalPayment(name);
            PaymentContext paymentContext = new PaymentContext(payPalPayment);
            paymentContext.processPayment(Integer.parseInt(amount));
        }
    }
}

