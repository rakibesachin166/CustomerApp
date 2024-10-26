package com.dev.customerapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.activityViewModels

import com.dev.customerapp.R;
import com.dev.customerapp.databinding.FragmentSelectUserStateBinding
import com.dev.customerapp.viewModels.CreateUserViewModel

public class SelectUserStateFragment : Fragment() {

    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectUserStateBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectUserStateBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

}