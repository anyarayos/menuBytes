package com.example.menubytes_customerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuWingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuWingsFragment extends Fragment {

    public final static String PRODUCT_ID= "PRODUCT_ID";
    LoadingDialog loadingDialog;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String category="wings";

    private String mParam1;
    private String mParam2;

    private ArrayList<ProductListClass> productListClassArrayList = productListClassArrayList = new ArrayList<>();

    public MenuWingsFragment() {
    }


    public static MenuWingsFragment newInstance(String param1, String param2) {
        MenuWingsFragment fragment = new MenuWingsFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_item_list,null);
        loadingDialog.startLoadingDialog();
        ListView listViewProducts = (ListView) view.findViewById(R.id.productsListView);
        Task task = new Task(getContext(),Task.RETRIEVE_PRODUCTS_BY_CATEGORY, new AsyncResponse() {
            @Override
            public void onFinish(Object output) {
                productListClassArrayList = (ArrayList<ProductListClass>)output;
                ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(),R.layout.list_product, productListClassArrayList);
                listViewProducts.setAdapter(productListAdapter);
                loadingDialog.dismissDialog();
                listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment fragment = null;
                        fragment = new Menu_WingsProd_Fragment(productListClassArrayList.get(position).getId(),category);
                        fm.replace(R.id.menu_container,fragment).commit();
                    }
                });
            }
        });
        task.execute("wings");

        return view;
    }
}