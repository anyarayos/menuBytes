package com.example.menubytes_customerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CartEdit_BevAddProduct_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartEdit_BevAddProduct_Fragment() {
    }

    public static CartEdit_BevAddProduct_Fragment newInstance(String param1, String param2) {
        CartEdit_BevAddProduct_Fragment fragment = new CartEdit_BevAddProduct_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_cart_edit_bevaddprod,null);
        Button backButton = view.findViewById(R.id.backToCartBtn);
        View cartView = view.findViewById(R.id.CartElementLayout);
        View editOrderView = view.findViewById(R.id.EditOrderLayout);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: BACKBUTTON ON EDIT 3
                //FIX THIS EHEHE. HIDE MO TONG FRAGMENT WHILE SETTTING THE VISIBLITIY NG LAYOUT NI CART. HEHEHHEHEHE THANKSS NAT LABYU! <3
//                editOrderView.setVisibility(editOrderView.INVISIBLE);
//                cartView.setVisibility(cartView.VISIBLE);
//                inflater.inflate(R.layout.fragment_cart_edit_product,null);
            }
        });


        return view;
    }
}