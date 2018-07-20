package com.example.android.kharagny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity
{
    Intent intent;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef;
    SharedPreferences lastLoginSharedPreferences;

    public void loginFunction(View view)
    {
        intent = new Intent(getApplicationContext() ,MainActivity.class );
        startActivity(intent);
    }
    public void signFunction(View view)
    {
        intent = new Intent(getApplicationContext() , RegActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView)findViewById(R.id.imageView7);
        Animation animation = new AlphaAnimation(0.00f , 1.00f);
        animation.setDuration(2000);
        imageView.startAnimation(animation);
        imageView.animate().rotationBy(360).setDuration(2000);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        //remember last login

        lastLoginSharedPreferences = getSharedPreferences("last_login", MODE_PRIVATE);
        String lastLoginProvider = lastLoginSharedPreferences.getString("provider", "");

        if (lastLoginProvider.equals("email"))
        {
            String email = lastLoginSharedPreferences.getString("email", "");
            String pass = lastLoginSharedPreferences.getString("pass", "");

            if (email.equals("") || pass.equals(""))
                showTheTwoButtons();
            else
            {
                Task<AuthResult> emailSignInTask = firebaseAuth.signInWithEmailAndPassword(email, pass);

                emailSignInTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {@Override public void onSuccess(AuthResult authResult)
                {
                    updateDatabase(authResult.getUser());
                }});
                emailSignInTask.addOnFailureListener(new OnFailureListener() {@Override public void onFailure(@NonNull Exception e)
                {
                    showTheTwoButtons();
                }});
            }
        }
        else if (lastLoginProvider.equals("facebook"))
        {
            String idToken = lastLoginSharedPreferences.getString("id_token", "");
            if (idToken.equals(""))
                showTheTwoButtons();
            else
            {
                System.out.println("/// facebook idToken: " + idToken);
                AuthCredential fbAuthCredential = FacebookAuthProvider.getCredential(idToken);
                connectToFirebaseWithCredential(fbAuthCredential);
            }
        }
        else if (lastLoginProvider.equals("google"))
        {
            String idToken = lastLoginSharedPreferences.getString("id_token", "");
            if (idToken.equals(""))
                showTheTwoButtons();
            else
            {
                System.out.println("/// google idToken: " + idToken);
                AuthCredential gAuthCredential = GoogleAuthProvider.getCredential(idToken, null);
                connectToFirebaseWithCredential(gAuthCredential);
            }
        }
        else
            showTheTwoButtons();


        //end: remember last login
    }


    private void connectToFirebaseWithCredential(AuthCredential credential)
    {
        Task<AuthResult> connectToFirebaseTask = firebaseAuth.signInWithCredential(credential);

        //on success
        connectToFirebaseTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {@Override public void onComplete(@NonNull Task<AuthResult> task)
        {
            if (task.isSuccessful())
            {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                popAnAlertDialogUp("facebook auto login", firebaseUser.getDisplayName());
                updateDatabase(firebaseUser);
            }
        }});
        //on failure
        connectToFirebaseTask.addOnFailureListener(new OnFailureListener() {@Override public void onFailure(@NonNull Exception e)
        {
            showTheTwoButtons();
        }});
    }

    private void updateDatabase(FirebaseUser firebaseUser)
    {
        User newUser = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), null);
        HashMap<String,Object> newUserToDatabase = new HashMap<>();
        newUserToDatabase.put(firebaseUser.getUid(), newUser);

        usersRef.updateChildren(newUserToDatabase).addOnFailureListener(new OnFailureListener() {@Override public void onFailure(@NonNull Exception e)
        {
            popAnAlertDialogUp("Error", e.toString() + "\nTry again later");
        }});

        goToHome(firebaseUser.getUid());
    }

    private void goToHome(String uid)
    {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("uid", uid);
        startActivity(homeIntent);
        /*//sign out
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor lastLoginSharedPreferenceEditor = lastLoginSharedPreferences.edit();
        lastLoginSharedPreferenceEditor.putString("provider", "");
        lastLoginSharedPreferenceEditor.commit();*/
    }

    private void showTheTwoButtons()
    {
        findViewById(R.id.splash_sign_in_butt).setVisibility(View.VISIBLE);
        findViewById(R.id.splash_sign_up_butt).setVisibility(View.VISIBLE);
    }




    //tools
    void popAnAlertDialogUp(Object title, Object message)
    {
        if (title == null)
            title = "";
        if (message == null)
            message = "";

        new AlertDialog.Builder(this)
                .setTitle(title.toString())
                .setMessage(message.toString())
                .show();
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.android.kharagny", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                System.out.println("/// " + "printHashKey() Hash Key: " + hashKey);

            }
        } catch (Exception e) {
            Log.e("///", "printHashKey()", e);
        }
    }
}
