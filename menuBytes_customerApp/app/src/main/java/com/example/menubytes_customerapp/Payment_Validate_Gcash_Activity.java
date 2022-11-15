package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Payment_Validate_Gcash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validate_gcash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        Toast.makeText(this, "PAYMENT ID:"+Utils.getInstance().getPayment_id(), Toast.LENGTH_SHORT).show();
        /*
        Handler handler = new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(Payment_Validate_Gcash_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(r, 5000);
        */

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refreshLayoutGcash);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                Task validatePaymentComplete = new Task(Task.VALIDATE_PAYMENT_COMPLETE, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output!=null){
                            /*Show E-Receipt*/
                            Toast.makeText(Payment_Validate_Gcash_Activity.this, "Your payment was successful.", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(Payment_Validate_Gcash_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }else{

                        }
                    }
                });validatePaymentComplete.execute(Utils.getInstance().getPayment_id());
                Task validatePaymentRejected = new Task(Task.VALIDATE_PAYMENT_REJECTED, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output!=null){
                            Toast.makeText(Payment_Validate_Gcash_Activity.this, "Your payment was rejected, please try again.", Toast.LENGTH_SHORT).show();
                            Utils.getInstance().setPayment_id(null);
                            finish();
                            Intent intent = new Intent(Payment_Validate_Gcash_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                        }
                    }
                });validatePaymentRejected.execute(Utils.getInstance().getPayment_id());

                pullToRefresh.setRefreshing(false);
            }
        });

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

