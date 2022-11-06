package com.example.menubytes_customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<OrderListClass> {
    private Context mContext;
    private int mResource;
    private String AddOnData=null;

    public OrderListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderListClass> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        TextView orderName = convertView.findViewById(R.id.txtOrderName);
        orderName.setText(getItem(position).getOrderName());
        TextView orderPrice = convertView.findViewById(R.id.txtOrderPrice);
        orderPrice.setText(getItem(position).getOrderPrice());
        TextView orderQty = convertView.findViewById(R.id.txtOrderQty);
        orderQty.setText(getItem(position).getOrderQty());
        TextView orderAddOns = convertView.findViewById(R.id.txtOrderAddOns);
        AddOnData = getItem(position).getOrderAddOns_1() +" "+ getItem(position).getOrderAddOns_2() +" "+
                    getItem(position).getOrderAddOns_3() +" "+ getItem(position).getOrderAddOns_4();
        orderAddOns.setText(AddOnData);

        return convertView;
    }
}
