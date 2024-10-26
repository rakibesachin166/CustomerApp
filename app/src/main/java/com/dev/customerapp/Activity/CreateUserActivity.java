package com.dev.customerapp.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.dev.customerapp.R;
import com.dev.customerapp.adapter.ViewPagerAdapter;
import com.dev.customerapp.viewModels.CreateUserViewModel;

public class CreateUserActivity extends AppCompatActivity {
    private CreateUserViewModel  sharedViewModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        sharedViewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);
    }
}