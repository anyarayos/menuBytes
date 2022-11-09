package com.example.menubytes_customerapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

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

    LoadingDialog loadingDialog;
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
    private Button btnAddToCart;
    private ConstraintLayout constraintLayout;
    private String category;

    private double priceTotal=0,addonsTotal=0, tempIntQty=0,price=0;

    private int wingsPiecesSellected, requiredFlavor;
    String tempQty;

    int PRODUCT_ID=-1;

    List<String> flavors = new ArrayList<String>();
    String flavour="";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Menu_WingsProd_Fragment() {
        // Required empty public constructor
    }

    public Menu_WingsProd_Fragment(int PRODUCT_ID, String CATEGORY) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.category = CATEGORY;
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
        loadingDialog = new LoadingDialog(this.getActivity());
        int product_id = PRODUCT_ID;

        if(product_id!=-1 && product_id!=-0){
            Task task = new Task(Task.RETRIEVE_PRODUCTS_BY_ID, new AsyncResponse() {
                @Override
                public void onFinish(Object output) {
                    ArrayList <ProductListClass> productListClassArrayList = (ArrayList<ProductListClass>)output;
                    try {
                        imgViewItemMenu.setImageDrawable(getDrawableFromAssets(productListClassArrayList.get(0).getImage()));
                        txtItemTitle.setText(productListClassArrayList.get(0).getName());
                        txtItemDescription.setText(productListClassArrayList.get(0).getDescription());
                        mealTotalText.setText(productListClassArrayList.get(0).getPrice());
                        priceTotal = Double.parseDouble(mealTotalText.getText().toString());
                        mealTotalText.setText(Double.toString(priceTotal)+"0");

                        constraintLayout.setVisibility(View.VISIBLE);
                        loadingDialog.dismissDialog();
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
        loadingDialog.startLoadingDialog();
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
        addButton = view.findViewById(R.id.btnUpdateOrder);
        addQtyButton = view.findViewById(R.id.addQtyButton);
        minusQtyButton = view.findViewById(R.id.minusQtyButton);
        constraintLayout = view.findViewById(R.id.frameLayoutwings);
        btnAddToCart = view.findViewById(R.id.btnUpdateOrder);

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



        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                fragment = new MenuWingsFragment();
                fm.replace(R.id.menu_container,fragment).commit();

                /*
                * TODO: ADD WINGS TO CART
                * */
                Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
            }
        });


        /*
        ChipGroup wingChipGroup = view.findViewById(R.id.category_chip_group);
        wingChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip wingChip = view.findViewById(checkedId);
                if (wingChip !=null) {
                    Toast.makeText(new Activity(), wingChip.getText().toString()+" ITU", Toast.LENGTH_SHORT).show();
                }

                wingChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if (isChecked){
                           List<Integer>ids = wingChipGroup.getCheckedChipIds();
                           if (ids.size()>2) {

                           }
                       }
                    }
                });
            }
        });*/

        return view;
    }

    public void checkIfFlavorExist(String flavour){
        if(!(flavors.contains(flavour))){
            flavors.add(flavour);
            Log.d("TAG", "checkIfFlavorExist: ");
        }
    }

    public void printFlavors(){
        if(!flavors.isEmpty()){
            for(int index = 0 ; index < flavors.size(); index++){
                Log.d("TAG", "printFlavors: " + flavors.get(index));
            }
        }
    }
}