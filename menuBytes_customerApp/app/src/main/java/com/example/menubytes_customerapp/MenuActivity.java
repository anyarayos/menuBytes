package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MenuActivity extends AppCompatActivity {
    Intent intent;

    int fragIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Menu);
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
                intent = new Intent(MenuActivity.this,CartActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        ChipNavigationBar chipNavigationBar = findViewById(R.id.foodMenuBar);
        chipNavigationBar.setItemSelected(R.id.Shawarma,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, new MenuShawarmaFragment()).commit();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.Shawarma:
                        fragment = new MenuShawarmaFragment();
                        break;
                    case R.id.Bowl:
                        fragment = new MenuBowlFragment();
                        break;
                    case R.id.Wings:
                        fragment = new MenuWingsFragment();
                        break;
                    case R.id.Beverages:
                        fragment = new MenuBeveragesFragment();
                        break;
                    case R.id.Addons:
                        fragment = new MenuAddonsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, fragment).commit();
            }
        });
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}