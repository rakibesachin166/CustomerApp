package com.dev.customerapp.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.viewModels.CreateUserViewModel


class CreateUserFormFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View? {
         return inflater.inflate(R.layout.fragment_create_user_form, container, false)
     }
}