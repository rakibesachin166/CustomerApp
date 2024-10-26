package com.dev.customerapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.dev.customerapp.fragments.CreateUserFormFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the drawer layout and toggle
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up the navigation view
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            // Handle navigation item clicks
            int id = menuItem.getItemId();
            if (id == R.id.navigation_add_user) {
                changeFragment(new CreateUserFormFragment());
            }
            drawerLayout.closeDrawers(); // Close the drawer after selection
            return true;
        });

        // Load the default fragment if this is the first creation
        if (savedInstanceState == null) {
            changeFragment(new CreateUserFormFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.drawerLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
