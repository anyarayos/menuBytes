package com.example.menubytes_customerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Orders_History_Fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView completedOrdersListView;
    private OrderListAdapter orderListAdapter;
    private TextView notifyOrderExistence;

    public Orders_History_Fragment() {
    }

    public static Orders_History_Fragment newInstance(String param1, String param2) {
        Orders_History_Fragment fragment = new Orders_History_Fragment();
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
        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output==null){
                    ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
                    notifyOrderExistence.setVisibility(View.VISIBLE);
                    completedOrdersArrayList.clear();
                    orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                }
                if(output!=null){
                    ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
                    notifyOrderExistence.setVisibility(View.GONE);
                    completedOrdersArrayList = (ArrayList<OrderListClass>) output;
                    orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);

                    }

            }
        });
        task.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_history,null);

        notifyOrderExistence = view.findViewById(R.id.notifyOrders3);

                //Initialize the listview
        completedOrdersListView = view.findViewById(R.id.orderListViewHistory);



//        final Handler refreshHandler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                // do updates
//                Toast.makeText(getActivity(), "completed orders refreshed", Toast.LENGTH_SHORT).show();
//                Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
//                    @Override
//                    public void onFinish(Object output) {
//                        if(output==null){
//                            notifyOrderExistence.setVisibility(View.VISIBLE);
//                            completedOrdersArrayList.clear();
//                            orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart, completedOrdersArrayList);
//                            completedOrdersListView.setAdapter(orderListAdapter);
//                        }
//                        if(output!=null){
//                            notifyOrderExistence.setVisibility(View.GONE);
//                            completedOrdersArrayList = (ArrayList<OrderListClass>) output;
//                            orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart, completedOrdersArrayList);
//                            completedOrdersListView.setAdapter(orderListAdapter);
//                        }
//                    }
//                });
//                task.execute();
//                refreshHandler.postDelayed(this, 3 * 1000);
//            }
//        };
//        refreshHandler.postDelayed(runnable, 3 * 1000);

        Button backToCart = view.findViewById(R.id.BacktoCartFromHistory);
        backToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartActivity.class);
                startActivity(intent);
            }
        });

        Button goToPayment = view.findViewById(R.id.proceedToPaymentBtn);
        goToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PaymentActivity.class);
                startActivity(intent);
            }
        });


        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.historyOrderFragmentRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                //INSERT CODES HERE
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }


}