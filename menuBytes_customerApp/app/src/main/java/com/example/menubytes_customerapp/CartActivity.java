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
    Dialog editDialog, placeOrderDialog;

    int updateFlavorCount=0,updateFlavorLimit=1;
    Button updateAddQty, updateMinusQty, updateMeal, cancelChanges;
    String tempQty;
    double basePrice, priceTotal = 0 ,addOnsTotal = 0, tempDoubleQty, mealFormula;
    TextView updateQtyText, updateMealTotalText, updateItemName, updateItemDescription, updateFlavorLimitText;
    CheckBox updateCheckBoxParmesan, updateCheckBoxBuffalo, updateCheckBoxSoy, updateCheckBoxSalted, updateCheckBoxBulgogi, updateCheckBoxHoney;


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
        cartView = findViewById(R.id.orderListViewHistory);
        subTotal = findViewById(R.id.subTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        notifyOrders = findViewById(R.id.notifyOrders);

        if (orders.size()==0) {
            btnPlaceOrder.setEnabled(false);
        } else {
            btnPlaceOrder.setEnabled(true);
        }
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
                cartView = findViewById(R.id.orderListViewHistory);
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
                        placeOrderDialog.show();
                        refreshActivity();
                        overridePendingTransition(0,0);
                    }
                });
                placeOrderTask.execute(subTotal.getText().toString(),Utils.getInstance().getUser_id());
            }
        });

        View layoutCartView = findViewById(R.id.CartElementLayout);
        View editOrderView = findViewById(R.id.EditOrderLayout);

        Button goToPending = placeOrderDialog.findViewById(R.id.btn_go_to_pending);
        Button goToMenu = placeOrderDialog.findViewById(R.id.btn_go_to_menu);
        
        goToPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Orders_Pending_Fragment();
                layoutCartView.setVisibility(cartView.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.EditOrderLayout, fragment).commit();
            }
        });
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            }
        });




        //DIALOG NEEDED CODES
        editDialog = new Dialog(this);
        //editDialog.setContentView(R.layout.dialog_edit_cart_prod);
        editDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        //editDialog.setCancelable(false);
        editDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        editDialog.setContentView(R.layout.dialog_edit_cart_prod);
        updateMeal = editDialog.findViewById(R.id.btn_update_item_edit_prod);
        cancelChanges = editDialog.findViewById(R.id.btn_cancel_changes_edit_prod);
        updateAddQty = editDialog.findViewById(R.id.addQtyButton_edit_prod);
        updateMinusQty = editDialog.findViewById(R.id.minusQtyButton_edit_prod);
        updateQtyText = editDialog.findViewById(R.id.qtyText_edit_prod);
        updateMealTotalText = editDialog.findViewById(R.id.mealTotalText_edit_prod);
        updateItemName = editDialog.findViewById(R.id.txtItemTitle_edit_prod);
        updateItemDescription = editDialog.findViewById(R.id.txtItemDescription_edit_prod);



        
        cartView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int prodID = orders.get(position).getProductID();
                //Toast.makeText(CartActivity.this, Integer.toString(prodID), Toast.LENGTH_SHORT).show();
                if ((prodID<=4 && prodID >=1) || prodID==11 || prodID==12 || prodID==16 || prodID==17) {
                    //
                }
                else if (prodID<=7 && prodID >=5) {
                    editDialog.setContentView(R.layout.dialog_edit_cart_wingsprod);
                    updateMeal = editDialog.findViewById(R.id.btn_update_item_wingsprod);
                    cancelChanges = editDialog.findViewById(R.id.btn_cancel_changes_edit_wingsprod);
                    updateAddQty = editDialog.findViewById(R.id.addQtyButton_edit_wingsprod);
                    updateMinusQty = editDialog.findViewById(R.id.minusQtyButton_edit_wingsprod);
                    updateQtyText = editDialog.findViewById(R.id.qtyText_edit_wingsprod);
                    updateMealTotalText = editDialog.findViewById(R.id.mealTotalText_edit_wingsprod);
                    updateItemName = editDialog.findViewById(R.id.txtItemTitle_edit_wingsprod);
                    updateFlavorLimitText = editDialog.findViewById(R.id.flavorPcs_edit_wingsprod);
                    updateItemDescription = editDialog.findViewById(R.id.txtItemDescription_edit_wingsprod);
                    updateCheckBoxParmesan = editDialog.findViewById(R.id.cbGarlicParmesan_edit_wingsprod);
                    updateCheckBoxBuffalo = editDialog.findViewById(R.id.cbBuffalo_edit_wingsprod);
                    updateCheckBoxSoy = editDialog.findViewById(R.id.cbSoyGarlic_edit_wingsprod);
                    updateCheckBoxSalted = editDialog.findViewById(R.id.cbSaltedEgg_edit_wingsprod);
                    updateCheckBoxBulgogi = editDialog.findViewById(R.id.cbBulgogi_edit_wingsprod);
                    updateCheckBoxHoney = editDialog.findViewById(R.id.cbSesameHoneyGlazed_edit_wingsprod);
                    updateItemDescription.setText("Available in six different flavors: Garlic Parmesan, Salted Egg, Buffalo, Bulgogi, Soy Garlic and Sesame Honey Glazed");
                    String flavorAllTemp;
                    flavorAllTemp = orders.get(position).getFlavors();
                    flavorAllTemp.replace("\n","_");
                    String [] splitFlavor = flavorAllTemp.split("_");
                    int length = splitFlavor.length;
                    for (int x = 0;x<=length-1;x++){
                        if (splitFlavor[x].equals("Garlic Parmesan")) {
                            updateCheckBoxParmesan.setChecked(true);
                        }
                        else if (splitFlavor[x].equals("Buffalo")) {
                            updateCheckBoxBuffalo.setChecked(true);
                        }
                        else if (splitFlavor[x].equals("Soy Garlic")) {
                            updateCheckBoxSoy.setChecked(true);
                        }
                        else if (splitFlavor[x].equals("Salted Egg")) {
                            updateCheckBoxSalted.setChecked(true);
                        }
                        else if (splitFlavor[x].equals("Bulgogi")) {
                            updateCheckBoxBulgogi.setChecked(true);
                        }
                        else if (splitFlavor[x].equals("Sesame Honey Glazed")) {
                            updateCheckBoxHoney.setChecked(true);
                        }
                    }

                    if (prodID==5){
                        updateFlavorLimit=1;
                        updateFlavorCount=1;
                        basePrice=90;
                    }
                    else if (prodID==6) {
                        updateFlavorLimit=2;
                        updateFlavorCount=2;
                        basePrice=150;
                    }
                    else if (prodID==7) {
                        updateFlavorLimit=3;
                        updateFlavorCount=3;
                        basePrice=270;
                    }
                    updateFlavorLimitText.setText(Integer.toString(updateFlavorLimit));

                    updateCheckBoxParmesan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxParmesan.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });

                    updateCheckBoxBuffalo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxBuffalo.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });

                    updateCheckBoxSoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxSoy.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });

                    updateCheckBoxSalted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxSalted.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });

                    updateCheckBoxBulgogi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxBulgogi.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });

                    updateCheckBoxHoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && updateFlavorCount>=updateFlavorLimit) {
                                updateCheckBoxHoney.setChecked(false);
                            }
                            else {
                                if (isChecked) {
                                    updateFlavorCount++;
                                }
                                else {
                                    updateFlavorCount--;
                                }
                            }

                            if (updateFlavorLimit == updateFlavorCount) {
                                updateMeal.setEnabled(true);
                            } else if (updateFlavorLimit > updateFlavorCount) {
                                updateMeal.setEnabled(false);
                            }
                        }
                    });
                }
                else if (prodID==8 || prodID==9 || prodID==13 || prodID==14 || prodID==19) {
                    editDialog.setContentView(R.layout.dialog_edit_cart_bevadd);
                    updateMeal = editDialog.findViewById(R.id.btn_update_item_edit_bevadd);
                    cancelChanges = editDialog.findViewById(R.id.btn_cancel_changes_edit_bevadd);
                    updateAddQty = editDialog.findViewById(R.id.addQtyButton_edit_bevadd);
                    updateMinusQty = editDialog.findViewById(R.id.minusQtyButton_edit_bevadd);
                    updateQtyText = editDialog.findViewById(R.id.qtyText_edit_bevadd);
                    updateMealTotalText = editDialog.findViewById(R.id.mealTotalText_edit_bevadd);
                    updateItemName = editDialog.findViewById(R.id.txtItemTitle_edit_bevadd);
                    updateItemDescription = editDialog.findViewById(R.id.txtItemDescription_edit_bevadd);
                    if (prodID == 8){
                        updateItemDescription.setText("Lemon water mixed with blueberry syrup for extraordinary taste.");
                        basePrice = 62;
                    }
                    else if (prodID == 9) {
                        updateItemDescription.setText("Sauce made with cheese or processed cheese as a primary ingredient.");
                        basePrice = 10;
                    }
                    else if (prodID == 13) {
                        updateItemDescription.setText("Java Rice is fried rice seasoned with turmeric and annatto powder is a great use for day-old rice.");
                        basePrice = 15;
                    }
                    else if (prodID == 14) {
                        updateItemDescription.setText("Garlic sauce is a sauce prepared using garlic as a primary ingredient. It is typically a pungent sauce, with the depth of garlic flavor determined by the amount of garlic used.");
                        basePrice = 10;
                    }
                    else if (prodID == 19) {
                        updateItemDescription.setText("A traditional Korean fermented vegetable made from Chinese cabbage, radish, green onion, red pepper powder, garlic, ginger and fermented seafood.");
                        basePrice = 10;
                    }
                }




                priceTotal = basePrice * Double.parseDouble(orders.get(position).getOrderQty());
                updateMealTotalText.setText(Double.toString(priceTotal) +"0");
                updateItemName.setText(orders.get(position).getOrderName());
                updateQtyText.setText(orders.get(position).getOrderQty());

                int x = Integer.parseInt(updateQtyText.getText().toString());
                if (x == 1) {
                    updateMinusQty.setEnabled(false);
                }

                editDialog.show();

                updateMinusQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempQty =  updateQtyText.getText().toString();
                        tempDoubleQty = Double.parseDouble(tempQty)-1;
                        tempQty = Integer.toString((int) tempDoubleQty);
                        updateQtyText.setText(tempQty);
                        if (tempDoubleQty==1){
                            updateMinusQty.setEnabled(false);
                        }
                        double mealFormula = (basePrice+addOnsTotal)*tempDoubleQty;
                        updateMealTotalText.setText(Double.toString(mealFormula)+"0");
                    }
                });

                updateAddQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempQty =  updateQtyText.getText().toString();
                        tempDoubleQty = Double.parseDouble(tempQty)+1;
                        tempQty = Integer.toString((int) tempDoubleQty);
                        updateQtyText.setText(tempQty);
                        if (tempDoubleQty==1){
                            updateMinusQty.setEnabled(false);
                        }
                        else if (tempDoubleQty>0){
                            updateMinusQty.setEnabled(true);
                        }
                        mealFormula = (basePrice+addOnsTotal)*tempDoubleQty;
                        updateMealTotalText.setText(Double.toString(mealFormula)+"0");
                    }
                });





                updateMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (prodID<=7 && prodID >=5) {
                            String updateFinalFlavor="";
                            if (updateCheckBoxParmesan.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxParmesan.getText().toString()+"_";
                            }
                            if (updateCheckBoxSalted.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxSalted.getText().toString()+"_";
                            }
                            if (updateCheckBoxBuffalo.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxBuffalo.getText().toString()+"_";
                            }
                            if (updateCheckBoxBulgogi.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxBulgogi.getText().toString()+"_";
                            }
                            if (updateCheckBoxSoy.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxSoy.getText().toString()+"_";
                            }
                            if (updateCheckBoxHoney.isChecked()) {
                                updateFinalFlavor = updateFinalFlavor+updateCheckBoxHoney.getText().toString()+"_";
                            }
                            orders.get(position).setFlavors(updateFinalFlavor);
                        }
                        orders.get(position).setOrderQty(updateQtyText.getText().toString());
                        orders.get(position).setOrderSubPrice(updateMealTotalText.getText().toString());
                        double orderTemp = Double.parseDouble(orders.get(position).getOrderQty()) / Double.parseDouble(orders.get(position).getOrderSubPrice());
                        orders.get(position).setOrderPrice(Double.toString(orderTemp)+"0");
                        cartView.setAdapter(orderListAdapter);
                        double total_price=0;
                        for(int x=0 ; x <orders.size(); x++){
                            double price = Double.parseDouble(orders.get(x).getOrderSubPrice());
                            total_price += price;
                        }
                        subTotal.setText(String.valueOf(total_price)+"0");
                        editDialog.dismiss();
                    }
                });

                cancelChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
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
                Toast.makeText(CartActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
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
                    btnPlaceOrder.setEnabled(false);
                } else {
                    btnPlaceOrder.setEnabled(true);
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


    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
}