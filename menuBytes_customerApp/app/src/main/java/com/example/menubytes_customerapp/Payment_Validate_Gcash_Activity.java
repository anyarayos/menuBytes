package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Payment_Validate_Gcash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validate_gcash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        /*
        Handler handler = new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                finish();
                //Intent intent = new Intent(Payment_Validate_Gcash_Activity.this, MainActivity.class);
                //startActivity(intent);
            }
        };
        handler.postDelayed(r, 5000);*/
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

