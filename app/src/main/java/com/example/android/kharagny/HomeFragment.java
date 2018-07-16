package com.example.android.kharagny;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeFragment extends android.support.v4.app.Fragment {
    ListView listView;
    ArrayList<String> categoriesTxt;
    ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        categoriesTxt = new ArrayList<String>();
        categoriesTxt.add("haggag");
        categoriesTxt.add("kirollos");
        categoriesTxt.add("hisham");
        categoriesTxt.add("shereef");
        categoriesTxt.add("sara");
        listView = view.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1 , categoriesTxt);
        listView.setAdapter(arrayAdapter);

       // categoriesTxt = new ArrayList<String>();
       // listView = (ListView)getActivity().findViewById(R.id.listView).get;

     //   listView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext() , android.R.layout.simple_list_item_1 , categoriesTxt));
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return view;


    }
}
