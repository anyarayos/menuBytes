package com.example.menubytes_customerapp;

public class OrderListClass {
    private int OrderID;



    private String OrderName;
    private String OrderPrice;
    private String OrderQty;
    private boolean OrderBundle=false;
    private String OrderAddOns_1="";
    private String OrderAddOns_2="";
    private String OrderAddOns_3="";
    private String OrderAddOns_4="";
    private String OrderFlavor_1="";
    private String OrderFlavor_2="";
    private String OrderFlavor_3="";


    public OrderListClass(int orderID, String orderName, String orderPrice, String orderQty){
        OrderID  = orderID;
        OrderName = orderName;
        OrderPrice = orderPrice;
        OrderQty = orderQty;
    }


    public OrderListClass(int orderID, String orderName, String orderPrice, String orderQty, String orderAddOns_1, String orderAddOns_2, String orderAddOns_3, String orderAddOns_4) {
        OrderID = orderID;
        OrderName = orderName;
        OrderPrice = orderPrice;
        OrderQty = orderQty;
        OrderAddOns_1 = orderAddOns_1;
        OrderAddOns_2 = orderAddOns_2;
        OrderAddOns_3 = orderAddOns_3;
        OrderAddOns_4 = orderAddOns_4;
    }

    public OrderListClass(int orderID, String orderName, String orderPrice, String orderQty,
                          boolean orderBundle, String orderAddOns_1, String orderAddOns_2, String orderAddOns_3, String orderAddOns_4) {
        OrderID = orderID;
        OrderName = orderName;
        OrderPrice = orderPrice;
        OrderQty = orderQty;
        OrderBundle = orderBundle;
        OrderAddOns_1 = orderAddOns_1;
        OrderAddOns_2 = orderAddOns_2;
        OrderAddOns_3 = orderAddOns_3;
        OrderAddOns_4 = orderAddOns_4;
    }

    public OrderListClass(String orderName, String orderPrice, String orderQty, String orderAddOns_1, String orderAddOns_2, String orderAddOns_3,
                          String orderFlavor_1, String orderFlavor_2, String orderFlavor_3) {
        OrderName = orderName;
        OrderPrice = orderPrice;
        OrderQty = orderQty;
        OrderAddOns_1 = orderAddOns_1;
        OrderAddOns_2 = orderAddOns_2;
        OrderAddOns_3 = orderAddOns_3;
        OrderFlavor_1 = orderFlavor_1;
        OrderFlavor_2 = orderFlavor_2;
        OrderFlavor_3 = orderFlavor_3;
    }

    public boolean isOrderBundle() {
        return OrderBundle;
    }

    public int getOrderID() {
        return OrderID;
    }

    public String getOrderName() {
        return OrderName;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public String getOrderAddOns_1() {
        return OrderAddOns_1;
    }

    public String getOrderAddOns_2() {
        return OrderAddOns_2;
    }

    public String getOrderAddOns_3() {
        return OrderAddOns_3;
    }

    public String getOrderAddOns_4() {
        return OrderAddOns_4;
    }

    public String getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(String orderQty) {
        OrderQty = orderQty;
    }

    public String getOrderFlavor_1() {
        return OrderFlavor_1;
    }

    public void setOrderFlavor_1(String orderFlavor_1) {
        OrderFlavor_1 = orderFlavor_1;
    }

    public String getOrderFlavor_2() {
        return OrderFlavor_2;
    }

    public void setOrderFlavor_2(String orderFlavor_2) {
        OrderFlavor_2 = orderFlavor_2;
    }

    public String getOrderFlavor_3() {
        return OrderFlavor_3;
    }

    public void setOrderFlavor_3(String orderFlavor_3) {
        OrderFlavor_3 = orderFlavor_3;
    }
}
