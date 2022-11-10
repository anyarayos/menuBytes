package com.example.menubytes_customerapp;

import java.util.ArrayList;

public class PendingListClass {
    String OrderStatus, OrderNum, OrderTy, OrderTotalPrize;
    Boolean has_addons;

    public PendingListClass(String orderStatus, String orderNum, String orderTy, String orderTotalPrize) {
        OrderStatus = orderStatus;
        OrderNum = orderNum;
        OrderTy = orderTy;
        OrderTotalPrize = orderTotalPrize;
    }

    public PendingListClass(String orderStatus, String orderNum, String orderTy, String orderTotalPrize, Boolean has_addons) {
        OrderStatus = orderStatus;
        OrderNum = orderNum;
        OrderTy = orderTy;
        OrderTotalPrize = orderTotalPrize;
        this.has_addons = has_addons;
    }

    public Boolean getHas_addons() {
        return has_addons;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(String orderNum) {
        OrderNum = orderNum;
    }

    public String getOrderTy() {
        return OrderTy;
    }

    public void setOrderTy(String orderTy) {
        OrderTy = orderTy;
    }

    public String getOrderTotalPrize() {
        return OrderTotalPrize;
    }

    public void setOrderTotalPrize(String orderTotalPrize) {
        OrderTotalPrize = orderTotalPrize;
    }
}
