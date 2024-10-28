package com.dev.customerapp.activity;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.dev.customerapp.R;
import com.dev.customerapp.fragments.CreateUserFormFragment;
import com.dev.customerapp.fragments.SelectUserStateFragment;
import com.dev.customerapp.fragments.SelectUserTypeFragment;
import com.dev.customerapp.viewModels.CreateUserViewModel;

public class CreateUserActivity extends AppCompatActivity {
    CreateUserViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        sharedViewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);

        // Observe currentPage LiveData
        sharedViewModel.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer page) {
                Log.d("sachin", "$page" + page);

                switch (page) {
                    case 0:
                        changeFragment(new SelectUserTypeFragment());
                        break;
                    case 1:
                        changeFragment(new SelectUserStateFragment());
                        break;
                    case 2:
                        changeFragment(new CreateUserFormFragment());
                        break;
                    default:
                        changeFragment(new SelectUserTypeFragment());
                }
            }
        });
//        changeFragment(new SelectUserTypeFragment());

    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.userCreateFrameLayout, fragment, fragment.getClass().getSimpleName())
                .commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int fragments = fragmentManager.getBackStackEntryCount();

        if (fragments == 1) {
            finish();
        } else if (fragments > 1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}