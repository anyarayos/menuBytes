package com.example.menubytes_customerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task extends AsyncTask<String, String, Object> {



    private AsyncResponse asyncResponse;
    private String method;
    private Connection connection;
    private PreparedStatement statement;
    private SqlStatements sqlStatements = new SqlStatements();
    private ResultSet resultSet;
    private Context context;
    private String TAG = "TASK_DEBUG";

    private ArrayList<ProductListClass> productListClassArrayList = new ArrayList<>();
    private ArrayList<OrderListClass> orderList = new ArrayList<>();
    private int order_id;

    public final static String RETRIEVE_PRODUCTS_BY_CATEGORY2="retrieveProductsByCategory2";
    public final static String RETRIEVE_PRODUCTS_BY_ID2 = "retrieveProductsByID2";
    public final static String INSERT_INTO_ORDERS = "insertIntoOrders";
    public final static String INSERT_INTO_ORDER_STATUS = "insertIntoOrderStatus";
    public final static String INSERT_INTO_ORDER_ITEMS = "insertIntoOrderItems";
    public final static String INSERT_ADDONS_INTO_ORDER_ITEMS = "insertAddOnsIntoOrderItems";
    public final static String RETRIEVE_TOTAL_AMOUNT = "RetrieveTotalAmount";
    public final static String INSERT_GCASH_PAYMENT = "InsertGcashPayment";
    public final static String INSERT_GCASH_PAYMENT2 = "InsertGcashPayment2";
    public final static String INSERT_CASH_PAYMENT = "InsertCashPayment";
    public final static String DISPLAY_PENDING_ORDERS = "retrieveAllPendingOrdersByTable" ;
    public final static String DISPLAY_COMPLETED_ORDERS = "retrieveAllCompletedOrdersByTable";
    public static final String CHECK_PAYMENT_COUNT = "checkPaymentCount";
    public static final String CHECK_PENDING_COUNT = "checkPendingCount" ;
    public static final String CHECK_COMPLETED_COUNT = "checkCompletedCount" ;
    public static final String RETRIEVE_ORDER_BREAKDOWN = "retrieveOrderBreakdownUsingOrderID";
    public static final String VALIDATE_PAYMENT_COMPLETE = "validateGcashComplete";
    public static final String VALIDATE_PAYMENT_REJECTED = "validatePaymentRejected";
    public static final String CHECK_USER_NAME_EXISTENCE = "checkUsernameExistence";
    public static final String CHECK_USER_NAME_PASSWORD = "checkUsernamePassword";
    public static final String SET_TABLE_NAME = "setTableName" ;
    public static final String UPDATE_LOGIN_TIME = " updateLogInTime" ;
    public static final String UPDATE_LOGOUT_TIME = "updateLogOutTime";
    public static final String CHECK_PASSWORD = "checkPassword";
    public static final String UPDATE_PAYMENT_TABLE_NAME = "updatePaymentTableName";
    public static final String UPDATE_ORDERS_TABLE_NAME = "updateOrdersTableName";
    public static final String GET_AMOUNT_CHANGE = "getAmountChange";
    public static final String ASK_ASSISTANCE = "askAssistance" ;
    public static final String CHECK_GCASH_AVAILABLE = "checkGCashAvailable";
    public static final String ASK_FOR_OR = "askForOR";
    public static final String CHECK_LOGIN_STATUS = "CHECK_LOGIN_STATUS";
    public static final String SET_STATUS_LOGGEDIN = "LOGGEDIN";
    public static final String SET_STATUS_LOGGEDOUT = "LOGGEDOUT";
    public static final String GET_GRAND_TOTAL = "getgrandtotal";

    public Task(String method) {
        this.method = method;
    }

    public Task(String method, AsyncResponse asyncResponse) {
        this.method = method;
        this.asyncResponse = asyncResponse;
    }

    public Task(Context context, String method, AsyncResponse asyncResponse) {
        this.context = context;
        this.method = method;
        this.asyncResponse = asyncResponse;
    }

    private void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://192.168.1.6:3306/menubytes", "admin", "admin");
        } catch (Exception e) {
            Log.i("DATABASE CONNECTION:", e.toString());
        }
    }

    public static  void disconnect(ResultSet rs, PreparedStatement stat, Connection cn){
        String TAG = "disconnect";
        try{
            if(rs!=null) rs.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "resultset disconnection error ");
        }
        try{
            if(stat!=null) stat.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "statement disconnection error ");

        }
        try{
            if(cn!=null) cn.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "connection disconnection error ");

        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected Object doInBackground(String... params) {
        setConnection();

        if(connection!=null){
            try {
                if (method.equals(RETRIEVE_PRODUCTS_BY_CATEGORY2)) {
                    statement = connection.prepareStatement(sqlStatements.getRetrieveProductsByCategory2());
                    String category =params[0];
                    statement.setString(1,category);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO PRODUCT DATA FOUND");
                    } else {
                        Log.d(TAG, "PRODUCT DATA FOUND");
                        while (resultSet.next()) {
                            productListClassArrayList.add(new ProductListClass(
                                    resultSet.getInt(1),
                                    resultSet.getBytes(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6),
                                    resultSet.getString(7)
                            ));
                        }
                        return productListClassArrayList;
                    }
                }

                if (method.equals(RETRIEVE_PRODUCTS_BY_ID2)) {
                    statement = connection.prepareStatement(sqlStatements.getRetrieveProductsByID2());
                    int id =Integer.valueOf(params[0]);
                    statement.setInt(1,id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            productListClassArrayList.add(new ProductListClass(resultSet.getBytes(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5)
                            ));
                        }
                        return productListClassArrayList;
                    }
                }
                if (method.equals(INSERT_INTO_ORDERS)) {
                    statement = connection.prepareStatement(sqlStatements.getInsertIntoOrders(), Statement.RETURN_GENERATED_KEYS);
                    double total = Double.valueOf(params[0]);
                    String user_id = params[1];
                    String created_at = returnDateTime();
                    statement.setInt(1,Integer.valueOf(user_id));
                    statement.setDouble(2,total);
                    statement.setString(3,created_at);
                    statement.setInt(4,Integer.valueOf(user_id));
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO ID_DATA FOUND");
                    } else {
                        Log.d(TAG, "ID_DATA FOUND");}
                    if(resultSet.next()){
                        order_id = resultSet.getInt(1);
                    }

//                int rowsAffected = statement.executeUpdate();
//                if(rowsAffected > 0) {
//                    Log.d(TAG, "doInBackground: INSERT_INTO_ORDERS Successful! ");
//                }
//                else{
//                    Log.d(TAG, "doInBackground: INSERT_INTO_ORDERS UnSuccessful! ");
//                }
                    return order_id;
                }
                if(method.equals(INSERT_INTO_ORDER_STATUS)){
                    statement = connection.prepareStatement(sqlStatements.getInsertIntoOrderStatus());
                    int order_id = Integer.valueOf(params[0]);
                    String user_id = params[1];
                    statement.setInt(1, order_id);
                    statement.setString(2,"IN QUEUE");
                    statement.setInt(3,order_id);
                    statement.setInt(4,Integer.valueOf(user_id));
                    statement.executeUpdate();
                }

                if(method.equals(INSERT_INTO_ORDER_ITEMS)){
                    statement = connection.prepareStatement(sqlStatements.getInsertIntoOrderItems());
                    int order_id = Integer.valueOf(params[0]);
                    int product_id = Integer.valueOf(params[1]);
                    String quantity = params[2];
                    boolean product_bundle = Boolean.valueOf(params[3]);
                    boolean has_addons = Boolean.parseBoolean(params[4]);
                    String flavors = params[5];
                    statement.setInt(1, order_id);
                    statement.setInt(2,product_id);
                    statement.setString(3,quantity);
                    statement.setBoolean(4,product_bundle);
                    statement.setBoolean(5, has_addons);
                    statement.setString(6, flavors);
                    statement.executeUpdate();
                }
                if(method.equals(INSERT_ADDONS_INTO_ORDER_ITEMS)){
                    statement = connection.prepareStatement(sqlStatements.getInsertAddOnsIntoOrderItems());
                    int order_id = Integer.valueOf(params[0]);
                    String product_name = params[1];
                    String quantity = params[2];
                    statement.setInt(1, order_id);
                    statement.setString(2,product_name);
                    statement.setString(3,quantity);
                    statement.setBoolean(4,false);
                    statement.executeUpdate();
                }

                if(method.equals(RETRIEVE_TOTAL_AMOUNT)){
                    statement = connection.prepareStatement(sqlStatements.getRetrieveTotalAmount());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();
                    String total_amount = "";
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            total_amount = resultSet.getString(1);
                        }
                        return total_amount;
                    }
                }

                if(method.equals(CHECK_PAYMENT_COUNT)){
                    int count = 0;
                    statement = connection.prepareStatement(sqlStatements.getCheckPaymentCount());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();
                    String total_amount = "";
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            count = resultSet.getInt(1);
                        }
                        return count;
                    }
                }

                if(method.equals(CHECK_PENDING_COUNT)){
                    int count = 0;
                    statement = connection.prepareStatement(sqlStatements.getCheckPendingOrders());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            count = resultSet.getInt(1);
                        }
                        return count;
                    }
                }

                if(method.equals(CHECK_COMPLETED_COUNT)){
                    int count = 0;
                    statement = connection.prepareStatement(sqlStatements.getCheckCompletedOrders());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            count = resultSet.getInt(1);
                        }
                        return count;
                    }
                }

                if(method.equals(INSERT_GCASH_PAYMENT)){
                    statement = connection.prepareStatement(sqlStatements.getInsertGcashPayment());
                    String totalAmount = params[0];
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setDouble(1,Double.valueOf(totalAmount));
                    statement.setInt(2, user_id);
                    statement.executeUpdate();
                }

