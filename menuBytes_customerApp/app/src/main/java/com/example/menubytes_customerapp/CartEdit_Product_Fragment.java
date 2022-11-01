package com.example.menubytes_customerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class CartEdit_Product_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartEdit_Product_Fragment() {
    }

    public static CartEdit_Product_Fragment newInstance(String param1, String param2) {
        CartEdit_Product_Fragment fragment = new CartEdit_Product_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_cart_edit_product,null);
        Button backButton = view.findViewById(R.id.backToCartBtn);
        View layoutCartView = view.findViewById(R.id.CartElementLayout);
        View editOrderView = view.findViewById(R.id.EditOrderLayout);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: BACKBUTTON ON EDIT 1
//FIX THIS EHEHE. HIDE MO TONG FRAGMENT WHILE SETTTING THE VISIBLITIY NG LAYOUT NI CART. HEHEHHEHEHE THANKSS NAT LABYU! <3
//                editOrderView.setVisibility(editOrderView.INVISIBLE);
//                layoutCartView.setVisibility(layoutCartView.VISIBLE);
            }
        });


        return view;
    }
}