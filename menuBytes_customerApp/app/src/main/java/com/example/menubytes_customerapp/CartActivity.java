package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView cartView;
    private TextView subTotal;
    private Button btnPlaceOrder;

    public ArrayList<OrderListClass> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderListClass> orders) {
        this.orders = orders;
    }

    public ArrayList<OrderListClass> orders = new ArrayList<>();


    public boolean addToOrders (OrderListClass order) {
        return orders.add(order);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Menu:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Cart:
                        return true;
                    case R.id.Payment:
                        startActivity(new Intent(getApplicationContext(),PaymentActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Settings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;

            }
        });
        cartView = findViewById(R.id.orderListView);
        subTotal = findViewById(R.id.subTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        if(!(Utils.getInstance().getOrders().isEmpty())){
            this.orders = Utils.getInstance().getOrders();
        }else{}

        if(orders.isEmpty()){
//            Toast.makeText(this, "orders.isEmpty()", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "orders has values.", Toast.LENGTH_SHORT).show();
            cartView = findViewById(R.id.orderListView);
            OrderListAdapter orderListAdapter = new OrderListAdapter(this,R.layout.list_cart, orders);
            cartView.setAdapter(orderListAdapter);

            double total_price=0;
            for(int index=0 ; index <orders.size(); index++){
                double price = Double.parseDouble(orders.get(index).getOrderPrice());
                total_price += price;
            }
            subTotal.setText(String.valueOf(total_price));
        }

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task placeOrderTask = new Task(Task.INSERT_INTO_ORDERS, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        int order_id = (int) output;
                        if(order_id!=0){
                            Toast.makeText(CartActivity.this, "Your order is placed.", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("order_id_debug", "onFinish: " + String.valueOf(order_id));
                        Task placeOrderStatus = new Task(Task.INSERT_INTO_ORDER_STATUS);
                        placeOrderStatus.execute(String.valueOf(order_id));


                            for(int index = 0; index < orders.size(); index++){
                                Task placeOrderItems = new Task(Task.INSERT_INTO_ORDER_ITEMS);
                                placeOrderItems.execute(String.valueOf(order_id),String.valueOf(orders.get(index).getOrderID()),
                                        orders.get(index).getOrderQty());
                            }

                    }
                });
                placeOrderTask.execute(subTotal.getText().toString());


            }
        });

    }
}