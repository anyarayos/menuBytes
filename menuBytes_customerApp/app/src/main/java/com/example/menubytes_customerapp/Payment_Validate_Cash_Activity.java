package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;

public class Payment_Validate_Cash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validate_cash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        /*
        Handler handler = new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(Payment_Validate_Cash_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(r, 5000);

        */
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refreshLayoutCash);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                //INSERT CODES HERE
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

