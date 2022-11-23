package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

public class PaymentActivity extends AppCompatActivity {

    private boolean hasGCash=true;
    private String beforeTaxString, taxString;
    private Button gcashButton,cashButton;
    private TextView subTotalTV, totalSumTV, beforeTaxTV, taxVatTV;
    private AlertDialog.Builder builder;
    private double beforeTax, tax;
    private Dialog gcashDialog,cashDialog;

    private ListView completedOrdersListView;
    private ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
    private OrderListAdapter orderListAdapter;

    private Timer autoUpdate;

    private int count = 0;

    private TextView notifyItemsPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        notifyItemsPayment = findViewById(R.id.notifyItemsPayment);

        subTotalTV = findViewById(R.id.subTotal);
        totalSumTV = findViewById(R.id.totalSum);
        beforeTaxTV = findViewById(R.id.beforeTax);
        taxVatTV = findViewById(R.id.taxVat);

        //Initialize the listview
        completedOrdersListView = findViewById(R.id.orderListViewReceipt);

        builder = new AlertDialog.Builder(this);

        gcashButton = findViewById(R.id.gcashButton);
        cashButton = findViewById(R.id.cashButton);
        gcashButton.setEnabled(false);
        cashButton.setEnabled(false);

        gcashDialog = new Dialog(this);
        gcashDialog.setContentView(R.layout.dialog_payment_gcash);
        gcashDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        gcashDialog.setCancelable(false);
        gcashDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button gcashProceedToPayment = gcashDialog.findViewById(R.id.btn_proceed_gcash);
        Button gcashBackToPaymentForm = gcashDialog.findViewById(R.id.btn_cancel_gcash);
        ImageView queueOrKitchen = gcashDialog.findViewById(R.id.queueOrKitchen);

        Dialog noGcashDialog;
        noGcashDialog = new Dialog(this);
        noGcashDialog.setContentView(R.layout.no_gcash_dialog);
        noGcashDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        noGcashDialog.setCancelable(false);
        noGcashDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button backToHome2 = noGcashDialog.findViewById(R.id.btn_confirm);
        backToHome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noGcashDialog.dismiss();
            }
        });

        Task checkGcashStatus = new Task(Task.CHECK_GCASH_AVAILABLE, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output!=null){
                    hasGCash = true;
                    queueOrKitchen.setImageBitmap(decodeBlobType((byte[])output));
                }else{
                    noGcashDialog.show();
                    hasGCash = false;
                    gcashButton.setEnabled(false);
                }
            }
        });checkGcashStatus.execute();

        TextView totalAmountToPayGcash = gcashDialog.findViewById(R.id.textView3);

        EditText RefNoEditText = gcashDialog.findViewById(R.id.refNoEditText);


        gcashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gcashDialog.show();
            }
        });
        gcashProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RefNoString = RefNoEditText.getText().toString();
                //TODO: GET PAYMENT ID
                Task gCashPayment = new Task(Task.INSERT_GCASH_PAYMENT2, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output!=null){
                            Utils.getInstance().setPayment_id((String) output);
                            startActivity(new Intent(PaymentActivity.this,Payment_Validate_Gcash_Activity.class));
                        }else{
                            Toast.makeText(PaymentActivity.this, "Payment unsuccessful, please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                gCashPayment.execute(totalSumTV.getText().toString(),RefNoString);


                //overridePendingTransition(0,0);
                gcashDialog.dismiss();
            }
        });

        gcashBackToPaymentForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefNoEditText.setText("");
                gcashDialog.dismiss();
            }
        });

        cashDialog = new Dialog(this);
        cashDialog.setContentView(R.layout.dialog_payment_cash);
        cashDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        cashDialog.setCancelable(false);
        cashDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button cashProceedToPayment = cashDialog.findViewById(R.id.btn_proceed_cash);
        Button cashBackToPaymentForm = cashDialog.findViewById(R.id.btn_cancel_cash);
        EditText cashAmountEditText = cashDialog.findViewById(R.id.cashAmountPayment);
        TextView totalAmountToPayCash = cashDialog.findViewById(R.id.textView2);

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.show();
            }
        });


        cashProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cashAmountString = cashAmountEditText.getText().toString();
                Task CashPayment = new Task(Task.INSERT_CASH_PAYMENT, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output!=null){
                            Utils.getInstance().setPayment_id((String) output);
                            startActivity(new Intent(PaymentActivity.this,Payment_Validate_Cash_Activity.class));
                        }else{
                            Toast.makeText(PaymentActivity.this, "Payment unsuccessful, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                CashPayment.execute(totalSumTV.getText().toString(), cashAmountString);

                //overridePendingTransition(0,0);
                cashDialog.dismiss();
            }
        });

        cashBackToPaymentForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.dismiss();
            }
        });

        //Check if there's a pending order
        Task checkPendingCount = new Task(Task.CHECK_PENDING_COUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                count = (int) output;
                if(count>0){
                    gcashButton.setEnabled(false);
                    cashButton.setEnabled(false);
                }else{
                    Task checkCompletedCount = new Task(Task.CHECK_COMPLETED_COUNT, new AsyncResponse() {
                        @Override
                        public void onFinish(Object output) {
                            int count = 0;
                            count = (int)output;
                            if(output!=null){
                                if(count>0){
                                    if(hasGCash) {
                                        gcashButton.setEnabled(true);
                                    }
                                    cashButton.setEnabled(true);
                                }
                            }
                        }
                    });checkCompletedCount.execute();

                }

            }
        });
        checkPendingCount.execute();

        //Check if there's a payment already
        Task checkPaymentCount = new Task(Task.CHECK_PAYMENT_COUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                int count = (int) output;
                if(count>0){
                    gcashButton.setEnabled(false);
                    cashButton.setEnabled(false);
                }
            }
        });
        checkPaymentCount.execute();

        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output==null){
                    notifyItemsPayment.setVisibility(View.VISIBLE);
                    completedOrdersArrayList.clear();
                    orderListAdapter = new OrderListAdapter(PaymentActivity.this,R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                }
                if(output!=null){
                    notifyItemsPayment.setVisibility(View.GONE);
                    completedOrdersArrayList = (ArrayList<OrderListClass>) output;
                    orderListAdapter = new OrderListAdapter(PaymentActivity.this,R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);
                }
            }
        });
        task.execute();
        //Update Total Amount
        Task paymentTask = new Task(Task.RETRIEVE_TOTAL_AMOUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                String total_amount = (String) output;
                beforeTax = Double.parseDouble(subTotalTV.getText().toString())/1.12;
                tax = beforeTax*0.12;
                beforeTaxString = Double.toString(beforeTax);
                taxString = Double.toString(tax);
                beforeTaxTV.setText(beforeTaxString);
                taxVatTV.setText(taxString);
                subTotalTV.setText(total_amount);
                totalSumTV.setText(total_amount);

                totalAmountToPayGcash.setText("Total Amount is to pay is "+totalSumTV.getText().toString());
                totalAmountToPayCash.setText("Total Amount to pay is "+totalSumTV.getText().toString());




                beforeTax = Double.parseDouble(subTotalTV.getText().toString());
                beforeTax = beforeTax/1.12;
                tax = beforeTax*0.12;
                //beforeTaxString = Double.toString(beforeTax);
                //taxString = Double.toString(tax);
                beforeTaxTV.setText(new DecimalFormat("##.##").format(beforeTax));
                taxVatTV.setText(new DecimalFormat("##.##").format(tax));

            }
        });
        paymentTask.execute();


        gcashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gcashDialog.show();
            }
        });

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.show();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.Payment);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this,CartActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });


