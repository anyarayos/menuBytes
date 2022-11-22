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
    private String flavors;

//use this
    public OrderListClass(String orderQty, String orderName,  String orderPrice,String orderSubPrice, boolean has_addons){
        this.OrderQty = orderQty;
        this.OrderName = orderName;
        this.OrderPrice = orderPrice;
        this.OrderSubPrice = orderSubPrice;
        this.has_addons = has_addons;
    }

    public OrderListClass(String orderQty, String orderName,  String orderPrice,String orderSubPrice, boolean has_addons,
                          String flavors){
        this.OrderQty = orderQty;
        this.OrderName = orderName;
        this.OrderPrice = orderPrice;
        this.OrderSubPrice = orderSubPrice;
        this.has_addons = has_addons;
        this.flavors = flavors;
    }

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
            OrderName = "B1G1_" + orderName;
        }
        OrderAddOnName = orderAddOnName;
        if(has_addons){
            this.has_addons=true;
        }else {this.has_addons=false;}
    }
    public OrderListClass(int productID, String orderName, String orderPrice, String orderQty,
                          String orderCategory, boolean orderBundle, String orderAddOnName, String orderSubPrice,
                          boolean has_addons,String flavors) {
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
        this.flavors =flavors;
    }


    public boolean isOrderBundle() {
        return OrderBundle;
    }

    public void setOrderBundle(boolean has_orderBundle) {

        OrderBundle = has_orderBundle;
    }

    public int getProductID() {
        return ProductID;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }
    public String getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        OrderPrice = orderPrice;
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

    public String getFlavors() {
        return flavors;
    }

    public void setFlavors(String flavors) {
        this.flavors = flavors;
    }
}
