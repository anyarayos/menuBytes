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

public class MenuShawarmaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public MenuShawarmaFragment() {
    }


    public static MenuShawarmaFragment newInstance(String param1, String param2) {
        MenuShawarmaFragment fragment = new MenuShawarmaFragment();
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
        View view = inflater.inflate(R.layout.fragment_shawarma,null);

        //creating list for shawarma
        ListView listViewShawarma = (ListView) view.findViewById(R.id.shawarmaListView);
        ArrayList <ShawarmaListClass> shawarmaListClassArrayList = new ArrayList<>();
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Wrap - Regular", "45.00","Ground beef, cucumber, onion, tomato w/ garlic sauce & cheese sauce in pita."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Wrap - Large", "55.00","Ground beef, cucumber, onion, tomato w/ garlic sauce & cheese sauce in pita."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Salad Bowl - Regular", "55.00","Lettuce topped with ground beef, onion, tomato w/ garlic sauce & cheese."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Salad Bowl - Large", "80.00","Lettuce topped with ground beef, onion, tomato w/ garlic sauce & cheese."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Rice Bowl - Regular", "55.00","Java rice with beef, cucumber, onion, tomato w/ garlic sauce & cheese."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Rice Bowl - Large", "70.00","Java rice with beef, cucumber, onion, tomato w/ garlic sauce & cheese."));
        shawarmaListClassArrayList.add(new ShawarmaListClass(R.drawable.menubyteslogo, "Shawarma Nachos", "70.00","Nachos w/ beef, tomato, onion and cucumber, cheese and garlic sauce."));

        //shawarma custom adapter
        ShawarmaListAdapter shawarmaListAdapter = new ShawarmaListAdapter(getActivity(),R.layout.list_shawarma,shawarmaListClassArrayList);
        listViewShawarma.setAdapter(shawarmaListAdapter);


        //attempt to onclicklistener hehe

        listViewShawarma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new Menu_SWR_Fragment ();
                        break;
                    case 1:
                        fragment = new Menu_SWL_Fragment ();
                        break;
                    case 2:
                        fragment = new Menu_SSBR_Fragment ();
                        break;
                    case 3:
                        fragment = new Menu_SSBL_Fragment ();
                        break;
                    case 4:
                        fragment = new Menu_SRBR_Fragment ();
                        break;
                    case 5:
                        fragment = new Menu_SRBL_Fragment ();
                        break;
                    case 6:
                        fragment = new Menu_SNL_Fragment ();
                        break;
                }
                fm.replace(R.id.menu_container,fragment).commit();
            }
        });

        return view;
    }
}