//        Dialog noGcashDialog;
//        noGcashDialog = new Dialog(this);
//        noGcashDialog.setContentView(R.layout.no_gcash_dialog);
//        noGcashDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
//        noGcashDialog.setCancelable(false);
//        noGcashDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//
//        //INSERT THIS
////        noGcashDialog.show();
//
//        Button backToHome2 = noGcashDialog.findViewById(R.id.btn_confirm);
//        backToHome2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                noGcashDialog.dismiss();
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        autoUpdate = new Timer();
//        autoUpdate.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
////                        update();
//                    }
//                });
//            }
//        }, 0, 5000); // updates each 5 secs
    }

    private void update(){
        Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();

        //Populate the arraylist
        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output==null){
                    notifyItemsPayment.setVisibility(View.VISIBLE);
                    completedOrdersArrayList.clear();
                    orderListAdapter = new OrderListAdapter(PaymentActivity.this,R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                }
                if(output!=null){
                    notifyItemsPayment.setVisibility(View.GONE);
                    completedOrdersArrayList = (ArrayList<OrderListClass>) output;
                    orderListAdapter = new OrderListAdapter(PaymentActivity.this,R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);
                }
            }
        });
        task.execute();

        //Check if there's a pending order
        Task checkPendingCount = new Task(Task.CHECK_PENDING_COUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                count = (int) output;
                if(count>0){
//                    builder.setMessage("You cannot pay while we prepare your orders.")
//                            .setCancelable(true).setNegativeButton("close", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //  Action for 'NO' Button
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
                    gcashButton.setEnabled(false);
                    cashButton.setEnabled(false);
                }else{
                    Task checkCompletedCount = new Task(Task.CHECK_COMPLETED_COUNT, new AsyncResponse() {
                        @Override
                        public void onFinish(Object output) {
                            int count = 0;
                            count = (int)output;
                            if(output!=null){
                                if(count>0){
                                    if(hasGCash){
                                        gcashButton.setEnabled(true);
                                    }

                                    cashButton.setEnabled(true);
                                }
                            }
                        }
                    });checkCompletedCount.execute();

                }

            }
        });
        checkPendingCount.execute();

        //Check if there's a payment already
        Task checkPaymentCount = new Task(Task.CHECK_PAYMENT_COUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                int count = (int) output;
                if(count>0){
                    gcashButton.setEnabled(false);
                    cashButton.setEnabled(false);
                }
            }
        });
        checkPaymentCount.execute();

        //Update Total Amount
        Task paymentTask = new Task(Task.RETRIEVE_TOTAL_AMOUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                String total_amount = (String) output;
                Log.d("TAG", "onFinish:####################################### "+ total_amount);


                subTotalTV.setText(total_amount);
                totalSumTV.setText(total_amount);

                beforeTax = Double.parseDouble(subTotalTV.getText().toString());
                beforeTax = beforeTax/1.12;
                tax = beforeTax*0.12;
                //beforeTaxString = Double.toString(beforeTax);
                //taxString = Double.toString(tax);
                beforeTaxTV.setText(new DecimalFormat("##.##").format(beforeTax));
                taxVatTV.setText(new DecimalFormat("##.##").format(tax));
            }
        });
        paymentTask.execute();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.paymentActivityRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Pull to refresh code here
                pullToRefresh.setRefreshing(true);
            }
        });

    }

    public Bitmap decodeBlobType(byte[] bytes_from_database){

        //get bytes from database
        byte[] bytes = bytes_from_database;

        //byte to bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;

    }

    @Override
    public void onPause() {
//        autoUpdate.cancel();
        super.onPause();
    }

}
