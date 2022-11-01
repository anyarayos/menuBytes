package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private SwipeMenuListView cartView;
    private TextView subTotal;
    private Button btnPlaceOrder;
    private TextView notifyOrders;
    private int ORDER_ID;
    OrderListAdapter orderListAdapter;
    private LoadingDialog loadingDialog;;

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

        loadingDialog = new LoadingDialog(this);

        cartView = findViewById(R.id.orderListView);
        subTotal = findViewById(R.id.subTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        notifyOrders = findViewById(R.id.notifyOrders);

        if(!(Utils.getInstance().getOrders().isEmpty())){
            notifyOrders.setVisibility(View.GONE);
            this.orders = Utils.getInstance().getOrders();
            if(orders.isEmpty()){
//            Toast.makeText(this, "orders.isEmpty()", Toast.LENGTH_SHORT).show();
            }else{
//            Toast.makeText(this, "orders has values.", Toast.LENGTH_SHORT).show();
                cartView = findViewById(R.id.orderListView);
                orderListAdapter = new OrderListAdapter(this,R.layout.list_cart, orders);
                cartView.setAdapter(orderListAdapter);

                double total_price=0;
                for(int index=0 ; index <orders.size(); index++){
                    double price = Double.parseDouble(orders.get(index).getOrderPrice());
                    total_price += price;
                }
                subTotal.setText(String.valueOf(total_price));
            }
        }

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Task placeOrderTask = new Task(Task.INSERT_INTO_ORDERS, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        int order_id = (int) output;
                        if(order_id!=0){
                            Utils.getInstance().addToOrderIds(order_id);
                        }
                        Log.d("order_id_debug", "onFinish: " + String.valueOf(order_id));
                        Task placeOrderStatus = new Task(Task.INSERT_INTO_ORDER_STATUS);
                        placeOrderStatus.execute(String.valueOf(order_id),Utils.getInstance().getUser_id());
                            for(int index = 0; index < orders.size(); index++){
                                if(index == orders.size()-1){
                                    Toast.makeText(CartActivity.this, "Your order is placed.", Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismissDialog();
                                }
                                Task placeOrderItems = new Task(Task.INSERT_INTO_ORDER_ITEMS);
                                placeOrderItems.execute(String.valueOf(order_id),
                                        String.valueOf(orders.get(index).getProductID()),
                                        orders.get(index).getOrderQty(),
                                        String.valueOf(orders.get(index).isOrderBundle()));
                            /*Check if order has Add-Ons
                            * */
                                if(orders.get(index).getOrderAddOns_1()!=""){
                                    Task placeOrderAddons = new Task(Task.INSERT_ADDONS_INTO_ORDER_ITEMS);
                                    placeOrderAddons.execute(
                                            String.valueOf(order_id),
                                            orders.get(index).getOrderAddOns_1(),
                                            orders.get(index).getOrderQty()
                                            );
                                }
                                if(orders.get(index).getOrderAddOns_2()!=""){
                                    Task placeOrderAddons = new Task(Task.INSERT_ADDONS_INTO_ORDER_ITEMS);
                                    placeOrderAddons.execute(String.valueOf(order_id),
                                            orders.get(index).getOrderAddOns_2(),
                                            orders.get(index).getOrderQty()
                                            );
                                }
                                if(orders.get(index).getOrderAddOns_3()!=""){
                                    Task placeOrderAddons = new Task(Task.INSERT_ADDONS_INTO_ORDER_ITEMS);
                                    placeOrderAddons.execute(String.valueOf(order_id),
                                            orders.get(index).getOrderAddOns_3(),
                                            orders.get(index).getOrderQty()
                                            );
                                }
                                if(orders.get(index).getOrderAddOns_4()!=""){
                                    Task placeOrderAddons = new Task(Task.INSERT_ADDONS_INTO_ORDER_ITEMS);
                                    placeOrderAddons.execute(String.valueOf(order_id),
                                            orders.get(index).getOrderAddOns_4(),
                                            orders.get(index).getOrderQty()
                                            );
                                }
                            }
                        Utils.getInstance().removeAll();
                        orders.clear();
                        refreshActivity();


                    }
                });
                placeOrderTask.execute(subTotal.getText().toString(),Utils.getInstance().getUser_id());

            }
        });

        View layoutCartView = findViewById(R.id.CartElementLayout);
        View editOrderView = findViewById(R.id.EditOrderLayout);

        cartView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CartActivity.this, "Clicked on:"+orders.get(position).getOrderName(), Toast.LENGTH_SHORT).show();
                //Open Specific Product Fragment to Edit
                String order_category = orders.get(position).getOrderCategory();
                Fragment fragment = null;
                switch(order_category){
                    case "shawarma":
                        fragment = new CartEdit_Product_Fragment();
                        layoutCartView.setVisibility(cartView.INVISIBLE);
                        break;
                    case "bowl":
                        fragment = new CartEdit_Product_Fragment();
                        layoutCartView.setVisibility(cartView.INVISIBLE);
                        break;
                    case "wings":
                        /*Open Wings Fragment*/
                        break;
                    case "beverage":
                        fragment = new CartEdit_BevAddProduct_Fragment();
                        layoutCartView.setVisibility(cartView.INVISIBLE);
                        break;
                    case "additional":
                        fragment = new CartEdit_BevAddProduct_Fragment();
                        layoutCartView.setVisibility(cartView.INVISIBLE);
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.EditOrderLayout, fragment).commit();
            }
        });


        //creating delete siwpe
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0x1D, 0x25)));
                deleteItem.setWidth(250);
                deleteItem.setIcon(R.drawable.deletelogo);
                menu.addMenuItem(deleteItem);
            }
        };

        cartView.setMenuCreator(creator);
        cartView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Toast.makeText(CartActivity.this, "Clicked Deleted", Toast.LENGTH_SHORT).show();
                orders.remove(position);
                orderListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        //view pending codes
        TextView viewPendingText = findViewById(R.id.viewPendingOrderHere);
        viewPendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Orders_Pending_Fragment();
                layoutCartView.setVisibility(cartView.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.EditOrderLayout, fragment).commit();
            }
        });

        //view history codes
        TextView viewFinishedText = findViewById(R.id.viewFinishedOrderHere);
        viewFinishedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Orders_History_Fragment();
                layoutCartView.setVisibility(cartView.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.EditOrderLayout, fragment).commit();
            }
        });


    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
}