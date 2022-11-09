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
        TextView orderSubPrice = convertView.findViewById(R.id.txtOrderSubPrice);

        TextView orderPrice = convertView.findViewById(R.id.txtOrderPrice);
        TextView orderQty = convertView.findViewById(R.id.txtOrderQty);
        orderQty.setText(getItem(position).getOrderQty());
        orderPrice.setText(getItem(position).getOrderPrice());

        TextView orderAddOns = convertView.findViewById(R.id.txtOrderAddOns);

        if(getItem(position).isHas_addons()){
            String add_on = "Shawarma All Meat";
            if(getItem(position).getOrderPrice()!=null){
                double all_meat_price = 10;
                double price = Double.parseDouble(getItem(position).getOrderPrice());
                price += all_meat_price;
                orderPrice.setText(String.valueOf(price));

                double qty = Double.parseDouble(getItem(position).getOrderQty());
                double grand_total = qty * price;
                orderSubPrice.setText(String.valueOf(grand_total));
            }
            orderAddOns.setText(add_on);
        }else{
            orderAddOns.setVisibility(View.GONE);
            orderSubPrice.setText(getItem(position).getOrderSubPrice());
        }



        return convertView;
    }
}
