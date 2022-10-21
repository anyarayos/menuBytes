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
import java.util.ArrayList;

public class Task extends AsyncTask<String, String, Object> {
    private AsyncResponse asyncResponse;
    private String method;
    private Connection connection;
    private PreparedStatement statement;
    private SqlStatements sqlStatements = new SqlStatements();
    private ResultSet resultSet;
    private Context context;

    private ArrayList<ProductListClass> productListClassArrayList = new ArrayList<>();

    public final static String RETRIEVE_PRODUCTS_BY_CATEGORY="retrieveProductsByCategory";

    public Task(String method, AsyncResponse asyncResponse) {
        this.method = method;
        this.asyncResponse = asyncResponse;
    }

    public Task(Context context, String method, AsyncResponse asyncResponse) {
        this.context = context;
        this.method = method;
        this.asyncResponse = asyncResponse;
    }

    public Task(String method) {
        this.method = method;
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
                    Log.d("resultSet:", "NO DATA FOUND");
                } else {
                    Log.d("resultSet:", "DATA FOUND");
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

            if (method.equals("retrieveProductsByID")) {
                statement = connection.prepareStatement(sqlStatements.getRetrieveProductsByID());
                int id =Integer.valueOf(params[0]);
                statement.setInt(1,id);
                resultSet = statement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    Log.d("resultSet:", "NO DATA FOUND");
                } else {
                    Log.d("resultSet:", "DATA FOUND");
                    while (resultSet.next()) {
                        productListClassArrayList.add(new ProductListClass(resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5)
                        ));
                    }
                    Log.d("resultSet:", "DATA FOUND" + productListClassArrayList.get(0).getImageShawarma());
                    return productListClassArrayList;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        asyncResponse.onFinish(o);
    }
}
