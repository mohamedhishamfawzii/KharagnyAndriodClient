package com.example.android.kharagny;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ActivityFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // firebase integration
       /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
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
            }*/

        return inflater.inflate(R.layout.fragment_activity, null);
    }
}
