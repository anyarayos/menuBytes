package com.example.menubytes_customerapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu_WingsProd_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu_WingsProd_Fragment extends Fragment {

    private TextView txtItemTitle;
    private TextView txtItemDescription;
    private TextView mealTotalText;
    private TextView qtyText;
    private ImageView imgViewItemMenu;
    private ChipGroup category_chip_group;
    private Chip garlicPar,saltedEgg,buffaloFlav,bulgogiFlav,soyGarlic,sesameHoney;
    private CheckBox checkBox_java;
    private CheckBox checkBox_cheese;
    private CheckBox checkBox_garlic;
    private Button addButton;
    private Button addQtyButton;
    private Button minusQtyButton;
    private ConstraintLayout constraintLayout;

    private double priceTotal=0,addonsTotal=0, tempIntQty=0,price=0;

    String tempQty;

    int PRODUCT_ID=-1;

    List<String> flavors = new ArrayList<String>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Menu_WingsProd_Fragment() {
        // Required empty public constructor
    }

    public Menu_WingsProd_Fragment(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }


    public static Menu_WingsProd_Fragment newInstance(String param1, String param2) {
        Menu_WingsProd_Fragment fragment = new Menu_WingsProd_Fragment();
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
                        mealTotalText.setText(productListClassArrayList.get(0).getPrice());
                        priceTotal = Double.parseDouble(mealTotalText.getText().toString());
                        mealTotalText.setText(Double.toString(priceTotal));

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
        View view = inflater.inflate(R.layout.fragment_menu__wingsprod,null);

        txtItemTitle = view.findViewById(R.id.txtItemTitle);
        txtItemDescription = view.findViewById(R.id.txtItemDescription);
        mealTotalText = view.findViewById(R.id.mealTotalText);
        qtyText = view.findViewById(R.id.qtyText);
        imgViewItemMenu = view.findViewById(R.id.imgViewItemMenu);
        category_chip_group = view.findViewById(R.id.category_chip_group);
        garlicPar = view.findViewById(R.id.garlicPar);
        saltedEgg = view.findViewById(R.id.saltedEgg);
        buffaloFlav = view.findViewById(R.id.saltedEgg);
        bulgogiFlav = view.findViewById(R.id.bulgogiFlav);
        soyGarlic = view.findViewById(R.id.soyGarlic);
        sesameHoney = view.findViewById(R.id.sesameHoney);
        checkBox_java = view.findViewById(R.id.checkBox_java);
        checkBox_cheese = view.findViewById(R.id.checkBox_cheese);
        checkBox_garlic = view.findViewById(R.id.checkBox_garlic);
        addButton = view.findViewById(R.id.addButton);
        addQtyButton = view.findViewById(R.id.addQtyButton);
        minusQtyButton = view.findViewById(R.id.minusQtyButton);
        constraintLayout = view.findViewById(R.id.frameLayoutwings);

        tempQty =  qtyText.getText().toString();
        qtyText.setText(tempQty);

        minusQtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempQty =  qtyText.getText().toString();
                tempIntQty = Double.parseDouble(tempQty)-1;
                tempQty = Integer.toString((int) tempIntQty);
                qtyText.setText(tempQty);
                if (tempIntQty==0){
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
                if (tempIntQty==0){
                    minusQtyButton.setEnabled(false);
                }
                else if (tempIntQty>0){
                    minusQtyButton.setEnabled(true);
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });

        category_chip_group.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                for(int id=0 ; id < checkedIds.size();id++){
                    switch(checkedIds.get(id)){
                        case R.id.garlicPar:
                            flavors.add("garlic parmesan");
                            break;
                        case R.id.saltedEgg:
                            flavors.add("salted egg");
                            break;
                        case R.id.buffaloFlav:
                            flavors.add("buffalo");
                            break;
                        case R.id.bulgogiFlav:
                            flavors.add("bulgogi");
                            break;
                        case R.id.soyGarlic:
                            flavors.add("soy garlic");
                            break;
                        case R.id.sesameHoney:
                            flavors.add("sesame honey");
                            break;
                    }
                }

            }
        });

        checkBox_java.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_java = checkBox_java.isChecked();
                if (isChecked_java == true){
                    addonsTotal = addonsTotal + 15;
                }
                else if (isChecked_java == false){
                    addonsTotal = addonsTotal - 15;
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });

        checkBox_cheese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_cheese = checkBox_cheese.isChecked();
                if (isChecked_cheese == true){
                    addonsTotal = addonsTotal + 10;
                }
                else if (isChecked_cheese == false){
                    addonsTotal = addonsTotal - 10;
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });

        checkBox_garlic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_garlic = checkBox_garlic.isChecked();
                if (isChecked_garlic == true){
                    addonsTotal = addonsTotal + 10;
                }
                else if (isChecked_garlic == false){
                    addonsTotal = addonsTotal - 10;
                }
                mealTotalText.setText(Double.toString((priceTotal+addonsTotal)*Double.parseDouble(qtyText.getText().toString()))+"0");
            }
        });

        return view;
    }
}