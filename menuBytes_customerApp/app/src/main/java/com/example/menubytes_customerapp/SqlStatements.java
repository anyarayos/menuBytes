package com.example.menubytes_customerapp;

public class SqlStatements {

    private String retrieveProductsByID = "SELECT PRODUCT_IMG, PRODUCT_NAME, PRODUCT_PRICE, " +
            "PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_ID = (?)";

    private String retrieveProductsByCategory = "SELECT PRODUCT_ID, PRODUCT_IMG, PRODUCT_NAME, " +
            "PRODUCT_PRICE, PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_CATEGORY = (?)AND \n" +
            "product_name != \"Shawarma All Meat\" AND product_availability = \"available\"";

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

//    private String insertIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity,product_bundle,has_addons)\n" +
//            "VALUES((?),(?),(?),(?),(?));";

    private String insertIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity,product_bundle,has_addons, flavors)\n" +
            "VALUES((?),(?),(?),(?),(?),(?));";

    private String insertAddOnsIntoOrderItems = "INSERT INTO order_items(order_id,product_id,quantity,product_bundle)\n" +
            "VALUES((?),(SELECT product_id from product where product_name = (?)),(?),(?));";

    private String insertGcashPayment = "INSERT INTO payment \n" +
            "(\n" +
            "amount_due,\n" +
            "payment_amount,\n" +
            "payment_method,\n" +
            "payment_status,\n" +
            "created_at,\n" +
            "created_by)\n" +
            "VALUES\n" +
            "(\n" +
            "(?),\n" +
            "(0), \n" +
            "\"GCASH\",\n" +
            "\"PENDING\",\n" +
            "current_timestamp(),\n" +
            "(SELECT user_name from user where user_id = (?))\n" +
            ");";

    private String insertGcashPaymentV2 = "INSERT INTO payment\n" +
            "(\n" +
            "payment_amount,\n" +
            "amount_due,\n" +
            "payment_method,\n" +
            "payment_status,\n" +
            "created_by,\n" +
            "remarks\n" +
            ")\n" +
            "VALUES(\n" +
            "(0),\n" +
            "(?),\n" +
            "(\"GCASH\"),\n" +
            "(\"PENDING\"),\n" +
            "(SELECT user_name from user where user_id = (?)),\n" +
            "(?)\n" +
            ");";

    public String getInsertGcashPaymentV2() {
        return insertGcashPaymentV2;
    }

    private String InsertCashPayment = "INSERT INTO payment \n" +
            "(\n" +
            "amount_due,\n" +
            "payment_amount,\n" +
            "payment_method,\n" +
            "payment_status,\n" +
            "created_at,\n" +
            "created_by)\n" +
            "VALUES\n" +
            "(\n" +
            "(?),\n" +
            "(?), \n" +
            "\"CASH\",\n" +
            "\"PENDING\",\n" +
            "current_timestamp(),\n" +
            "(SELECT user_name from user where user_id = (?))\n" +
            ");";

    private String checkPaymentCount = "SELECT COUNT(payment_id) FROM payment\n" +
            "WHERE created_by = (SELECT user_name from user WHERE user_id = (?));";

    private String checkPendingOrders = "SELECT COUNT(orders.order_id) FROM orders\n" +
            "INNER JOIN order_status ON orders.order_id = order_status.order_id\n" +
            "WHERE orders.created_by = (SELECT user_name from user WHERE user_id = (?))\n" +
            "AND (order_status.order_status = \"IN QUEUE\" OR order_status.order_status = \"PREPARING\")\n" +
            ";";
    private String checkCompletedOrders = "SELECT COUNT(orders.order_id) FROM orders\n" +
            "INNER JOIN order_status ON orders.order_id = order_status.order_id\n" +
            "WHERE orders.created_by = (SELECT user_name from user WHERE user_id = (?))\n" +
            "AND (order_status.order_status = \"COMPLETED\")\n" +
            ";";

    private String retrieveTotalAmount = "SELECT \n" +
            "SUM(orders.total) AS total_amount\n" +
            "FROM orders\n" +
            "INNER JOIN\n" +
            "order_status ON order_status.order_id = orders.order_id\n" +
            "WHERE order_status = \"COMPLETED\" \n" +
            "AND orders.created_by = (SELECT user_name from user WHERE user_id = (?)) \n" +
            "AND DATE(orders.created_at) = curdate();\n;";

    private String retrieveAllPendingOrdersByTable = "SELECT IF((order_status.order_status=\"PREPARING\"),\"IN THE KITCHEN\", " +
            "order_status.order_status)," +
            " orders.order_id, " +
            "orderitems.qty, " +
            "orders.total\n" +
            "FROM orders\n" +
            "INNER JOIN\n" +
            "order_status ON order_status.order_id = orders.order_id\n" +
            "JOIN\n" +
            "(SELECT order_id, SUM(quantity) AS qty FROM order_items WHERE order_items.product_id != ((SELECT product_id FROM product WHERE product_name = \"Shawarma All Meat\")) GROUP BY order_id)\n" +
            "AS orderitems ON orderitems.order_id = orders.order_id\n" +
            "WHERE \n" +
            "orders.created_by = ((SELECT user_name FROM user WHERE user_id = (?))) \n" +
            "AND (order_status = \"PREPARING\" OR order_status = \"IN QUEUE\");";


