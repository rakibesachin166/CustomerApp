package com.dev.customerapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.customerapp.R;
import com.dev.customerapp.databinding.ActivityLoginBinding;
import com.dev.customerapp.utils.ExtensionKt;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.userLoginButton.setOnClickListener(v -> ExtensionKt.changeActivity(LoginActivity.this, MainActivity.class, true));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear the binding reference to avoid memory leaks
        binding = null;
    }
}