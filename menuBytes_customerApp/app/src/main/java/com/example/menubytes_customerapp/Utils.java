package com.example.menubytes_customerapp;

import java.util.ArrayList;

public class Utils {

    private String user_id;

    private String table_name;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    private static Utils instance;

    private static int order_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private static ArrayList<Integer> order_id_list = new ArrayList<Integer>();

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

    public void removeFromOrders (int position) {this.orders.remove(position);}

    public boolean addToOrderIds (int order_id){
        return  order_id_list.add(order_id);
    }
    
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