//                if(method.equals(INSERT_GCASH_PAYMENT2)){
//                    statement = connection.prepareStatement(sqlStatements.getInsertGcashPaymentV2(), Statement.RETURN_GENERATED_KEYS);
//                    String totalAmount = params[0];
//                    String remarks = params[1];
//                    String payment_id = null;
//                    int user_id = 0;
//                    if(Utils.getInstance().getUser_id()!=null){
//                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
//                    }
//                    statement.setDouble(1,Double.valueOf(totalAmount));
//                    statement.setInt(2, user_id);
//                    statement.setString(3,remarks);
//                    statement.executeUpdate();
//
//                    resultSet = statement.getGeneratedKeys();
//                    if (!resultSet.isBeforeFirst()) {
//                        Log.d(TAG, "GCASH NO ID_DATA FOUND");
//                    } else {
//                        Log.d(TAG, "GCASH ID_DATA FOUND");}
//                    if(resultSet.next()){
//                        payment_id = resultSet.getString(1);
//                    }
//                    return payment_id;
//                }
                //subtotal,amount_due,remarks,discount_id,discount_type,discount_amount
                if(method.equals(INSERT_GCASH_PAYMENT2)){
                    statement = connection.prepareStatement(sqlStatements.getInsertGcashPaymentV2(), Statement.RETURN_GENERATED_KEYS);
                    String subtotal = params[0];
                    String amount_due = params[1];
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    String remarks = params[2];
                    String discount_id = params[3];
                    String discount_type = params[4];
                    String discount_amount = params[5];
                    String payment_id = null;

                    statement.setDouble(1,Double.valueOf(subtotal));
                    statement.setDouble(2,Double.valueOf(amount_due));
                    statement.setInt(3,Integer.valueOf(user_id));
                    statement.setString(4,remarks);
                    statement.setInt(5,Integer.valueOf(discount_id));
                    statement.setString(6,discount_type);
                    statement.setDouble(7, Double.valueOf(discount_amount));
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "GCASH NO ID_DATA FOUND");
                    } else {
                        Log.d(TAG, "GCASH ID_DATA FOUND");}
                    if(resultSet.next()){
                        payment_id = resultSet.getString(1);
                    }
                    return payment_id;
                }

