package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SettingsActivity extends AppCompatActivity {
    Dialog loginDialog, logoutDialog;
    BottomNavigationView bottomNavigationView;
    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        userName = findViewById(R.id.userName);
        if(Utils.getInstance().getTable_name()!=null){
            userName.setText(Utils.getInstance().getTable_name());
        }
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Account);
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
        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.log_out_dialog);
        logoutDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        logoutDialog.setCancelable(false);
        logoutDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        Button ChangeLoginBtn = findViewById(R.id.changeAccountBtn);
        ChangeLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.show();
            }
        });
        EditText textfieldUser = loginDialog.findViewById(R.id.textfieldUser);
        EditText textFieldPass = loginDialog.findViewById(R.id.textFieldPass);
        Button inDialogLogin = loginDialog.findViewById(R.id.loginBtn);
        Button inDialogCancel = loginDialog.findViewById(R.id.cancelBtn);


        inDialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = textfieldUser.getText().toString();
                String password = textFieldPass.getText().toString();

                if(user_name.equals("")||password.equals("")){
                    Toast.makeText(SettingsActivity.this, "Do not leave any fields blank.", Toast.LENGTH_SHORT).show();
                }else{
                    Task checkUserNameExistence = new Task(Task.CHECK_USER_NAME_EXISTENCE, new AsyncResponse() {
                        @Override
                        public void onFinish(Object output) {
                            if(output!=null){
                                Task checkUserNamePassword = new Task(Task.CHECK_USER_NAME_PASSWORD, new AsyncResponse() {
                                    @Override
                                    public void onFinish(Object output) {
                                        if(output!=null){



                                            /*Logout the previous user first*/
                                            Task set_to_logged_out = new Task(Task.SET_STATUS_LOGGEDOUT);
                                            set_to_logged_out.execute(Utils.getInstance().getUser_id());

                                            Utils.getInstance().setUser_id((String)output);

                                            /*Update LogIn Status of current user*/
                                            Task set_to_logged_in = new Task(Task.SET_STATUS_LOGGEDIN);
                                            set_to_logged_in.execute(Utils.getInstance().getUser_id());
                                            
                                            Task setTableName = new Task(Task.SET_TABLE_NAME, new AsyncResponse() {
                                                @Override
                                                public void onFinish(Object output) {
                                                    if(output!=null){
                                                        Task checkUserStatus = new Task(Task.CHECK_LOGIN_STATUS, new AsyncResponse() {
                                                            @Override
                                                            public void onFinish(Object output) {
                                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                                                SharedPreferences.Editor editor = pref.edit();
                                                                Log.d("TAG", "onFinish: " + String.valueOf(output));

                                                                if(String.valueOf(output).equals("1")){
                                                                    Toast.makeText(SettingsActivity.this, "User logged in from another device.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    Utils.getInstance().setTable_name((String)output);
                                                                    editor.putString("USER_ID",Utils.getInstance().getUser_id());
                                                                    editor.putString("TABLE_NAME",(String)output);
                                                                    editor.commit();
                                                                    loginDialog.dismiss();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }


                                                            }
                                                        });checkUserStatus.execute(Utils.getInstance().getUser_id());

//                                                        Utils.getInstance().setTable_name((String)output);
//                                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//                                                        SharedPreferences.Editor editor = pref.edit();
//                                                        editor.putString("USER_ID",Utils.getInstance().getUser_id());
//                                                        editor.putString("TABLE_NAME",(String)output);
//                                                        editor.commit();
//                                                        loginDialog.dismiss();
//                                                        finish();
//                                                        startActivity(getIntent());
                                                    }
                                                }
                                            });setTableName.execute((String)output);
//
                                        } else{
                                            Toast.makeText(SettingsActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });checkUserNamePassword.execute(user_name,password);
                            }else{
                                Toast.makeText(SettingsActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });checkUserNameExistence.execute(user_name);
                }

            }
        });

        inDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });


        Button logoutBtn = findViewById(R.id.logoutBtn);
        TextView dialogUserName = logoutDialog.findViewById(R.id.logOutName);
        EditText textFieldPassLogout = logoutDialog.findViewById(R.id.textFieldPass);

        Button logoutProceed = logoutDialog.findViewById(R.id.logoutButton);
        Button logoutCancel = logoutDialog.findViewById(R.id.cancelButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUserName.setText(userName.getText().toString());
                logoutDialog.show();

            }
        });

        logoutProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = textFieldPassLogout.getText().toString();
                Task checkPassword = new Task(Task.CHECK_PASSWORD, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output!=null){
                            //TODO: update status
                            Task updateLogOutTime = new Task(Task.UPDATE_LOGOUT_TIME);
                            updateLogOutTime.execute(Utils.getInstance().getUser_id());

                            Task set_to_logged_out = new Task(Task.SET_STATUS_LOGGEDOUT);
                            set_to_logged_out.execute(Utils.getInstance().getUser_id());
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("STATUS", "0");editor.commit();
                            startActivity(new Intent(getApplicationContext(),IntroActivity.class));
                        }else{
                            Toast.makeText(SettingsActivity.this, "Incorrect Password!" + Utils.getInstance().getUser_id(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });checkPassword.execute(Utils.getInstance().getUser_id(),password);

            }
        });

        logoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });
    }
}