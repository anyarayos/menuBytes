package com.example.menubytes_customerapp;

public class PendingOrderSumListClass {


    private String PendingOrderSumName;
    private String PendingOrderSumPrice;
    private String PendingOrderSumQty;
    private boolean has_addons;

    public PendingOrderSumListClass(String pendingOrderSumName, String pendingOrderSumPrice, String pendingOrderSumQty) {
        PendingOrderSumName = pendingOrderSumName;
        PendingOrderSumPrice = pendingOrderSumPrice;
        PendingOrderSumQty = pendingOrderSumQty;
    }

    public PendingOrderSumListClass(String pendingOrderSumName, String pendingOrderSumPrice, String pendingOrderSumQty, boolean has_addons) {
        PendingOrderSumName = pendingOrderSumName;
        PendingOrderSumPrice = pendingOrderSumPrice;
        PendingOrderSumQty = pendingOrderSumQty;
        this.has_addons = has_addons;
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

    public boolean isHas_addons() {
        return has_addons;
    }

    public void setHas_addons(boolean has_addons) {
        this.has_addons = has_addons;
    }
}
