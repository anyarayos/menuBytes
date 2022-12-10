package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private SwipeMenuListView cartView;

    private TextView subTotal;
    private Button btnPlaceOrder,proceedPaymentButton;
    private TextView notifyOrders;
    private int ORDER_ID;
    private OrderListAdapter orderListAdapter;
    private LoadingDialog loadingDialog;;
    Dialog editDialog, placeOrderDialog, clearCartDialog;
    Button clearAllbtn;
    int samp;



    private  boolean productBundle_edit_checked = false;


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
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Payment:
                        startActivity(new Intent(getApplicationContext(),PaymentActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Account:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;

            }
        });


        loadingDialog = new LoadingDialog(this);
        cartView = findViewById(R.id.orderListViewReceipt);
        subTotal = findViewById(R.id.subTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        notifyOrders = findViewById(R.id.notifyOrders);

        samp = notifyOrders.getVisibility();

        placeOrderDialog = new Dialog(this);
        placeOrderDialog.setContentView(R.layout.placed_order_dialog);
        placeOrderDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        placeOrderDialog.setCancelable(false);
        placeOrderDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        if(!(Utils.getInstance().getOrders().isEmpty())){
            notifyOrders.setVisibility(View.GONE);
            this.orders = Utils.getInstance().getOrders();
            if(orders.isEmpty()){
//            Toast.makeText(this, "orders.isEmpty()", Toast.LENGTH_SHORT).show();
            }else{
//            Toast.makeText(this, "orders has values.", Toast.LENGTH_SHORT).show();
                cartView = findViewById(R.id.orderListViewReceipt);
                orderListAdapter = new OrderListAdapter(this,R.layout.list_cart, orders);
                cartView.setAdapter(orderListAdapter);

                double total_price=0;
                for(int index=0 ; index <orders.size(); index++){
                    double price = Double.parseDouble(orders.get(index).getOrderSubPrice());
                    total_price += price;
                }
                subTotal.setText(String.valueOf(total_price)+"0");
            }
        }


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samp = notifyOrders.getVisibility();
                if (samp!=0) {
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
                                    //Toast.makeText(CartActivity.this, "Your order is placed.", Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismissDialog();
                                }
                                Task placeOrderItems = new Task(Task.INSERT_INTO_ORDER_ITEMS);
                                placeOrderItems.execute(String.valueOf(order_id),
                                        String.valueOf(orders.get(index).getProductID()),
                                        orders.get(index).getOrderQty(),
                                        String.valueOf(orders.get(index).isOrderBundle()),
                                        String.valueOf(orders.get(index).isHas_addons()),
                                        orders.get(index).getFlavors());
                                /*Check if order has Add-Ons
                                 * */
                                if(orders.get(index).isHas_addons()){
                                    Task placeOrderAddons = new Task(Task.INSERT_ADDONS_INTO_ORDER_ITEMS);
                                    placeOrderAddons.execute(
                                            String.valueOf(order_id),
                                            "Shawarma All Meat",
                                            orders.get(index).getOrderQty()
                                    );
                                }

                            }
                            Utils.getInstance().removeAll();
                            orders.clear();
                            //refreshActivity();
                            overridePendingTransition(0,0);
                        }
                    });
                    placeOrderTask.execute(subTotal.getText().toString(),Utils.getInstance().getUser_id());
                    placeOrderDialog.show();
                }
                else {
                    Toast.makeText(CartActivity.this, "ERROR! You do not have order in the list!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View layoutCartView = findViewById(R.id.CartElementLayout);
        View editOrderView = findViewById(R.id.EditOrderLayout);

        Button goToPending = placeOrderDialog.findViewById(R.id.btn_go_to_pending);
        Button goToMenu = placeOrderDialog.findViewById(R.id.btn_go_to_menu);
        
        goToPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderDialog.dismiss();
                Fragment fragment = null;
                fragment = new Orders_Pending_Fragment();
                layoutCartView.setVisibility(cartView.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.EditOrderLayout, fragment).commit();
            }
        });
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderDialog.dismiss();
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
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
                //Toast.makeText(CartActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                orders.remove(position);
                cartView.setAdapter(orderListAdapter);
                //orderListAdapter.notifyDataSetChanged();
                double total_price=0;
                for(int x=0 ; x <orders.size(); x++){
                    double price = Double.parseDouble(orders.get(x).getOrderSubPrice());
                    total_price += price;
                }
                subTotal.setText(String.valueOf(total_price)+"0");
                if (orders.size()==0) {
                    notifyOrders.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        cartView.setCloseInterpolator(new BounceInterpolator());
        cartView.setOpenInterpolator(new BounceInterpolator());

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
        clearAllbtn = findViewById(R.id.clearAllBtn);
        clearCartDialog = new Dialog(this);
        clearCartDialog.setContentView(R.layout.clear_all_dialog);
        clearCartDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        clearCartDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        clearAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samp = notifyOrders.getVisibility();
                if (samp!=0) {
                     clearCartDialog.show();
                }
                else {
                    Toast.makeText(CartActivity.this, "ERROR! You do not have order in the list!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button proceedToClear = clearCartDialog.findViewById(R.id.btn_proceed);
        Button cancelClear = clearCartDialog.findViewById(R.id.btn_cancel);
        proceedToClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.clear();
                cartView.setAdapter(orderListAdapter);
                notifyOrders.setVisibility(View.VISIBLE);
                subTotal.setText("0.00");
                clearCartDialog.dismiss();
            }
        });

        cancelClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCartDialog.dismiss();
            }
        });


    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
}