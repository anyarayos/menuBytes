package com.example.menubytes_customerapp;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

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

    private ListView pendingListView;
    private Dialog pendingDialog;
    private ArrayList <PendingListClass> pendingArrayList = new ArrayList<>();
    private ArrayList<PendingOrderSumListClass> pendingOrderSumArrayList = new ArrayList<>();
    private PendingListAdapter pendingListAdapter;

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

        //dialog
        pendingDialog = new Dialog(getActivity());

        //populate arraylist
        Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                if(output!=null){
                    pendingArrayList = (ArrayList<PendingListClass>) output;
                    pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
                    pendingListView.setAdapter(pendingListAdapter);
                }
            }
        });
        task.execute();

        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
                Toast.makeText(getActivity(), "pending orders refreshed", Toast.LENGTH_SHORT).show();
                Task task = new Task(Task.DISPLAY_PENDING_ORDERS, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output==null){
                            notifyIfEmpty.setVisibility(View.VISIBLE);
                            pendingArrayList.clear();
                            pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
                            pendingListView.setAdapter(pendingListAdapter);
                        }
                        if(output!=null){
                            notifyIfEmpty.setVisibility(View.GONE);
                            pendingArrayList = (ArrayList<PendingListClass>) output;
                            pendingListAdapter = new PendingListAdapter(getActivity(),R.layout.list_pending,pendingArrayList);
                            pendingListView.setAdapter(pendingListAdapter);
                        }
                    }
                });
                task.execute();
                refreshHandler.postDelayed(this, 3 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 3 * 1000);



        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //change image when queue or kitchen
                //queue in drawable = dialogorder
                //kitchen in drawable dialogkitchen
                //ImageView img= (ImageView) view.findViewById(R.id.queueOrKitchen);
                //img.setImageResource(R.drawable.my_image);
                switch (position){
                    case 0:
                        //NAGCRA-CRASH PAG INA-ADD YUNG ORDER SUMMARY SA ORDER ID.
                        //BAKA HINDI IDEAL YUNG DIALOG LANG SINCE WALA SYANG SARILING CLASS. WATCHU THINK NAT?

                        //pendingListView = view.findViewById(R.id.pendingOrderSumListView);
                        //pendingOrderSumArrayList.add(new PendingOrderSumListClass("Shawarma Wrap Large","10.00","1","ADDONS HERE"));
                        //pendingOrderSumArrayList.add(new PendingOrderSumListClass("Shawarma Wrap Small","50.00","1","ADDONS HERE"));
                        //pendingOrderSumArrayList.add(new PendingOrderSumListClass("Shawarma Bowl Large","15.00","1","ADDONS HERE"));
                        //pendingOrderSumArrayList.add(new PendingOrderSumListClass("Shawarma Bowl Small","120.00","1","ADDONS HERE"));
                        //PendingOrderSumListAdapter pendingOrderSumListAdapter = new PendingOrderSumListAdapter(getActivity(),R.layout.list_pending_order_sum,pendingOrderSumArrayList);
                        //pendingListView.setAdapter(pendingOrderSumListAdapter);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

                pendingDialog.show();
            }
        });


        pendingDialog.setContentView(R.layout.pending_dialog);
        pendingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialog_background));
        //pendingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pendingDialog.setCancelable(false);
        pendingDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button backtoPending = pendingDialog.findViewById(R.id.btn_okay);
        backtoPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingDialog.dismiss();
            }
        });



        return view;
    }
}