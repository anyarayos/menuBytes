package com.example.menubytes_customerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
                                                    if(output!=null){
                                                        Utils.getInstance().setTable_name((String)output);
                                                        Intent i = new Intent(IntroActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        finish();
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