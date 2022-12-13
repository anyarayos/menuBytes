package com.example.menubytes_customerapp;

public class Payment {
    private String payment_amount;
    private String payment_change;
    private String subtotal;
    private String amount_due;
    private String discount_amount;
    private String discount_type;

    public Payment(String payment_amount, String payment_change) {
        this.payment_amount = payment_amount;
        this.payment_change = payment_change;
    }

//SELECT subtotal, amount_due, discount_amount, discount_type FROM payment WHERE payment_id = (?);


    public Payment(String subtotal, String amount_due, String discount_amount, String discount_type) {
        this.subtotal = subtotal;
        this.amount_due = amount_due;
        this.discount_amount = discount_amount;
        this.discount_type = discount_type;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
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
