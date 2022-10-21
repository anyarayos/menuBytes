package com.example.menubytes_customerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Menu_SWR_Fragment extends Fragment {

    //i added this
    String tmpMealTotal,tempQty;
    int priceTotal=45,addonsTotal=0, tempIntQty=1;
    TextView qtyTxt, mealTotal;
    Button minusQty, addQty, addToCart;
    RadioButton soloRad, b1t1Rad;
    RadioGroup radioGroup;
    CheckBox cbJava, cbCheese, cbGarlic, cbAllMeat;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Menu_SWR_Fragment() {
    }

    public static Menu_SWR_Fragment newInstance(String param1, String param2) {
        Menu_SWR_Fragment fragment = new Menu_SWR_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_menu_swr,null);

        //meal total changing
        mealTotal = (TextView) view.findViewById(R.id.mealTotalText);
        mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*tempIntQty)+".00");

        //quantity changing
        qtyTxt = (TextView) view.findViewById(R.id.qtyText);
        minusQty = (Button) view.findViewById(R.id.minusQtyButton);
        addQty = (Button) view.findViewById(R.id.addQtyButton);
        tempQty =  qtyTxt.getText().toString();
        qtyTxt.setText(tempQty);
        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempQty =  qtyTxt.getText().toString();
                tempIntQty = Integer.parseInt(tempQty)-1;
                tempQty = Integer.toString(tempIntQty);
                qtyTxt.setText(tempQty);
                if (tempIntQty==0){
                    minusQty.setEnabled(false);
                }
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
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
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
            }
        });

        //Type of Meal Changing

        radioGroup = (RadioGroup) view.findViewById(R.id.typeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.soloRadioButton:
                        priceTotal = 45;
                        mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
                        break;
                    case R.id.b1t1RadioButton:
                        priceTotal = 79;
                        mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
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
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
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
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
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
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
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
                mealTotal.setText(Integer.toString((priceTotal+addonsTotal)*Integer.parseInt(qtyTxt.getText().toString()))+".00");
            }
        });


        return view;
    }
}