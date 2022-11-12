package com.example.menubytes_customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PendingOrderSumListAdapter extends ArrayAdapter<PendingOrderSumListClass> {

    private Context mContext;
    private int mResource;

    public PendingOrderSumListAdapter (@NonNull Context context, int resource, @NonNull ArrayList<PendingOrderSumListClass> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        TextView txtPendOrderName_ = convertView.findViewById(R.id.txtPendOrderName);
        txtPendOrderName_.setText(getItem(position).getPendingOrderSumName());
        TextView txtPendOrderQty_ = convertView.findViewById(R.id.txtPendOrderQty);
        txtPendOrderQty_.setText(getItem(position).getPendingOrderSumQty());
        TextView txtPendOrderPrice_ = convertView.findViewById(R.id.txtPendOrderPrice);
        txtPendOrderPrice_.setText(getItem(position).getPendingOrderSumPrice());




        TextView txtOrderAddOns = convertView.findViewById(R.id.txtOrderAddOns);
        String add_on = "";
        if(getItem(position).isHas_addons()){
            add_on = "Shawarma All Meat";
            double tempAddOnsPrice = Double.parseDouble(txtPendOrderPrice_.getText().toString())+10;
            String newPriceString = Double.toString(tempAddOnsPrice)+"0";
            txtPendOrderPrice_.setText(newPriceString);
        }
        txtOrderAddOns.setText(add_on);

        TextView txtPendOrderSubPrice_ = convertView.findViewById(R.id.txtPendOrderSubPrice);
        double subPriceDouble = Double.parseDouble(txtPendOrderPrice_.getText().toString()) * Double.parseDouble(txtPendOrderQty_.getText().toString());
        //double qtyDouble = Double.parseDouble(txtPendOrderQty_.getText().toString());
        //double priceDouble = subPriceDouble / qtyDouble;
        String orderSubPriceString = Double.toString(subPriceDouble)+"0";


        txtPendOrderSubPrice_.setText(orderSubPriceString);


        /*
        if(getItem(position).isHas_addons()){
            txtOrderAddOns.setText("Shawarma All Meat");
            if(getItem(position).getPendingOrderSumPrice()!=null){
                double qty = Double.parseDouble(getItem(position).getPendingOrderSumQty());
                double price = Double.parseDouble(getItem(position).getPendingOrderSumPrice());
                price += 10;
                double total_price = qty * price;
                txtPendOrderSubPrice.setText(String.valueOf(total_price)+"0");
                txtPendOrderPrice_.setText(String.valueOf(price)+"0");
            }

        }else{
            txtOrderAddOns.setVisibility(View.GONE);
            double qty = Double.parseDouble(getItem(position).getPendingOrderSumQty());
            double price = Double.parseDouble(getItem(position).getPendingOrderSumPrice());
            double total_price = qty * price;
            txtPendOrderSubPrice.setText(String.valueOf(total_price));
            txtPendOrderPrice_.setText(getItem(position).getPendingOrderSumPrice());
        }
        */
        return convertView;
    }

}
