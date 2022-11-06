package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaymentActivity extends AppCompatActivity {

    private Button gcashButton,cashButton;
    private TextView subTotal, totalSum;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        subTotal = findViewById(R.id.subTotal);
        totalSum = findViewById(R.id.totalSum);
        builder = new AlertDialog.Builder(this);

        Task checkPendingCount = new Task(Task.CHECK_PENDING_COUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                int count = (int) output;
                if(count>0){
                    gcashButton.setEnabled(false);
                    cashButton.setEnabled(false);
                    builder.setMessage("You cannot pay while we prepare your orders.")
                            .setCancelable(true).setNegativeButton("close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.show();
                }
            }
        });
        checkPendingCount.execute();

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




        Task paymentTask = new Task(Task.RETRIEVE_TOTAL_AMOUNT, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                String total_amount = (String) output;
                Log.d("TAG", "onFinish:####################################### "+ total_amount);
                subTotal.setText(total_amount);
                totalSum.setText(total_amount);
            }
        });
        paymentTask.execute();

        gcashButton = findViewById(R.id.gcashButton);
        cashButton = findViewById(R.id.cashButton);

        gcashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Cashier will be here to validate order.", Toast.LENGTH_LONG).show();
                Task gCashPayment = new Task(Task.INSERT_GCASH_PAYMENT);
                gCashPayment.execute(totalSum.getText().toString());
            }
        });

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Cashier will be here to receive payment.", Toast.LENGTH_LONG).show();
                Task gCashPayment = new Task(Task.INSERT_CASH_PAYMENT);
                gCashPayment.execute(totalSum.getText().toString());
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
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Payment:
                        return true;
                    case R.id.Settings:
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
}