package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    private EditText textfieldUser;
    private EditText textFieldPass;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        textFieldPass = findViewById(R.id.textFieldPass);
        textfieldUser = findViewById(R.id.textfieldUser);
        loginBtn = findViewById(R.id.loginBtn);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if(pref.contains("STATUS")){
            String login_status = pref.getString("STATUS",null);
            if(login_status.equals("1")){
                Utils.getInstance().setTable_name(pref.getString("TABLE_NAME",null));
                Utils.getInstance().setUser_id(pref.getString("USER_ID",null));
                Task set_to_logged_in = new Task(Task.SET_STATUS_LOGGEDIN);
                set_to_logged_in.execute(Utils.getInstance().getUser_id());
                Intent i = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }else{

        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = textfieldUser.getText().toString();
                String password = textFieldPass.getText().toString();
                if (textfieldUser.getText().toString().equals("") || textFieldPass.getText().toString().equals("")) {
                    Toast.makeText(IntroActivity.this, "Do not leave any fields blank", Toast.LENGTH_SHORT).show();
                }else{
                    Task checkUserNameExistence = new Task(Task.CHECK_USER_NAME_EXISTENCE, new AsyncResponse() {
                        @Override
                        public void onFinish(Object output) {
                            if(output!=null){
                                Task checkUserNamePassword = new Task(Task.CHECK_USER_NAME_PASSWORD, new AsyncResponse() {
                                    @Override
                                    public void onFinish(Object output) {
                                        if(output!=null){
                                            Utils.getInstance().setUser_id((String)output);
                                            Task setTableName = new Task(Task.SET_TABLE_NAME, new AsyncResponse() {
                                                @Override
                                                public void onFinish(Object output) {
                                                    String table_name = (String)output;
                                                    if(output!=null){
                                                        //TODO: update status
                                                        Task checkUserStatus = new Task(Task.CHECK_LOGIN_STATUS, new AsyncResponse() {
                                                            @Override
                                                            public void onFinish(Object output) {
                                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                                                SharedPreferences.Editor editor = pref.edit();
                                                                Log.d("TAG", "onFinish: " + String.valueOf(output));

                                                                    if(String.valueOf(output).equals("1")){
                                                                        Toast.makeText(IntroActivity.this, "User logged in from another device.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        Utils.getInstance().setTable_name(table_name);
                                                                        Task set_to_logged_in = new Task(Task.SET_STATUS_LOGGEDIN);
                                                                        set_to_logged_in.execute(Utils.getInstance().getUser_id());
                                                                        editor.putString("STATUS", "1");
                                                                        editor.putString("USER_ID",Utils.getInstance().getUser_id());
                                                                        editor.putString("TABLE_NAME",table_name);
                                                                        editor.commit();
                                                                        Intent i = new Intent(IntroActivity.this, MainActivity.class);
                                                                        startActivity(i);
                                                                        finish();
                                                                    }


                                                            }
                                                        });checkUserStatus.execute(Utils.getInstance().getUser_id());


                                                    }
                                                }
                                            });setTableName.execute((String)output);
//                                            Intent i = new Intent(IntroActivity.this, MainActivity.class);
//                                            startActivity(i);
//                                            finish();
                                        } else{
                                            Toast.makeText(IntroActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });checkUserNamePassword.execute(user_name,password);
                            }else{
                                Toast.makeText(IntroActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });checkUserNameExistence.execute(user_name);
                }

            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(IntroActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
//            }
//        },3000);




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