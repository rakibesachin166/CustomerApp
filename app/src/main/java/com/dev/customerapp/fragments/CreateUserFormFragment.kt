package com.dev.customerapp.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentCreateUserFormBinding
import com.dev.customerapp.viewModels.CreateUserViewModel


class CreateUserFormFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateUserFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserFormBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}