package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },3000);



        /*
        READ THIS!!!!!
        
        SWR = Shawarma Wrap Regular
        SWL = Shawarma Wrap Large
        SSBR = Shawarma Salad Bowl Regular
        SSBL = Shawarma Salad Bowl Large
        SRBR = Shawarma Rice Bowl Regular
        SRBL = Shawarma Rice Bowl Large
        SNL = Shawarma Nachos Large
         */
    }
}