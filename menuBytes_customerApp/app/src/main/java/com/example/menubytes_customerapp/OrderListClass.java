package com.example.menubytes_customerapp;

public class OrderListClass {
    private String OrderID;
    private int ProductID;
    private String OrderName;
    private String OrderPrice;
    private String OrderQty;
    private String OrderCategory;
    private boolean OrderBundle=false;
    private String OrderAddOnName ="";
    private String OrderSubPrice;
    private boolean has_addons;

//use this
    public OrderListClass(String orderQty, String orderName,  String orderPrice,String orderSubPrice, boolean has_addons){
        this.OrderQty = orderQty;
        this.OrderName = orderName;
        this.OrderPrice = orderPrice;
        this.OrderSubPrice = orderSubPrice;
        this.has_addons = has_addons;
    }

    //TODO: Has Add Ons
    public OrderListClass(int productID, String orderName, String orderPrice, String orderQty,
                          String orderCategory, boolean orderBundle, String orderAddOnName, String orderSubPrice,
                          boolean has_addons) {
        ProductID = productID;
        OrderName = orderName;
        OrderPrice = orderPrice;
        OrderQty = orderQty;
        OrderSubPrice = orderSubPrice;
        OrderCategory = orderCategory;
        OrderBundle = orderBundle;
        if(orderBundle){
            OrderName = "B1G1 " + orderName;
        }
        OrderAddOnName = orderAddOnName;
        if(has_addons){
            this.has_addons=true;
        }else {this.has_addons=false;}
    }


    public boolean isOrderBundle() {
        return OrderBundle;
    }

    public int getProductID() {
        return ProductID;
    }

    public String getOrderName() {
        return OrderName;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public String getOrderAddOnName() {
        return OrderAddOnName;
    }


    public String getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(String orderQty) {
        OrderQty = orderQty;
    }


    public String getOrderCategory() {
        return OrderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        OrderCategory = orderCategory;
    }

    public String getOrderSubPrice() {
        return OrderSubPrice;
    }

    public void setOrderSubPrice(String orderSubPrice) {
        OrderSubPrice = orderSubPrice;
    }

    public boolean isHas_addons() {
        return has_addons;
    }

    public void setHas_addons(boolean has_addons) {
        this.has_addons = has_addons;
    }
}
