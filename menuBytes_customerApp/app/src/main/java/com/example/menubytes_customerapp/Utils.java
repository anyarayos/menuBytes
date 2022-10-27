package com.example.menubytes_customerapp;

import java.util.ArrayList;

public class Utils {

    /*Call this singleton class everytime you add an item to the cart*/
    private static Utils instance;

    private static int order_id;

    private static ArrayList<OrderListClass> orders = new ArrayList<>();

    private Utils(){

    }

        public static Utils getInstance() {
            if(null!=instance){
                return instance;
            }
            else{
                instance = new Utils();
                return instance;
            }
        }


    public boolean addToOrders (OrderListClass order) {
        return orders.add(order);
    }

    public boolean removeFromOrders (OrderListClass order) {return orders.remove(order);}

    public void changeQuantity (int index, String quantity){
        orders.get(index).setOrderQty(quantity);
    }

    public static ArrayList<OrderListClass> getOrders() {
        return orders;
    }

    public static int getOrder_id() {
        return order_id;
    }

    public static void setOrder_id(int order_id) {
        Utils.order_id = order_id;
    }

    public static void removeAll(){
        orders.clear();
    }
}