//                if(method.equals(INSERT_CASH_PAYMENT)){
//                    statement = connection.prepareStatement(sqlStatements.getInsertCashPayment(),Statement.RETURN_GENERATED_KEYS);
//                    String totalAmount = params[0];
//                    String paymentAmount = params[1];
//                    String payment_id = null;
//                    int user_id = 0;
//                    if(Utils.getInstance().getUser_id()!=null){
//                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
//                    }
//                    statement.setDouble(1,Double.valueOf(totalAmount));
//                    statement.setDouble(2,Double.valueOf(paymentAmount));
//                    statement.setInt(3, user_id);
//                    statement.executeUpdate();
//
//                    resultSet = statement.getGeneratedKeys();
//                    if (!resultSet.isBeforeFirst()) {
//                        Log.d(TAG, "CASH PAYMENT NO ID_DATA FOUND");
//                    } else {
//                        Log.d(TAG, "CASH PAYMENT ID_DATA FOUND");}
//                    if(resultSet.next()){
//                        payment_id = resultSet.getString(1);
//                    }
//                    return payment_id;
//                }

                //subtotal,amount_due,payment_amount,discount_id,discount_type,discount_amount
                if(method.equals(INSERT_CASH_PAYMENT)){
                    statement = connection.prepareStatement(sqlStatements.getInsertCashPayment(),Statement.RETURN_GENERATED_KEYS);
                    String subtotal = params[0];
                    String amount_due = params[1];
                    String payment_amount = params[2];
                    String discount_id = params[3];
                    String discount_type = params[4];
                    String discount_amount = params[5];
                    String payment_id = null;
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setDouble(1,Double.valueOf(subtotal));
                    statement.setDouble(2,Double.valueOf(amount_due));
                    statement.setDouble(3,Double.valueOf(payment_amount));
                    statement.setInt(4, user_id);
                    statement.setString(5,discount_id);
                    statement.setString(6,discount_type);
                    statement.setString(7,discount_amount);
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CASH PAYMENT NO ID_DATA FOUND");
                    } else {
                        Log.d(TAG, "CASH PAYMENT ID_DATA FOUND");}
                    if(resultSet.next()){
                        payment_id = resultSet.getString(1);
                    }
                    return payment_id;
                }


                if(method.equals(DISPLAY_PENDING_ORDERS)){
                    ArrayList <PendingListClass> pendingArrayList = new ArrayList<>();
                    statement = connection.prepareStatement(sqlStatements.getRetrieveAllPendingOrdersByTable());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            pendingArrayList.add(new PendingListClass(
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4)));
                        }
                        return pendingArrayList;
                    }
                }

                if(method.equals(DISPLAY_COMPLETED_ORDERS)){
                    ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
                    statement = connection.prepareStatement(sqlStatements.getRetrieveAllCompletedOrdersByTable());
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setInt(1,user_id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "DISPLAY_COMPLETED_ORDERS: NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DISPLAY_COMPLETED_ORDERS: DATA FOUND");
                        while (resultSet.next()) {
                            completedOrdersArrayList.add(new OrderListClass(
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getBoolean(5),
                                    resultSet.getString(6)
                                    ));
                        }
                        return completedOrdersArrayList;
                    }
                }

                if(method.equals(RETRIEVE_ORDER_BREAKDOWN)){
                    ArrayList<PendingOrderSumListClass> pendingOrderSumArrayList = new ArrayList<>();
                    statement = connection.prepareStatement(sqlStatements.getRetrieveOrderBreakdownUsingOrderID());
                    int order_id=0;
                    order_id = Integer.valueOf(params[0]);
                    if(order_id != 0){
                        statement.setInt(1,order_id);
                        resultSet = statement.executeQuery();
                    }

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                                pendingOrderSumArrayList.add(
                                  new PendingOrderSumListClass(
                                          resultSet.getString(1),
                                          resultSet.getString(2),
                                          resultSet.getString(3),
                                          resultSet.getBoolean(4),
                                          resultSet.getString(5)
                                  )
                                );
                        }
                        return pendingOrderSumArrayList;
                    }
                }

                if(method.equals(VALIDATE_PAYMENT_COMPLETE)){
                    statement = connection.prepareStatement(sqlStatements.getValidatePaymentComplete());
                    String payment_id = params[0];
                    statement.setInt(1,Integer.valueOf(payment_id));
                    String result = null;
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "VALIDATE_PAYMENT_COMPLETE : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "VALIDATE_PAYMENT_COMPLETE : DATA FOUND");
                        while (resultSet.next()) {
                            result = resultSet.getString(1);
                        }
                        return result;
                    }
                }

                if(method.equals(VALIDATE_PAYMENT_REJECTED)){
                    statement = connection.prepareStatement(sqlStatements.getValidatePaymentRejected());
                    String payment_id = params[0];
                    statement.setInt(1,Integer.valueOf(payment_id));
                    String result = null;
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "VALIDATE_REJECTED : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "VALIDATE_REJECTED : DATA FOUND");
                        while (resultSet.next()) {
                            result = resultSet.getString(1);
                        }
                        return result;
                    }
                }

                if(method.equals(CHECK_USER_NAME_EXISTENCE)){
                    statement = connection.prepareStatement(sqlStatements.getCheckUsernameExistence());
                    String user_id = null;
                    String username = params[0];
                    statement.setString(1, username);

                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CHECK_USER_NAME_EXISTENCE : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "CHECK_USER_NAME_EXISTENCE: DATA FOUND");
                        while (resultSet.next()) {
                            user_id = resultSet.getString(1);
                        }
                        return user_id;
                    }
                }

                if(method.equals(CHECK_USER_NAME_PASSWORD)){
                    statement = connection.prepareStatement(sqlStatements.getCheckUsernamePassword());
                    String user_id = null;
                    String username = params[0];
                    String password = params[1];
                    statement.setString(1, username);
                    statement.setString(2, password);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CHECK_USER_NAME_PASSWORD : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "CHECK_USER_NAME_PASSWORD: DATA FOUND");
                        while (resultSet.next()) {
                            user_id = resultSet.getString(1);
                        }
                        return user_id;
                    }
                }

                if(method.equals(SET_TABLE_NAME)) {
                    statement = connection.prepareStatement(sqlStatements.getSetTableName());
                    String user_id = null;
                    String table_name = null;
                    user_id = params[0];
                    statement.setString(1, user_id);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "SET_TABLE_NAME : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "SET_TABLE_NAME: DATA FOUND");
                        while (resultSet.next()) {
                            table_name = resultSet.getString(1);
                        }
                        return table_name;
                    }
                }

                if(method.equals(UPDATE_LOGIN_TIME)){
                    statement = connection.prepareStatement(sqlStatements.getUpdateLogInTime());
                    String user_id = params[0];
                    statement.setString(1,user_id);
                    statement.executeUpdate();
                }

                if(method.equals(UPDATE_LOGOUT_TIME)){
                    statement = connection.prepareStatement(sqlStatements.getUpdateLogOutTime());
                    String user_id = params[0];
                    statement.setString(1,user_id);
                    statement.executeUpdate();
                }

                if(method.equals(CHECK_PASSWORD)){
                    statement = connection.prepareStatement(sqlStatements.getCheckPassword());
                    String user_id = null;
                    String user_id_input = params[0];
                    String password = params[1];
                    statement.setString(1, user_id_input);
                    statement.setString(2, password);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CHECK_PASSWORD) : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "CHECK_PASSWORD): DATA FOUND");
                        while (resultSet.next()) {
                            user_id = resultSet.getString(1);
                        }
                        return user_id;
                    }
                }

                if(method.equals(UPDATE_PAYMENT_TABLE_NAME)){
                    statement = connection.prepareStatement(sqlStatements.getUpdatePaymentTableName());
                    String payment_id = params[0];
                    statement.setString(1,payment_id);
                    Log.d(TAG, "doInBackground: UPDATE_PAYMENT_TABLE_NAME");
                    statement.executeUpdate();
                }

                if(method.equals(UPDATE_ORDERS_TABLE_NAME)){
                    statement = connection.prepareStatement(sqlStatements.getUpdateOrdersTableName());
                    String user_id = params[0];
                    statement.setString(1,user_id);
                    Log.d(TAG, "doInBackground: UPDATE_ORDERS_TABLE_NAME");
                    statement.executeUpdate();
                }

                if(method.equals(GET_AMOUNT_CHANGE)){
                    statement = connection.prepareStatement(sqlStatements.getGetAmountAndChange());
                    ArrayList<Payment> payments = new ArrayList<>();
                    String payment_id = params[0];
                    statement.setString(1, payment_id);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "GET_AMOUNT_CHANGE : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "GET_AMOUNT_CHANGE: DATA FOUND");
                        while (resultSet.next()) {
                            payments.add(new Payment(resultSet.getString(1),
                                    resultSet.getString(2))
                            );
                        }
                        return payments;
                    }
                }

                if(method.equals(GET_GRAND_TOTAL)){
                    statement = connection.prepareStatement(sqlStatements.getRetrieveGrandTotal());
                    ArrayList<Payment> payments = new ArrayList<>();
                    String payment_id = params[0];
                    statement.setString(1, payment_id);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "GET_GRAND_TOTAL : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "GET_GRAND_TOTAL: DATA FOUND");
                        while (resultSet.next()) {
                            payments.add(new Payment(
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4))
                            );
                        }
                        return payments;
                    }
                }

                if(method.equals(ASK_ASSISTANCE)){
                    statement = connection.prepareStatement(sqlStatements.getAskAssistance());
                    String user_id = Utils.getInstance().getUser_id();
                    statement.setString(1,user_id);
                    Log.d(TAG, "ASK_ASSISTANCE");
                    statement.executeUpdate();
                }

                if(method.equals(ASK_FOR_OR)){
                    statement = connection.prepareStatement(sqlStatements.getAskForOR());
                    String user_id = Utils.getInstance().getUser_id();
                    statement.setString(1,user_id);
                    Log.d(TAG, "ASK_FOR_OR");
                    statement.executeUpdate();
                }

                if(method.equals(CHECK_GCASH_AVAILABLE)){
                    statement = connection.prepareStatement(sqlStatements.getCheckGcashAvailability());
                    byte[] bytes = null;
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CHECK_GCASH_AVAILABLE) : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "CHECK_GCASH_AVAILABLE) : DATA FOUND");
                        while (resultSet.next()) {
                            bytes = resultSet.getBytes(1);
                        }
                        return bytes;
                    }
                }

                //TODO: update status
                if(method.equals(CHECK_LOGIN_STATUS)){
                    statement = connection.prepareStatement(sqlStatements.getCheck_login_status());
                    String user_id = params[0];
                    statement.setString(1, user_id);
                    String status = null;
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "CHECK_LOGIN_STATUS) : NO DATA FOUND");
                    } else {
                        Log.d(TAG, "CHECK_LOGIN_STATUS) : DATA FOUND");
                        while (resultSet.next()) {
                            status = resultSet.getString(1);
                        }
                        Log.d(TAG, "CHECK_LOGIN_STATUS:  " + status);
                        return status;
                    }
                }

                if(method.equals(SET_STATUS_LOGGEDIN)){
                    statement = connection.prepareStatement(sqlStatements.getSet_status_true());
                    String user_id = params[0];
                    statement.setString(1, user_id);
                    statement.executeUpdate();
                }

                if(method.equals(SET_STATUS_LOGGEDOUT)){
                    statement = connection.prepareStatement(sqlStatements.getSet_status_false());
                    String user_id = params[0];
                    statement.setString(1, user_id);
                    statement.executeUpdate();
                }

                disconnect(resultSet,statement,connection);
            }


            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try {
            super.onPostExecute(o);
                if(o!=null){
                    asyncResponse.onFinish(o);
                }else{asyncResponse.onFinish(null);}
            }
        catch (Exception e){
            Log.i("onPostExecute", e.toString());
        }
    }

    private static String returnDateTime(){
        DateTimeFormatter dtf = null;
        String dateTime=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            dateTime = dtf.format(now);
        }
        return dateTime;
    }
}
