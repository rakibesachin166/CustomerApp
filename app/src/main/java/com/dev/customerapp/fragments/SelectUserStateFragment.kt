package com.dev.customerapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.activityViewModels

import com.dev.customerapp.R;
import com.dev.customerapp.viewModels.CreateUserViewModel

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectUserStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectUserStateFragment : Fragment() {

    private val sharedViewModel: CreateUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_user_state, container, false)
    }

}