package com.example.menubytes_customerapp;

public class Payment {
    private String payment_amount;
    private String payment_change;

    public Payment(String payment_amount, String payment_change) {
        this.payment_amount = payment_amount;
        this.payment_change = payment_change;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getPayment_change() {
        return payment_change;
    }

    public void setPayment_change(String payment_change) {
        this.payment_change = payment_change;
    }
}
