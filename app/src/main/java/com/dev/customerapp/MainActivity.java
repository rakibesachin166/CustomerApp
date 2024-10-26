package com.dev.customerapp;

import static com.dev.customerapp.utils.ExtensionKt.changeActivity;
import static com.dev.customerapp.utils.ExtensionKt.printLog;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.dev.customerapp.Activity.CreateUserActivity;
import com.dev.customerapp.fragments.CreateUserFormFragment;
import com.dev.customerapp.utils.ExtensionKt;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            closeDrawer();
            int id = item.getItemId();
            if (id == R.id.navigation_add_user) {
                printLog("sachin","Click");
                ExtensionKt.changeActivity(MainActivity.this, CreateUserActivity.class, false);
            }
            return false;
        });

    }


    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.drawerLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
