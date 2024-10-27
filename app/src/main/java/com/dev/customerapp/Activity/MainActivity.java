package com.dev.customerapp.Activity;

import static com.dev.customerapp.utils.ExtensionKt.printLog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.dev.customerapp.R;
import com.dev.customerapp.fragments.AccountFragment;
import com.dev.customerapp.fragments.AddCustomerFragment;
import com.dev.customerapp.fragments.HomeFragment;
import com.dev.customerapp.utils.ExtensionKt;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

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
        View headerView = navigationView.getHeaderView(0);
        TextView headerButton = headerView.findViewById(R.id.tvSignIn);
        headerButton.setOnClickListener(v -> ExtensionKt.changeActivity(MainActivity.this, LoginActivity.class, true));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            closeDrawer();
            int id = item.getItemId();
            if (id == R.id.navigation_add_user) {
                printLog("sachin", "Click");
                ExtensionKt.changeActivity(MainActivity.this, CreateUserActivity.class, false);
            } else if (id == R.id.navigation_add_customer) {
                changeFragment(new AddCustomerFragment());
            }
            return false;
        });


        if (savedInstanceState == null) {
            changeFragment(new HomeFragment());
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        changeFragment(new HomeFragment());

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.bottom_home) {
            printLog("navin", "home");
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.bottom_account) {
            selectedFragment = new AccountFragment();
        }
        if (selectedFragment != null) {
            changeFragment(selectedFragment);
        }
        return true;
    };


    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
