package com.example.menubytes_customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PendingListAdapter extends ArrayAdapter<PendingListClass> {

    private Context mContext;
    private int mResource;

    public PendingListAdapter (@NonNull Context context, int resource, @NonNull ArrayList<PendingListClass> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        TextView txtViewStatus_ = convertView.findViewById(R.id.txtViewStatus);
        txtViewStatus_.setText(getItem(position).getOrderStatus());
        TextView txtViewOrderNum_ = convertView.findViewById(R.id.txtViewOrderNum);
        txtViewOrderNum_.setText(getItem(position).getOrderNum());
        TextView txtViewQty_ = convertView.findViewById(R.id.txtViewQty);
        txtViewQty_.setText(getItem(position).getOrderTy());
        TextView txtViewOrderPrice_ = convertView.findViewById(R.id.txtViewOrderPrice);
        txtViewOrderPrice_.setText(getItem(position).getOrderTotalPrize());
        return convertView;
    }

}
