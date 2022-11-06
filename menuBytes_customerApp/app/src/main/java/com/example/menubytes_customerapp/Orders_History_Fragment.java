package com.example.menubytes_customerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private ArrayList<OrderListClass> completedOrdersArrayList = new ArrayList<>();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_history,null);

        notifyOrderExistence = view.findViewById(R.id.notifyOrders3);

                //Initialize the listview
        completedOrdersListView = view.findViewById(R.id.orderListView);

        //Populate the arraylist
        Task task = new Task(Task.DISPLAY_COMPLETED_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output!=null){
                    completedOrdersArrayList = (ArrayList<OrderListClass>) output;
                    if(!completedOrdersArrayList.isEmpty()){notifyOrderExistence.setVisibility(View.GONE);}
                    orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart, completedOrdersArrayList);
                    completedOrdersListView.setAdapter(orderListAdapter);
                }
            }
        });
        task.execute();





        //Instantiate the Adapter, select a layout
        orderListAdapter = new OrderListAdapter(getActivity(),R.layout.list_cart,	completedOrdersArrayList);

        //Make your created listview set an adapter
        completedOrdersListView.setAdapter(orderListAdapter);
        return view;
    }
}