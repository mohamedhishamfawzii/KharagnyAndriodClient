package com.example.android.kharagny;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    ImageView imageView;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView)findViewById(R.id.imageView7);
        Animation animation = new AlphaAnimation(0.00f , 1.00f);
        animation.setDuration(2000);
        imageView.startAnimation(animation);
        imageView.animate().rotationBy(360).setDuration(2000);
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