//    private String retrieveAllCompletedOrdersByTable = "SELECT order_items.quantity,  \n" +
//            "IF(order_items.product_bundle,(CONCAT(\"B1G1 \",product.product_name)),product.product_name)\n" +
//            ",IF(order_items.product_bundle,product.product_bundle,product.product_price)\n" +
//            "FROM order_items\n" +
//            "INNER JOIN\n" +
//            "product ON order_items.product_id = product.product_id\n" +
//            "INNER JOIN\n" +
//            "orders ON order_items.order_id = orders.order_id\n" +
//            "INNER JOIN\n" +
//            "order_status ON order_items.order_id = order_status.order_id\n" +
//            "WHERE\n" +
//            "orders.created_by = ((SELECT user_name FROM user WHERE user_id = (?))) AND order_status = \"COMPLETED\";";

    private String retrieveAllCompletedOrdersByTable = "\n" +
        "SELECT order_items.quantity, \n" +
        "            IF(order_items.product_bundle,(CONCAT(\"B1G1 \",product.product_name)),product.product_name)\n" +
        "            ,IF(order_items.product_bundle,product.product_bundle,product.product_price),\n" +
        "            (cast((order_items.quantity) AS DECIMAL)*(IF(order_items.product_bundle,product.product_bundle,product.product_price)))\n" +
        "            ,order_items.has_addons, order_items.flavors FROM order_items\n" +
        "            INNER JOIN\n" +
        "            product ON order_items.product_id = product.product_id\n" +
        "            INNER JOIN\n" +
        "            orders ON order_items.order_id = orders.order_id\n" +
        "            INNER JOIN\n" +
        "            order_status ON order_items.order_id = order_status.order_id\n" +
        "            WHERE\n" +
        "            orders.created_by = ((SELECT user_name FROM user WHERE user_id = (?))) AND order_items.product_id != ((SELECT product_id FROM product WHERE product_name = \"Shawarma All Meat\")) AND order_status = \"COMPLETED\";";
