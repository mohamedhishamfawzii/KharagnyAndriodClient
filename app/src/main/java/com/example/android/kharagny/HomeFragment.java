package com.example.android.kharagny;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends android.support.v4.app.Fragment {
    ListView listView;
    ArrayList<String> categoriesTxt;
    // custom adapter for the list view
    CustomAdapter customAdapter;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // pass the fragment you want to show(inflate) to the inflater
        View view = inflater.inflate(R.layout.fragment_home, null);
        // array list of the categories to be displayed in the listView
        categoriesTxt = new ArrayList<String>();
        // these categories are fixed
        categoriesTxt.add("Entertainment");
        categoriesTxt.add("Extra-ordinary Dining");
        categoriesTxt.add("Adventure");
        categoriesTxt.add("Night Life");
        categoriesTxt.add("Cinemas");
        listView = view.findViewById(R.id.listView);
        customAdapter = new CustomAdapter(getContext() ,  categoriesTxt);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),categoriesTxt.get(i), Toast.LENGTH_SHORT).show();
                intent = new Intent(getContext(),CategoriesActivity.class );
                intent.putExtra("category" , categoriesTxt.get(i));
                startActivity(intent);

            }
        });



        return view;


    }
}
