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

        return convertView;
    }

}