//    private String retrieveAllCompletedOrdersByTable = "SELECT order_items.quantity, \n" +
//        "IF(order_items.product_bundle,(CONCAT(\"B1G1\",product.product_name)),product.product_name) AS NAME,\n" +
//        "IF(order_items.product_bundle,\n" +
//        "\tIF(order_items.has_addons,(product.product_bundle+20),(product.product_bundle)),\n" +
//        "\tIF(order_items.has_addons,(product.product_price+10),(product.product_price))) AS PRICE,\n" +
//        "(cast((order_items.quantity) AS DECIMAL)*(IF(order_items.product_bundle,\n" +
//        "\tIF(order_items.has_addons,(product.product_bundle+20),(product.product_bundle)),\n" +
//        "\tIF(order_items.has_addons,(product.product_price+10),(product.product_price))))) AS TOTAL_PRICE\n" +
//        ",order_items.has_addons \n" +
//        "FROM order_items\n" +
//        "INNER JOIN\n" +
//        "product ON order_items.product_id = product.product_id\n" +
//        "INNER JOIN\n" +
//        "orders ON order_items.order_id = orders.order_id\n" +
//        "INNER JOIN\n" +
//        "order_status ON order_items.order_id = order_status.order_id\n" +
//        "WHERE\n" +
//        "orders.created_by = ((SELECT user_name FROM user WHERE user_id = (?))) \n" +
//        "AND \n" +
//        "order_status = \"COMPLETED\"\n" +
//        "AND \n" +
//        "order_items.product_id != (SELECT product_id FROM product WHERE product_name = \"Shawarma All Meat\");";

    private String retrieveOrderBreakdownUsingOrderID = "SELECT IF((order_items.product_bundle),CONCAT(\"B1G1 \",product.product_name),product.product_name) AS name,\n" +
            "IF((order_items.product_bundle),product.product_bundle,product.product_price)\n" +
            ",order_items.quantity, order_items.has_addons, order_items.flavors\n" +
            "FROM order_items\n" +
            "INNER JOIN\n" +
            "product ON order_items.product_id = product.product_id\n" +
            "INNER JOIN\n" +
            "orders ON order_items.order_id = orders.order_id\n"  +
            "WHERE order_items.order_id = (?) AND order_items.product_id != ((SELECT product_id FROM product WHERE product_name = \"Shawarma All Meat\")) AND DATE(orders.created_at) = curdate()\n" +
            "";

    private String validateGcashComplete = "SELECT payment_id FROM payment \n" +
            "WHERE payment_id = (?)\n" +
            "AND payment_status = \"COMPLETE\"\n" +
            "AND DATE(completed_at) = current_date()\n" +
            "            ;";
    private String validatePaymentRejected ="SELECT payment_id FROM payment \n" +
            "WHERE payment_id = (?)\n" +
            "AND payment_status = \"REJECTED\"\n" +
            "            ;";

    private String checkUsernameExistence = "SELECT user_id FROM user WHERE (user_name = (?) and user_type = 'customer');";

    private String checkUsernamePassword = "SELECT user_id FROM user WHERE user_name = (?) AND password = (?);";

    private String setTableName = "SELECT replace(user_name, \"_\", \" \") FROM user WHERE user_id = (?);";

    private String updateLogInTime = "UPDATE user\n" +
            "SET log_in = current_timestamp()\n" +
            "WHERE user_id = (?)\n" +
            ";";
    private String updateLogOutTime = "UPDATE user\n" +
            "SET log_out = current_timestamp()\n" +
            "WHERE user_id = (?)\n" +
            ";";

    public String getUpdateLogInTime() {
        return updateLogInTime;
    }

    public String getUpdateLogOutTime() {
        return updateLogOutTime;
    }

    public String getSetTableName() {
        return setTableName;
    }

    public String getValidateGcashComplete() {
        return validateGcashComplete;
    }

    public String getCheckUsernameExistence() {
        return checkUsernameExistence;
    }

    public String getCheckUsernamePassword() {
        return checkUsernamePassword;
    }

    public String getValidatePaymentRejected() {
        return validatePaymentRejected;
    }

    public String getValidatePaymentComplete() {
        return validateGcashComplete;
    }

    public String getRetrieveOrderBreakdownUsingOrderID() {
        return retrieveOrderBreakdownUsingOrderID;
    }

    public String getCheckCompletedOrders() {
        return checkCompletedOrders;
    }

    public String getCheckPendingOrders() {
        return checkPendingOrders;
    }

    public String getCheckPaymentCount() {
        return checkPaymentCount;
    }

    public String getInsertCashPayment() {
        return InsertCashPayment;
    }

    public String getRetrieveAllPendingOrdersByTable() {
        return retrieveAllPendingOrdersByTable;
    }


    public String getRetrieveAllCompletedOrdersByTable() {
        return retrieveAllCompletedOrdersByTable;
    }

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
