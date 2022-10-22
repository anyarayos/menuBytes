package com.example.menubytes_customerapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Menu_Product_Fragment extends Fragment {

    //i added this
    String tmpMealTotal,tempQty;
    double priceTotal=0,addonsTotal=0, tempIntQty=0;
    TextView qtyTxt, mealTotal;
    Button minusQty, addQty, addToCart;
    RadioButton soloRad, b1t1Rad;
    RadioGroup radioGroup;
    CheckBox cbJava, cbCheese, cbGarlic, cbAllMeat;
    ConstraintLayout constraintLayout;

    int PRODUCT_ID=-1;

    ImageView imgViewItemMenu;
    TextView txtItemTitle;
    TextView txtItemDescription;
    TextView soloPrice;
    TextView b1t1Price;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<ProductListClass> productListClassArrayList = productListClassArrayList = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    public Menu_Product_Fragment() {
    }

    public Menu_Product_Fragment(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
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

            int product_id = PRODUCT_ID;

            if(product_id!=-1 && product_id!=-0){
                Task task = new Task("retrieveProductsByID", new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        ArrayList <ProductListClass> productListClassArrayList = (ArrayList<ProductListClass>)output;
                        try {
                            imgViewItemMenu.setImageDrawable(getDrawableFromAssets(productListClassArrayList.get(0).getImage()));
                            txtItemTitle.setText(productListClassArrayList.get(0).getName());
                            txtItemDescription.setText(productListClassArrayList.get(0).getDescription());
                            soloPrice.setText(productListClassArrayList.get(0).getPrice());
                            b1t1Price.setText(productListClassArrayList.get(0).getProductBundle());
                            priceTotal = Double.parseDouble(soloPrice.getText().toString());
                            mealTotal.setText(Double.toString(priceTotal));
                            if(productListClassArrayList.get(0).getProductBundle()==null){
                                b1t1Rad = getView().findViewById(R.id.b1t1RadioButton);
                                b1t1Rad.setVisibility(View.GONE);
                            }

                            constraintLayout.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                task.execute(String.valueOf(product_id));
            }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        View view = inflater.inflate(R.layout.fragment_menu_product,null);

        constraintLayout = view.findViewById(R.id.fragment_frameLayout);

        //meal total changing
        mealTotal = (TextView) view.findViewById(R.id.mealTotalText);


        //quantity changing
        imgViewItemMenu = view.findViewById(R.id.imgViewItemMenu);
        txtItemTitle = view.findViewById(R.id.txtItemTitle);
        txtItemDescription = view.findViewById(R.id.txtItemDescription);
        soloPrice = view.findViewById(R.id.soloPrice);
        b1t1Price = view.findViewById(R.id.b1t1Price);

        qtyTxt = (TextView) view.findViewById(R.id.qtyText);
        minusQty = (Button) view.findViewById(R.id.minusQtyButton);
        addQty = (Button) view.findViewById(R.id.addQtyButton);
        tempQty =  qtyTxt.getText().toString();
        qtyTxt.setText(tempQty);
        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempQty =  qtyTxt.getText().toString();
                tempIntQty = Double.parseDouble(tempQty)-1;
                tempQty = Integer.toString((int) tempIntQty);
                qtyTxt.setText(tempQty);
                if (tempIntQty==0){
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
                if (tempIntQty==0){
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
                        priceTotal = Double.parseDouble(soloPrice.getText().toString());
                        mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
                        break;
                    case R.id.b1t1RadioButton:
                        priceTotal = Double.parseDouble(b1t1Price.getText().toString());
                        mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
                        break;
                }

            }
        });

        //addons changing
        cbJava = (CheckBox) view.findViewById(R.id.checkBox_java);
        cbCheese = (CheckBox) view.findViewById(R.id.checkBox_cheese);
        cbGarlic = (CheckBox) view.findViewById(R.id.checkBox_garlic);
        cbAllMeat = (CheckBox) view.findViewById(R.id.checkBox_allmeat);

        cbJava.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_java = cbJava.isChecked();
                if (isChecked_java == true){
                    addonsTotal = addonsTotal + 15;
                }
                else if (isChecked_java == false){
                    addonsTotal = addonsTotal - 15;
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });

        cbCheese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_cheese = cbCheese.isChecked();
                if (isChecked_cheese == true){
                    addonsTotal = addonsTotal + 10;
                }
                else if (isChecked_cheese == false){
                    addonsTotal = addonsTotal - 10;
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });

        cbGarlic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_garlic = cbGarlic.isChecked();
                if (isChecked_garlic == true){
                    addonsTotal = addonsTotal + 10;
                }
                else if (isChecked_garlic == false){
                    addonsTotal = addonsTotal - 10;
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });

        cbAllMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_allmeat = cbAllMeat.isChecked();
                if (isChecked_allmeat == true){
                    addonsTotal = addonsTotal + 10;
                }
                else if (isChecked_allmeat == false){
                    addonsTotal = addonsTotal - 10;
                }
                mealTotal.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyTxt.getText().toString()))+"0");
            }
        });


        return view;
    }
}