package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

public class ElectronicReceiptActivity extends AppCompatActivity {

    private TextView subTotalTV, totalSumTV, beforeTaxTV, taxVatTV;
    private double beforeTax, tax;

    private ListView completedOrdersListView;
    private ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
    private OrderListAdapter orderListAdapter;

    private int count = 0;

    private TextView notifyItemsPayment;
    Button btn_go_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_receipt);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        btn_go_back2 = findViewById(R.id.btn_go_back2);
        btn_go_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElectronicReceiptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        notifyItemsPayment = findViewById(R.id.notifyItemsPayment);

        subTotalTV = findViewById(R.id.subTotal);
        totalSumTV = findViewById(R.id.totalSum);
        beforeTaxTV = findViewById(R.id.beforeTax);
        taxVatTV = findViewById(R.id.taxVat);
        completedOrdersListView = findViewById(R.id.orderListViewHistory);

        update();

    }

    private void update() {

        //Populate the arraylist
        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if (output == null) {
                    Toast.makeText(ElectronicReceiptActivity.this, "NULL", Toast.LENGTH_SHORT).show();
                    notifyItemsPayment.setVisibility(View.VISIBLE);
                    completedOrdersArrayList.clear();
                    orderListAdapter = new OrderListAdapter(ElectronicReceiptActivity.this, R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                }
                if (output != null) {
                    Toast.makeText(ElectronicReceiptActivity.this, "NOT NULL", Toast.LENGTH_SHORT).show();
                    notifyItemsPayment.setVisibility(View.GONE);
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

        Task updatePaymentTableName = new Task(Task.UPDATE_PAYMENT_TABLE_NAME);
        updatePaymentTableName.execute(Utils.getInstance().getPayment_id());

        Task updateOrdersTableName = new Task(Task.UPDATE_ORDERS_TABLE_NAME);
        updateOrdersTableName.execute(Utils.getInstance().getUser_id());
    }
}