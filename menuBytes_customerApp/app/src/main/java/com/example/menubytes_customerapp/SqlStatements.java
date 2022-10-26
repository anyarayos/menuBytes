package com.example.menubytes_customerapp;

public class SqlStatements {

    private String retrieveProductsByID = "SELECT PRODUCT_IMG, PRODUCT_NAME, PRODUCT_PRICE, " +
            "PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_ID = (?)";

    private String retrieveProductsByCategory = "SELECT PRODUCT_ID, PRODUCT_IMG, PRODUCT_NAME, " +
            "PRODUCT_PRICE, PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_CATEGORY = (?)";

    private String insertIntoOrders = "INSERT INTO orders(user_id, total, created_at, created_by) VALUES(\n" +
            "((SELECT user_id from user where user_id = 3)),\n" +
            "(?),\n" +
            "(?),\n" +
            "((SELECT user_name from user where user_id = 3))\n" +
            ");\n";

    private String insertIntoOrderStatus = "INSERT INTO order_status(order_id, order_status, created_at, created_by)\n" +
            "VALUES(\n" +
            "(?),\n" +
            "(?),\n" +
            "((SELECT created_at FROM orders where order_id = (?))),\n" +
            "((SELECT user_name from user where user_id = 3))\n" +
            ");";

    private String insertIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity)\n" +
            "VALUES((?),(?),(?));";

    public String getInsertIntoOrderItems() {
        return insertIntoOrderItems;
    }

    public String getInsertIntoOrderStatus() {
        return insertIntoOrderStatus;
    }
    public String getRetrieveProductsByCategory() {
        return retrieveProductsByCategory;
    }

    public String getRetrieveProductsByID() {
        return retrieveProductsByID;
    }

    public String getInsertIntoOrders() {
        return insertIntoOrders;
    }

}
