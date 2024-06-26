package com.example.menubytes_customerapp;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Menu_Product_Fragment extends Fragment {

    //i added this
    String tmpMealTotal,tempQty;
    double priceTotal=0,addonsTotal=0, tempIntQty=0;
    TextView qtyTxt, mealTotal,txtChoose,txtOption;
    Button minusQty, addQty, addToCart;
    RadioButton soloRad, b1t1Rad;
    RadioGroup radioGroup;
    CheckBox  cbAllMeat;
    ConstraintLayout constraintLayout;
    LoadingDialog loadingDialog;
    int PRODUCT_ID=-1;
    String category;
    String orderAddOnsName ="";

    Dialog addToCartDialog;

    TextView soloStringPrice;
    TextView b1t1StringPrice;

    ImageView imgViewItemMenu;
    TextView txtItemTitle;
    TextView txtShawarmaAllMeatPrice;
    TextView txtItemDescription;

    Boolean has_addons;

    String mealPrice;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<ProductListClass> productListClassArrayList = productListClassArrayList = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    private  boolean productBundle_checked = false;

    public Menu_Product_Fragment() {
    }

    public Menu_Product_Fragment(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }
    public Menu_Product_Fragment(int PRODUCT_ID,String CATEGORY) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.category = CATEGORY;
    }

    public static Menu_Product_Fragment newInstance(String param1, String param2) {
        Menu_Product_Fragment fragment = new Menu_Product_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this.getActivity());
            int product_id = PRODUCT_ID;

            if(product_id!=-1 && product_id!=-0){
                Task task = new Task(Task.RETRIEVE_PRODUCTS_BY_ID2, new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output==null){
                            loadingDialog.dismissDialog();
                        }
                        ArrayList <ProductListClass> productListClassArrayList = (ArrayList<ProductListClass>)output;
                        imgViewItemMenu.setImageBitmap(decodeBlobType(productListClassArrayList.get(0).getBytes()));
                            txtItemTitle.setText(productListClassArrayList.get(0).getName());
                            txtItemDescription.setText(productListClassArrayList.get(0).getDescription());
                            soloStringPrice.setText(productListClassArrayList.get(0).getPrice());
                            b1t1StringPrice.setText(productListClassArrayList.get(0).getProductBundle());
                            priceTotal = Double.parseDouble(soloStringPrice.getText().toString());
                            mealTotal.setText(Double.toString(priceTotal)+"0");
                            if(productListClassArrayList.get(0).getProductBundle()==null){
                                b1t1Rad = getView().findViewById(R.id.b1t1RadioButton);
                                b1t1Rad.setVisibility(View.GONE);
                            }
                            if(txtItemTitle.getText().toString()!=null){
                                if(txtItemTitle.getText().toString().equals("Shawarma Salad Bowl")
                                || txtItemTitle.getText().toString().equals("Shawarma Salad (Large)")
                                ){
                                    cbAllMeat.setVisibility(View.GONE);
                                    txtShawarmaAllMeatPrice.setVisibility(View.GONE);
                                    txtChoose.setVisibility(View.GONE);
                                            txtOption.setVisibility(View.GONE);
                                }
                                if(category.equals("bowl")){
                                    cbAllMeat.setVisibility(View.GONE);
                                    txtShawarmaAllMeatPrice.setVisibility(View.GONE);
                                    txtChoose.setVisibility(View.GONE);
                                    txtOption.setVisibility(View.GONE);
                                }
                            }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_product,null);
        loadingDialog.startLoadingDialog();
        constraintLayout = view.findViewById(R.id.fragment_frameLayout);
        txtItemTitle = view.findViewById(R.id.txtItemTitle);
        //meal total changing
        mealTotal = (TextView) view.findViewById(R.id.mealTotalText);
        txtChoose = view.findViewById(R.id.txtChoose);
        txtOption = view.findViewById(R.id.txtOption);


        soloStringPrice = view.findViewById(R.id.soloPrice);
        b1t1StringPrice = view.findViewById(R.id.b1t1Price);

        addToCartDialog = new Dialog(getActivity());
        addToCartDialog.setContentView(R.layout.added_to_cart_dialog);
        addToCartDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialog_background));
        addToCartDialog.setCancelable(false);
        addToCartDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;



        //quantity changing
        imgViewItemMenu = view.findViewById(R.id.imgViewItemMenu);
        txtItemTitle = view.findViewById(R.id.txtItemTitle);
        txtItemDescription = view.findViewById(R.id.txtItemDescription);
        soloStringPrice = view.findViewById(R.id.soloPrice);
        b1t1StringPrice = view.findViewById(R.id.b1t1Price);

        qtyTxt = (TextView) view.findViewById(R.id.qtyText);
        minusQty = (Button) view.findViewById(R.id.minusQtyButton);
        addQty = (Button) view.findViewById(R.id.addQtyButton);
        tempQty =  qtyTxt.getText().toString();
        qtyTxt.setText(tempQty);

        txtShawarmaAllMeatPrice = view.findViewById(R.id.txtShawarmaAllMeatPrice);

        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempQty =  qtyTxt.getText().toString();
                tempIntQty = Double.parseDouble(tempQty)-1;
                tempQty = Integer.toString((int) tempIntQty);
                qtyTxt.setText(tempQty);
                if (tempIntQty==1){
                    minusQty.setEnabled(false);
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)* Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });
        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempQty =  qtyTxt.getText().toString();
                int tempIntQty = Integer.parseInt(tempQty)+1;
                tempQty = Integer.toString(tempIntQty);
                qtyTxt.setText(tempQty);
                if (tempIntQty==1){
                    minusQty.setEnabled(false);
                }
                else if (tempIntQty>0){
                    minusQty.setEnabled(true);
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });

        //Type of Meal Changing

        radioGroup = (RadioGroup) view.findViewById(R.id.typeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.soloRadioButton:
                        productBundle_checked = false;
                        priceTotal = Double.parseDouble(soloStringPrice.getText().toString());
                        mealPrice = soloStringPrice.getText().toString();
                        txtShawarmaAllMeatPrice.setText("10.00");
                        if (cbAllMeat.isChecked()){
                            addonsTotal = 10.00;
                        }
                        mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
                        break;
                    case R.id.b1t1RadioButton:
                        productBundle_checked = true;
                        priceTotal = Double.parseDouble(b1t1StringPrice.getText().toString());
                        mealPrice = b1t1StringPrice.getText().toString();
                        txtShawarmaAllMeatPrice.setText("20.00");
                        if (cbAllMeat.isChecked()){
                            addonsTotal = 20.00;
                        }
                        mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
                        break;
                }

            }
        });
        cbAllMeat = (CheckBox) view.findViewById(R.id.checkBox_allmeat);

        cbAllMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_allmeat = cbAllMeat.isChecked();
                if (isChecked_allmeat == true){
                    addonsTotal = addonsTotal + Double.parseDouble(txtShawarmaAllMeatPrice.getText().toString());
                    orderAddOnsName = "Shawarma All Meat";
                }
                else if (isChecked_allmeat == false){
                    addonsTotal = addonsTotal - Double.parseDouble(txtShawarmaAllMeatPrice.getText().toString());
                    orderAddOnsName = "";
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });
        ChipNavigationBar chipNavigationBar = view.findViewById(R.id.foodMenuBar);

        addToCart = view.findViewById(R.id.btnUpdateOrder);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCartDialog.show();

                //Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                String finalName = txtItemTitle.getText().toString();
                String finalTotal = mealTotal.getText().toString();
                String finalQty = qtyTxt.getText().toString();

               if(cbAllMeat.isChecked() || orderAddOnsName!=""){
                   has_addons = true;

               }else{
                   has_addons = false;
               }

//                Toast.makeText(getActivity(), ""+has_addons, Toast.LENGTH_SHORT).show();



                OrderListClass order = new OrderListClass(PRODUCT_ID, finalName,
                        mealPrice,
                        finalQty,
                        category ,
                        productBundle_checked,
                        orderAddOnsName,
                        finalTotal,
                        has_addons);


                Utils.getInstance().addToOrders(order);
            }
        });

        Button backButton = view.findViewById(R.id.btn_back1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                switch(category){
                    case "shawarma":
                        fragment = new MenuShawarmaFragment();
                        break;
                    case "bowl":
                        fragment = new MenuBowlFragment();
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
                    case "shawarma":
                        fragment = new MenuShawarmaFragment();
                        break;
                    case "bowl":
                        fragment = new MenuBowlFragment();
                }
                fm.replace(R.id.menu_container,fragment).commit();
            }
        });

        Button goToCart = addToCartDialog.findViewById(R.id.btn_go_cart);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}