package com.example.android.kharagny;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText username;
    EditText password;
    public void loginFunc(View view)
    {
        Log.i("status" , "pressed");
    }
    Intent intent ;
    View fbButt, gButt;
    CallbackManager fbCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbButt = findViewById(R.id.facebooklogin);
        gButt = findViewById(R.id.googlelogin);
        button = (Button)findViewById(R.id.button);
        username = (EditText)findViewById(R.id.emailText);
        password = (EditText)findViewById(R.id.editText3);

        fbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                //TODO: when user cancels the login process
            }

            @Override
            public void onError(FacebookException error) {
                //TODO: when an error happens
            }
        });

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference test1Ref = firebaseDatabase.child("test1");
        test1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> allElementsInTest1 = dataSnapshot.getChildren();
                Iterator<DataSnapshot> test1Iterator = allElementsInTest1.iterator();
                while (test1Iterator.hasNext())
                {
                    DataSnapshot item = test1Iterator.next();
                    System.out.println(("///" + item.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fbButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        gButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
