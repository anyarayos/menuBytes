package com.example.menubytes_customerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<ProductListClass> {
    private Context mContext;
    private int mResource;
    public ProductListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ProductListClass> objects) {
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
        try {
            imageView.setImageDrawable(getDrawableFromAssets(getItem(position).getImageShawarma()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView txtName = convertView.findViewById(R.id.txtNameShawarma);
        txtName.setText(getItem(position).getNameShawarma());
        TextView txtPrice = convertView.findViewById(R.id.txtPriceShawarma);
        txtPrice.setText(getItem(position).getPriceShawarma());
        TextView txtDes = convertView.findViewById(R.id.txtDesShawarma);
        txtDes.setText(getItem(position).getDesShawarma());
        return convertView;
    }

    public Drawable getDrawableFromAssets(String fileName) throws IOException {
        try {
            // get input stream
            InputStream ims = getContext().getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            return d;
        } catch (Exception ex) {
            Log.d("Drawable.createFromStream", "Error: " + ex.toString());
            return null;
        }
    }
}
