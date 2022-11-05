package com.example.menubytes_customerapp;

public class SqlStatements {

    private String retrieveProductsByID = "SELECT PRODUCT_IMG, PRODUCT_NAME, PRODUCT_PRICE, " +
            "PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_ID = (?)";

    private String retrieveProductsByCategory = "SELECT PRODUCT_ID, PRODUCT_IMG, PRODUCT_NAME, " +
            "PRODUCT_PRICE, PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_CATEGORY = (?)";

    private String insertIntoOrders = "INSERT INTO orders(user_id, total, created_at, created_by) \n" +
            "VALUES(\n" +
            "((SELECT user_id from user where user_id = (?))),\n" +
            "(?),\n" +
            "(?),\n" +
            "((SELECT user_name from user where user_id = (?)))\n" +
            " );";

    private String insertIntoOrderStatus = "INSERT INTO order_status(order_id, order_status, created_at, created_by)\n" +
            "VALUES(\n" +
            "(?),\n" +
            "(?),\n" +
            "((SELECT created_at FROM orders where order_id = (?))),\n" +
            "((SELECT user_name from user where user_id = (?)))\n" +
            ");";

    private String insertIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity,product_bundle)\n" +
            "VALUES((?),(?),(?),(?));";

    private String insertAddOnsIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity,product_bundle)\n" +
            "VALUES((?),(SELECT product_id from product where product_name = (?)),(?),(?));";

    private String insertGcashPayment = "INSERT INTO payment \n" +
            "(payment_amount,\n" +
            "payment_method,\n" +
            "payment_status,\n" +
            "created_at,\n" +
            "created_by)\n" +
            "VALUES\n" +
            "((0), \n" +
            "\"GCASH\",\n" +
            "\"PENDING\",\n" +
            "current_timestamp(),\n" +
            "(SELECT user_name from user where user_id = (?))\n" +
            ");";

    private String retrieveTotalAmount = "SELECT \n" +
            "SUM(orders.total) AS total_amount\n" +
            "FROM orders\n" +
            "INNER JOIN\n" +
            "order_status ON order_status.order_id = orders.order_id\n" +
            "LEFT JOIN\n" +
            "payment ON payment.created_by = orders.created_by \n" +
            "WHERE order_status != \"REJECTED\" \n" +
            "AND orders.created_by = (SELECT user_name from user WHERE user_id = (?)) \n" +
            "AND (payment.payment_status IS NULL OR payment.payment_status = \"PENDING\")\n" +
            "AND DATE(orders.created_at) = curdate(); ";

    public String getRetrieveTotalAmount() {
        return retrieveTotalAmount;
    }

    public String getInsertGcashPayment() {
        return insertGcashPayment;
    }

    public String getInsertAddOnsIntoOrderItems() {
        return insertAddOnsIntoOrderItems;
    }

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
