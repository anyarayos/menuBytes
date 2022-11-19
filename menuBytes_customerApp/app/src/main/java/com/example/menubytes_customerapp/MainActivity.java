package com.example.menubytes_customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private ListView pendingListView;
    private ArrayList<PendingListClass> pendingArrayList = new ArrayList<>();
    private PendingListAdapter pendingListAdapter;
    private TextView notifyOrders3;
    private Timer autoUpdate;
    private TextView txtGreeting;
    Dialog notifyDialog;
    Button assitanceBtn;
    @Override
    public void onResume() {
        super.onResume();
//        autoUpdate = new Timer();
//        autoUpdate.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
////                        update();
//                    }
//                });
//            }
//        }, 0, 5000); // updates each 5 secs
    }

    private void update(){
        // your logic here
        Toast.makeText(context, "refreshed", Toast.LENGTH_SHORT).show();
        Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output==null){
                    notifyOrders3.setVisibility(View.VISIBLE);
                    pendingArrayList.clear();
                    pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
                if(output!=null){
                    notifyOrders3.setVisibility(View.GONE);
                    pendingArrayList = (ArrayList<PendingListClass>) output;
                    pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
            }
        });
        task.execute();
    }

    @Override
    public void onPause() {
//        autoUpdate.cancel();
        super.onPause();
    }


    Context context = MainActivity.this;

    private int[] mImages = new int[]{
        R.drawable.carousel0,R.drawable.carousel1,R.drawable.carousel2,R.drawable.carousel3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Home);

        Task updateLogInTime = new Task(Task.UPDATE_LOGIN_TIME);
        updateLogInTime.execute(Utils.getInstance().getUser_id());

        notifyOrders3 = findViewById(R.id.notifyOrders3);
        txtGreeting = findViewById(R.id.txtGreeting);
        pendingListView = findViewById(R.id.pendingOrderListView);
        if(Utils.getInstance().getTable_name()!=null){
            txtGreeting.setText("HI "+Utils.getInstance().getTable_name() + "!");
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        return true;
                    case R.id.Menu:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.fab:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
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
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        CarouselView carouselView = findViewById(R.id.carousel);
        carouselView.setPageCount(4);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                switch (position) {
                    case 0:
                        imageView.setImageResource(R.drawable.carousel0);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.carousel1);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.carousel2);
                        break;
                    default:
                        imageView.setImageResource(R.drawable.carousel3);
                        break;
                }
            }
        });

        TextView goToMenu = findViewById(R.id.notifyOrders3);
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                overridePendingTransition(0,0);
                bottomNavigationView.setSelectedItemId(R.id.Menu);
            }
        });

        Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output==null){
                    notifyOrders3.setVisibility(View.VISIBLE);
                    pendingArrayList.clear();
                    pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
                if(output!=null){
                    notifyOrders3.setVisibility(View.GONE);
                    pendingArrayList = (ArrayList<PendingListClass>) output;
                    pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
            }
        });
        task.execute();


        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.mainActivityRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output==null){
                            notifyOrders3.setVisibility(View.VISIBLE);
                            pendingArrayList.clear();
                            pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                            pendingListView.setAdapter(pendingListAdapter);
                        }
                        if(output!=null){
                            notifyOrders3.setVisibility(View.GONE);
                            pendingArrayList = (ArrayList<PendingListClass>) output;
                            pendingListAdapter = new PendingListAdapter(MainActivity.this,R.layout.list_pending,pendingArrayList);
                            pendingListView.setAdapter(pendingListAdapter);
                        }
                    }
                });
                task.execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        notifyDialog = new Dialog(this);
        notifyDialog.setContentView(R.layout.need_assistance_dialog);
        notifyDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.dialog_background));
        notifyDialog.setCancelable(false);
        notifyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        assitanceBtn = findViewById(R.id.assitanceButton);
        assitanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task askAssistance = new Task(Task.ASK_ASSISTANCE);
                askAssistance.execute();
                notifyDialog.show();
            }
        });
        Button backToHome = notifyDialog.findViewById(R.id.btn_go_back1);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDialog.dismiss();
            }
        });
    }
}