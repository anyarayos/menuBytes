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

    public final static String RETRIEVE_PRODUCTS_BY_CATEGORY="retrieveProductsByCategory";
    public final static String RETRIEVE_PRODUCTS_BY_ID = "retrieveProductsByID";
    public final static String INSERT_INTO_ORDERS = "insertIntoOrders";
    public final static String INSERT_INTO_ORDER_STATUS = "insertIntoOrderStatus";
    public final static String INSERT_INTO_ORDER_ITEMS = "insertIntoOrderItems";
    public final static String INSERT_ADDONS_INTO_ORDER_ITEMS = "insertAddOnsIntoOrderItems";
    public final static String RETRIEVE_ORDERS_USING_ID_STATUS = "retrieveOrderItemsUsingIdStatus";
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
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.210:3306/menubytes", "admin", "admin");
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
                if (method.equals(RETRIEVE_PRODUCTS_BY_CATEGORY)) {
                    statement = connection.prepareStatement(sqlStatements.getRetrieveProductsByCategory());
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
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6)
                            ));
                        }
                        return productListClassArrayList;
                    }
                }
                if (method.equals(RETRIEVE_PRODUCTS_BY_ID)) {
                    statement = connection.prepareStatement(sqlStatements.getRetrieveProductsByID());
                    int id =Integer.valueOf(params[0]);
                    statement.setInt(1,id);
                    resultSet = statement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            productListClassArrayList.add(new ProductListClass(resultSet.getString(1),
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
                    statement.setInt(1, order_id);
                    statement.setInt(2,product_id);
                    statement.setString(3,quantity);
                    statement.setBoolean(4,product_bundle);
                    statement.setBoolean(5, has_addons);
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

                //TODO: use to get reference #
                if(method.equals(INSERT_GCASH_PAYMENT2)){
                    statement = connection.prepareStatement(sqlStatements.getInsertGcashPaymentV2());
                    String totalAmount = params[0];
                    String remarks = params[1];
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setDouble(1,Double.valueOf(totalAmount));
                    statement.setInt(2, user_id);
                    statement.setString(3,remarks);
                    statement.executeUpdate();
                }

                if(method.equals(INSERT_CASH_PAYMENT)){
                    statement = connection.prepareStatement(sqlStatements.getInsertCashPayment());
                    String totalAmount = params[0];
                    int user_id = 0;
                    if(Utils.getInstance().getUser_id()!=null){
                        user_id = Integer.valueOf(Utils.getInstance().getUser_id());
                    }
                    statement.setDouble(1,Double.valueOf(totalAmount));
                    statement.setInt(2, user_id);
                    statement.executeUpdate();
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
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            completedOrdersArrayList.add(new OrderListClass(
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getBoolean(5)
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
                                          resultSet.getBoolean(4)
                                  )
                                );
                        }
                        return pendingOrderSumArrayList;
                    }
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
