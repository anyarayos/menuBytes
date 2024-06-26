package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

public class PaymentActivity extends AppCompatActivity {


    String labelDiscount;
    TextView DiscountLabelTV;

    private boolean hasGCash=true;
    private String beforeTaxString, taxString;
    private Button gcashButton,cashButton, requestDiscountButton;
    private TextView subTotalTV, totalSumTV, beforeTaxTV, taxVatTV, discountTV;
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
        discountTV = findViewById(R.id.discountTotal);
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
                double refNoTemp = Double.parseDouble(RefNoEditText.getText().toString());
                if (refNoTemp >0) {

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
                    //subtotal,amount_due,remarks,discount_id,discount_type,discount_amount
                    String subtotal = subTotalTV.getText().toString();
                    String amount_due = totalSumTV.getText().toString();
                    String remarks = RefNoString;
                    String discount_id = "0";
                    String discount_type = labelDiscount;
                    String discount_amount = discountTV.getText().toString();

                    gCashPayment.execute(subtotal,amount_due,remarks,discount_id,discount_type,discount_amount);
                    gcashDialog.dismiss();
                } else {
                    Toast.makeText(PaymentActivity.this, "The reference number you enter is invalid.", Toast.LENGTH_SHORT).show();
                }
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
                double paymentTemp = Double.parseDouble(cashAmountEditText.getText().toString());
                double amountTemp = Double.parseDouble(totalSumTV.getText().toString());
                if (paymentTemp>=amountTemp) {
                    Task CashPayment = new Task(Task.INSERT_CASH_PAYMENT, new AsyncResponse() {
                        @Override
                        public void onFinish(Object output) {
                            if(output!=null){
                                Utils.getInstance().setPayment_id((String) output);
                                startActivity(new Intent(PaymentActivity.this,Payment_Validate_Cash_Activity.class));
                            }
                            else{
                                Toast.makeText(PaymentActivity.this, "Payment unsuccessful, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //subtotal,amount_due,payment_amount,discount_id,discount_type,discount_amount
                    String subtotal = subTotalTV.getText().toString();
                    String amount_due = totalSumTV.getText().toString();
                    String payment_amount = cashAmountString;
                    String discount_id = "0";
                    String discount_type = labelDiscount;
                    String discount_amount = discountTV.getText().toString();
                    CashPayment.execute(subtotal,amount_due,payment_amount,discount_id,discount_type,discount_amount);
                    cashDialog.dismiss();
                } else {
                    Toast.makeText(PaymentActivity.this, "The amount you enter is invalid.", Toast.LENGTH_SHORT).show();
                }
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
        DiscountLabelTV = findViewById(R.id.textViewDiscountLabel);
        labelDiscount="None";

        Dialog discountRequestDialog;
        discountRequestDialog = new Dialog(this);
        discountRequestDialog.setContentView(R.layout.dialog_disc);
        discountRequestDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        discountRequestDialog.setCancelable(false);
        discountRequestDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnRequestDialog = discountRequestDialog.findViewById(R.id.btn_submit_disc);

        requestDiscountButton = findViewById(R.id.requestDiscButton);
        requestDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountRequestDialog.show();
            }
        });

        RadioGroup radioGroupDiscountLabel = discountRequestDialog.findViewById(R.id.groupDiscLabel);
        radioGroupDiscountLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGovernment:
                        labelDiscount = "government";
                        break;
                    case R.id.rbPwd:
                        labelDiscount = "pwd";
                        break;
                    case R.id.rbSenior:
                        labelDiscount = "senior";
                        break;
                }
                Toast.makeText(getApplicationContext(), labelDiscount, Toast.LENGTH_SHORT).show();
            }
        });

        btnRequestDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double subTotalDouble = Double.parseDouble(subTotalTV.getText().toString());
                discountRequestDialog.dismiss();
                DiscountLabelTV.setText(labelDiscount);

                //arith exp
                double discTemp = 0.20 * subTotalDouble;
                String temp = Double.toString(discTemp);
                discountTV.setText(temp+"0");
                double grandTotal = subTotalDouble - discTemp;
                temp = Double.toString(grandTotal);
                totalSumTV.setText(temp+"0");
                //INSERT DISCOUNT CODES HERE BELOW SELECT CONDITION ON WHAT labelDiscount IS
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
//                        update();
//                    }
//                });
//            }
//        }, 0, 2); // updates each 5 secs
    }

    private void update(){
//        Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();

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

                if (total_amount.equals("")) {
                    total_amount = "0.00";
                }
                subTotalTV.setText(total_amount);

                beforeTax = Double.parseDouble(subTotalTV.getText().toString());
                beforeTax = beforeTax/1.12;
                tax = beforeTax*0.12;
                if (DiscountLabelTV.getText().toString().equals("None")) {
                    discountTV.setText("0.00");
                } else {
                    double discount =  0.20 * beforeTax;
                    discountTV.setText(Double.toString(discount));
                    double grandTotal = Double.parseDouble(total_amount) - discount;
                    total_amount = Double.toString(grandTotal)+"0";
                }
                totalSumTV.setText(total_amount);
                beforeTaxTV.setText(new DecimalFormat("##.##").format(beforeTax));
                taxVatTV.setText(new DecimalFormat("##.##").format(tax));
            }
        });
        paymentTask.execute();
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
