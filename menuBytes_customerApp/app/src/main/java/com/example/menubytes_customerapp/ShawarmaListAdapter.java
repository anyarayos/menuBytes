package com.example.menubytes_customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ShawarmaListAdapter extends ArrayAdapter<ShawarmaListClass> {
    private Context mContext;
    private int mResource;
    public ShawarmaListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ShawarmaListClass> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        ImageView imageView = convertView.findViewById(R.id.imageShawarma);
        imageView.setImageResource(getItem(position).getImageShawarma());
        TextView txtName = convertView.findViewById(R.id.txtNameShawarma);
        txtName.setText(getItem(position).getNameShawarma());
        TextView txtPrice = convertView.findViewById(R.id.txtPriceShawarma);
        txtPrice.setText(getItem(position).getPriceShawarma());
        TextView txtDes = convertView.findViewById(R.id.txtDesShawarma);
        txtDes.setText(getItem(position).getDesShawarma());
        return convertView;
    }
}
