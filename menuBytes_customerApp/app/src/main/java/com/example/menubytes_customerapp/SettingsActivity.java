package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mysql.jdbc.Util;

public class SettingsActivity extends AppCompatActivity {
    Dialog loginDialog;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Settings);
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
                        startActivity(new Intent(getApplicationContext(),PaymentActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Settings:
                        return true;
                }
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,CartActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        loginDialog = new Dialog(this);
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        loginDialog.setCancelable(false);
        loginDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        Button OutLoginBtn = findViewById(R.id.outLoginBtn);
        OutLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.show();
            }
        });
        EditText textfieldUser = loginDialog.findViewById(R.id.textfieldUser);
        Button inDialogLogin = loginDialog.findViewById(R.id.loginBtn);
        Button inDialogCancel = loginDialog.findViewById(R.id.cancelBtn);


        inDialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_field = textfieldUser.getText().toString();

                if(user_field.equals("table_1")){
                    Utils.getInstance().setUser_id("3");
                }
                if(user_field.equals("table_2")){
                    Utils.getInstance().setUser_id("4");
                }
                if(user_field.equals("table_3")){
                    Utils.getInstance().setUser_id("5");
                }

                Toast.makeText(SettingsActivity.this, "Successfully Login!"+ Utils.getInstance().getUser_id(), Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });

        inDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });
    }
}