package com.example.menubytes_customerapp;

import java.util.ArrayList;

public class OrderListContainer {
    boolean willDelete;


    public OrderListContainer(boolean pendingAction) {
        willDelete = pendingAction;
    }

    public boolean isWillDelete() {
        return willDelete;
    }

    public void setWillDelete(boolean willDelete) {
        this.willDelete = willDelete;
    }
}
