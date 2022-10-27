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
    private int order_id;

    public final static String RETRIEVE_PRODUCTS_BY_CATEGORY="retrieveProductsByCategory";
    public final static String RETRIEVE_PRODUCTS_BY_ID = "retrieveProductsByID";
    public final static String INSERT_INTO_ORDERS = "insertIntoOrders";
    public final static String INSERT_INTO_ORDER_STATUS = "insertIntoOrderStatus";
    public final static String INSERT_INTO_ORDER_ITEMS = "insertIntoOrderItems";
    public final static String INSERT_ADDONS_INTO_ORDER_ITEMS = "insertAddOnsIntoOrderItems";

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
            connection = DriverManager.getConnection("jdbc:mysql://aws-simplified.ccnp1cnd7apy.ap-northeast-1.rds.amazonaws.com:3306/menubytes", "admin", "P0Y9aixM7jUZr6Cg");
        } catch (Exception e) {
            Log.i("DATABASE CONNECTION:", e.toString());
        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected Object doInBackground(String... params) {
        setConnection();
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
                String created_at = returnDateTime();
                statement.setDouble(1,total);
                statement.setString(2,created_at);
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
                statement.setInt(1, order_id);
                statement.setString(2,"PENDING");
                statement.setInt(3,order_id);
                statement.executeUpdate();
            }
            if(method.equals(INSERT_INTO_ORDER_ITEMS)){
                statement = connection.prepareStatement(sqlStatements.getInsertIntoOrderItems());
                int order_id = Integer.valueOf(params[0]);
                int product_id = Integer.valueOf(params[1]);
                String quantity = params[2];
                boolean product_bundle = Boolean.valueOf(params[3]);
                statement.setInt(1, order_id);
                statement.setInt(2,product_id);
                statement.setString(3,quantity);
                statement.setBoolean(4,product_bundle);
                statement.executeUpdate();
            }
            if(method.equals(INSERT_ADDONS_INTO_ORDER_ITEMS)){
                statement = connection.prepareStatement(sqlStatements.getInsertAddOnsIntoOrderItems());
                int order_id = Integer.valueOf(params[0]);
                String product_name = params[1];
                boolean product_bundle = Boolean.valueOf(params[2]);
                statement.setInt(1, order_id);
                statement.setString(2,product_name);
                statement.setBoolean(3,product_bundle);
                statement.executeUpdate();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try {
            super.onPostExecute(o);
                if(o!=null){
                    asyncResponse.onFinish(o);
                }
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
