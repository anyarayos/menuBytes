package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ElectronicReceiptActivity extends AppCompatActivity {

    private TextView subTotalTV, totalSumTV, beforeTaxTV, taxVatTV;
    private double beforeTax, tax;
    private TextView AmountText, changeText;


    private ListView completedOrdersListView;
    private ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
    private OrderListAdapter orderListAdapter;

    private int count = 0;

    Button btn_go_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_receipt);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        AmountText = findViewById(R.id.AmountText);
        changeText = findViewById(R.id.changeText);

        //Initialize the listview
        completedOrdersListView = findViewById(R.id.orderListViewReceipt);



        subTotalTV = findViewById(R.id.subTotal);
        totalSumTV = findViewById(R.id.totalSum);
        beforeTaxTV = findViewById(R.id.beforeTax);
        taxVatTV = findViewById(R.id.taxVat);

        update();

        Dialog needOrDialog;
        needOrDialog = new Dialog(this);
        needOrDialog.setContentView(R.layout.need_or_dialog);
        needOrDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        needOrDialog.setCancelable(false);
        needOrDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button finishedTransactionOnDialog = needOrDialog.findViewById(R.id.btn_go_back1);
        Button finishTransaction = findViewById(R.id.btn_go_back2);
        Button requestOR = findViewById(R.id.btnAskOr);

        finishTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElectronicReceiptActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        requestOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task askForOr = new Task(Task.ASK_FOR_OR);
                askForOr.execute();
                needOrDialog.show();
            }
        });

        finishedTransactionOnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needOrDialog.dismiss();
                Intent intent = new Intent(ElectronicReceiptActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void update() {

        //Populate the arraylist
        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if (output == null) {
                    //Toast.makeText(ElectronicReceiptActivity.this, "NO QUERIED DATA", Toast.LENGTH_SHORT).show();
                    completedOrdersArrayList.clear();
                    orderListAdapter = new OrderListAdapter(ElectronicReceiptActivity.this, R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                }
                if (output != null) {
                    //Toast.makeText(ElectronicReceiptActivity.this, "QUERY SUCCESS", Toast.LENGTH_SHORT).show();
                    completedOrdersArrayList = (ArrayList<OrderListClass>) output;
                    orderListAdapter = new OrderListAdapter(ElectronicReceiptActivity.this, R.layout.list_cart, completedOrdersArrayList);
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
                Log.d("TAG", "onFinish:####################################### " + total_amount);


                subTotalTV.setText(total_amount);
                totalSumTV.setText(total_amount);

                beforeTax = Double.parseDouble(subTotalTV.getText().toString());
                beforeTax = beforeTax / 1.12;
                tax = beforeTax * 0.12;
                //beforeTaxString = Double.toString(beforeTax);
                //taxString = Double.toString(tax);
                beforeTaxTV.setText(new DecimalFormat("##.##").format(beforeTax));
                taxVatTV.setText(new DecimalFormat("##.##").format(tax));
            }
        });
        paymentTask.execute();

        Task getAmountChange = new Task(Task.GET_AMOUNT_CHANGE, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                ArrayList<Payment> payments = new ArrayList<>();
                if(output!=null){
                    payments = (ArrayList<Payment>) output;
                    AmountText.setText(payments.get(0).getPayment_amount());
                    changeText.setText(payments.get(0).getPayment_change());
                }
            }
        });getAmountChange.execute(Utils.getInstance().getPayment_id());

        Task updatePaymentTableName = new Task(Task.UPDATE_PAYMENT_TABLE_NAME);
        updatePaymentTableName.execute(Utils.getInstance().getPayment_id());

        Task updateOrdersTableName = new Task(Task.UPDATE_ORDERS_TABLE_NAME);
        updateOrdersTableName.execute(Utils.getInstance().getUser_id());

    }
}