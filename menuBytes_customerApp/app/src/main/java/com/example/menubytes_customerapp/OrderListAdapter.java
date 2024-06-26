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
        String nameTemp = getItem(position).getOrderName();
        nameTemp = nameTemp.replaceAll("_"," ");
        orderName.setText(nameTemp);

        TextView orderQty = convertView.findViewById(R.id.txtOrderQty);
        orderQty.setText(getItem(position).getOrderQty());

        TextView orderSubPrice = convertView.findViewById(R.id.txtOrderSubPrice);
        orderSubPrice.setText(getItem(position).getOrderSubPrice());

        double subPriceDouble = Double.parseDouble(orderSubPrice.getText().toString());
        double qtyDouble = Double.parseDouble(orderQty.getText().toString());
        double priceDouble = subPriceDouble / qtyDouble;
        String orderPriceString = Double.toString(priceDouble)+"0";

        TextView orderPrice = convertView.findViewById(R.id.txtOrderPrice);
        orderPrice.setText(orderPriceString);

        TextView orderAddOns = convertView.findViewById(R.id.txtOrderAddOns);

        //SHOW DESCRIPTION
        String add_on = "";
        if(getItem(position).isHas_addons()){
            add_on = "Shawarma All Meat";
            orderAddOns.setText(add_on);
        }else{
            if(getItem(position).getFlavors()!=null){
                String FlavorAll = getItem(position).getFlavors();
                FlavorAll = FlavorAll.substring(0,FlavorAll.length()-1);
                FlavorAll = FlavorAll.replaceAll("_","\n");
                orderAddOns.setText(FlavorAll);
            }else{
                orderAddOns.setText("");
            }

        }



        /*
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
        }*/



        return convertView;
    }
}
