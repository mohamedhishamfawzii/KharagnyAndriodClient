package com.example.android.kharagny;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AccountsFragment extends android.support.v4.app.Fragment {
    ListView listView;
    ArrayList<String> categoriesTxt;
    ArrayAdapter arrayAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, null);
        categoriesTxt = new ArrayList<String>();
        categoriesTxt.add("account1");
        categoriesTxt.add("account2");
        categoriesTxt.add("account3");
        categoriesTxt.add("account4");
        categoriesTxt.add("account5");
        listView = view.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1 , categoriesTxt);
        listView.setAdapter(arrayAdapter);
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
       return view;
    }
}
