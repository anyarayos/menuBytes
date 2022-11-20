package com.example.menubytes_customerapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu_BevAddProd_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu_BevAddProd_Fragment extends Fragment {

    private ImageView imgViewItemMenu;
    private TextView txtItemTitle;
    private TextView txtItemDescription;
    private TextView qtyText;
    private TextView mealTotalText;
    private Button addQtyButton;
    private Button minusQtyButton;
    private Button addToCartButton;
    private ConstraintLayout constraintLayout;
    LoadingDialog loadingDialog;
    private double priceTotal=0,addonsTotal=0, tempIntQty=0;
    private String category;
    Dialog addToCartDialog;
    String tempQty;

    int PRODUCT_ID=-1;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Menu_BevAddProd_Fragment() {
        // Required empty public constructor
    }

    public Menu_BevAddProd_Fragment(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public Menu_BevAddProd_Fragment(int PRODUCT_ID, String CATEGORY) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.category = CATEGORY;
    }

    public static Menu_BevAddProd_Fragment newInstance(String param1, String param2) {
        Menu_BevAddProd_Fragment fragment = new Menu_BevAddProd_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int product_id = PRODUCT_ID;
        loadingDialog = new LoadingDialog(this.getActivity());
        if(product_id!=-1 && product_id!=-0){
            Task task = new Task(Task.RETRIEVE_PRODUCTS_BY_ID2, new AsyncResponse() {
                @Override
                public void onFinish(Object output) {
                    ArrayList<ProductListClass> productListClassArrayList = (ArrayList<ProductListClass>)output;
//                        imgViewItemMenu.setImageDrawable(getDrawableFromAssets(productListClassArrayList.get(0).getImage()));
                        imgViewItemMenu.setImageBitmap(decodeBlobType(productListClassArrayList.get(0).getBytes()));
                        txtItemTitle.setText(productListClassArrayList.get(0).getName());
                        txtItemDescription.setText(productListClassArrayList.get(0).getDescription());
                        mealTotalText.setText(productListClassArrayList.get(0).getPrice());
                        priceTotal = Double.parseDouble(mealTotalText.getText().toString());
                        mealTotalText.setText(Double.toString(priceTotal)+"0");

                        constraintLayout.setVisibility(View.VISIBLE);
                        loadingDialog.dismissDialog();

                }
            });

            task.execute(String.valueOf(product_id));
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public Bitmap decodeBlobType(byte[] bytes_from_database){

        byte[] bytes = bytes_from_database;

        InputStream is = new ByteArrayInputStream(bytes);

        Bitmap bitmap = BitmapFactory.decodeStream(is);

        return bitmap;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu__bevaddprod,null);
        loadingDialog.startLoadingDialog();
        constraintLayout = view.findViewById(R.id.frameLayoutwings);
        imgViewItemMenu = view.findViewById(R.id.imgViewItemMenu);
        txtItemTitle = view.findViewById(R.id.txtItemTitle);
        txtItemDescription = view.findViewById(R.id.txtItemDescription);
        qtyText = view.findViewById(R.id.qtyText);
        mealTotalText = view.findViewById(R.id.mealTotalText);
        addQtyButton = view.findViewById(R.id.addQtyButton);
        minusQtyButton = view.findViewById(R.id.minusQtyButton);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        tempQty =  qtyText.getText().toString();
        qtyText.setText(tempQty);


        addToCartDialog = new Dialog(getActivity());
        addToCartDialog.setContentView(R.layout.added_to_cart_dialog);
        addToCartDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialog_background));
        addToCartDialog.setCancelable(false);
        addToCartDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;



        minusQtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempQty =  qtyText.getText().toString();
                tempIntQty = Double.parseDouble(tempQty)-1;
                tempQty = Integer.toString((int) tempIntQty);
                qtyText.setText(tempQty);
                if (tempIntQty==1){
                    minusQtyButton.setEnabled(false);
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)* Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });
        addQtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempQty =  qtyText.getText().toString();
                int tempIntQty = Integer.parseInt(tempQty)+1;
                tempQty = Integer.toString(tempIntQty);
                qtyText.setText(tempQty);
                if (tempIntQty==1){
                    minusQtyButton.setEnabled(false);
                }
                else if (tempIntQty>0){
                    minusQtyButton.setEnabled(true);
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartDialog.show();
                //Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                String finalName = txtItemTitle.getText().toString();
                String finalTotal = mealTotalText.getText().toString();
                String finalQty = qtyText.getText().toString();
                String mealPrice = Double.toString(Double.parseDouble(mealTotalText.getText().toString())/Double.parseDouble(qtyText.getText().toString()))+"0";
                OrderListClass order = new OrderListClass(PRODUCT_ID, finalName, mealPrice, finalQty, category ,false,"", finalTotal,
                        false);
                Utils.getInstance().addToOrders(order);

            }
        });
        Button backButton3 = view.findViewById(R.id.btn_back3);
        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                switch(category){
                    case "additional":
                        fragment = new MenuAddonsFragment();
                        break;
                    case "beverage":
                        fragment = new MenuBeveragesFragment();
                        break;
                }
                fm.replace(R.id.menu_container,fragment).commit();
            }
        });



        Button addToCartDialogButton = addToCartDialog.findViewById(R.id.btn_go_back);
        addToCartDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartDialog.dismiss();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                switch(category){
                    case "additional":
                        fragment = new MenuAddonsFragment();
                        break;
                    case "beverage":
                        fragment = new MenuBeveragesFragment();
                        break;
                }

                fm.replace(R.id.menu_container,fragment).commit();
            }
        });

        return view;
    }
}