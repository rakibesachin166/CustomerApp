package com.dev.customerapp.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dev.customerapp.fragments.CreateUserFormFragment;
import com.dev.customerapp.fragments.SelectUserStateFragment;
import com.dev.customerapp.fragments.SelectUserTypeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CreateUserFormFragment();
            case 1:
                return new SelectUserStateFragment();
            case 2:
                return new SelectUserTypeFragment();
            default:
                return new CreateUserFormFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
