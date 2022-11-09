package com.example.menubytes_customerapp;

public class PendingOrderSumListClass {


    private String PendingOrderSumName;
    private String PendingOrderSumPrice;
    private String PendingOrderSumQty;

    public PendingOrderSumListClass(String pendingOrderSumName, String pendingOrderSumPrice, String pendingOrderSumQty) {
        PendingOrderSumName = pendingOrderSumName;
        PendingOrderSumPrice = pendingOrderSumPrice;
        PendingOrderSumQty = pendingOrderSumQty;
    }

    public String getPendingOrderSumName() {
        return PendingOrderSumName;
    }

    public void setPendingOrderSumName(String pendingOrderSumName) {
        PendingOrderSumName = pendingOrderSumName;
    }

    public String getPendingOrderSumPrice() {
        return PendingOrderSumPrice;
    }

    public void setPendingOrderSumPrice(String pendingOrderSumPrice) {
        PendingOrderSumPrice = pendingOrderSumPrice;
    }

    public String getPendingOrderSumQty() {
        return PendingOrderSumQty;
    }

    public void setPendingOrderSumQty(String pendingOrderSumQty) {
        PendingOrderSumQty = pendingOrderSumQty;
    }

}
