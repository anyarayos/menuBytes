package com.example.menubytes_customerapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Orders_Pending_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView pendingListView,pendingOrderSumLV;
    private ArrayList <PendingListClass> pendingArrayList = new ArrayList<>();
    private ArrayList<PendingOrderSumListClass> pendingOrderSumArrayList = new ArrayList<>();
    private PendingListAdapter pendingListAdapter;
    private PendingOrderSumListAdapter pendingOrderSumListAdapter;

    private TextView notifyIfEmpty;

    public Orders_Pending_Fragment() {
    }

    public static Orders_Pending_Fragment newInstance(String param1, String param2) {
        Orders_Pending_Fragment fragment = new Orders_Pending_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_pending,null);
        notifyIfEmpty = view.findViewById(R.id.notifyIfEmpty);
        //initialize the listview
        pendingListView = view.findViewById(R.id.pendingOrderListView);
        pendingOrderSumLV = view.findViewById(R.id.pendingOrderSumListView);


        //populate arraylist
        Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output!=null){
                    notifyIfEmpty.setVisibility(View.GONE);
                    pendingArrayList = (ArrayList<PendingListClass>) output;
                    pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
            }
        });
        task.execute();


        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                Task pendingTask = new Task(Task.RETRIEVE_ORDER_BREAKDOWN, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output==null){
                            pendingOrderSumArrayList.clear();
                            pendingOrderSumListAdapter = new PendingOrderSumListAdapter(getActivity(),R.layout.list_pending_order_sum,
                            pendingOrderSumArrayList);
                            pendingOrderSumLV.setAdapter(pendingOrderSumListAdapter);
                        }
                        if(output!=null){
                            pendingOrderSumArrayList = (ArrayList<PendingOrderSumListClass>)output;
                            pendingOrderSumListAdapter = new PendingOrderSumListAdapter(getActivity(),R.layout.list_pending_order_sum,
                            pendingOrderSumArrayList);
                            pendingOrderSumLV.setAdapter(pendingOrderSumListAdapter);
                        }
                    }
                });pendingTask.execute(pendingArrayList.get(position).getOrderNum());

            }
        });


//        final Handler refreshHandler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                // do updates
//                Toast.makeText(getActivity(), "pending orders refreshed", Toast.LENGTH_SHORT).show();
//                Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
//                    @Override
//                    public void onFinish(Object output) {
//                        if(output==null){
//                            notifyIfEmpty.setVisibility(View.VISIBLE);
//                            pendingArrayList.clear();
//                            pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
//                            pendingListView.setAdapter(pendingListAdapter);
//                        }
//                        if(output!=null){
//                            notifyIfEmpty.setVisibility(View.GONE);
//                            pendingArrayList = (ArrayList<PendingListClass>) output;
//                            pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
//                            pendingListView.setAdapter(pendingListAdapter);
//                        }
//                    }
//                });
//                task.execute();
//                refreshHandler.postDelayed(this, 3 * 1000);
//            }
//        };
//        refreshHandler.postDelayed(runnable, 3 * 1000);

        Button backToCart = view.findViewById(R.id.BacktoCartBtn2);
        backToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartActivity.class);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pendingOrderFragmentRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Pull to refresh code here
                pullToRefresh.setRefreshing(true);
            }
        });



        return view;
    }
}