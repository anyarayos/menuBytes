package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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