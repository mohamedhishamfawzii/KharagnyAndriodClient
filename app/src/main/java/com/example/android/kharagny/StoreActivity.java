package com.example.android.kharagny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StoreActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        intent = getIntent();
        String storeName = intent.getStringExtra("storeName");
        Toast.makeText(this,storeName, Toast.LENGTH_SHORT).show();

    }
}
