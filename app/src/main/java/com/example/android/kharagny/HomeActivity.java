package com.example.android.kharagny;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new HomeFragment());

        String uid = getIntent().getStringExtra("uid");
        if (!uid.equals(""))
            popAnAlertDialogUp("[debug] uid", uid);
        else
            popAnAlertDialogUp("Error", "no uid received");

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationDrawerView = findViewById(R.id.navigation_drawer_nav_vw);
        navigationDrawerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Toast.makeText(HomeActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        android.support.v4.app.Fragment fragment = null;
        switch(item.getItemId())
        {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new ActivityFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new AccountsFragment();
        }
        loadFragment(fragment);
        return true;
    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment)
    {
        //switching fragment
        if (fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container , fragment)
                    .commit();
            return true;
        }
        return false;
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
}
