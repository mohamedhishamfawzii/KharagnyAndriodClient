package com.example.android.kharagny;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CategoriesActivity extends AppCompatActivity {
    Intent intent;
    ListView listView;
    ArrayList<String> storesList;
    CustomAdapter customAdapter;
    String storeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        storesList = new ArrayList<>();
        intent = getIntent();
        String category = intent.getStringExtra("category");
        Toast.makeText(this,category, Toast.LENGTH_SHORT).show();

        // creating dummy stores for list view
        storesList.add("costa");
        storesList.add("carlos");
        storesList.add("tamara");
        // initializing the list view
        listView = (ListView)findViewById(R.id.listView);
        customAdapter = new CustomAdapter(getApplicationContext() , storesList);
        listView.setAdapter(customAdapter);

        // click listener for the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(getApplicationContext() , StoreActivity.class);
                intent.putExtra("storeDetails" , storeName);
                startActivity(intent);

            }
        });

        // firebase integration
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryDatabaseRef = databaseReference.child(category);
        categoryDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> categoryItr = dataSnapshot.getChildren().iterator();

                while (categoryItr.hasNext())
                {
                    DataSnapshot store = categoryItr.next();
                    storeName = store.child("name").getValue().toString();
                    storesList.add(store.child("name").getValue().toString());
                    listView.setAdapter(customAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
