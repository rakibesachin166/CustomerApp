package com.dev.customerapp.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentSelectUserTypeBinding
import com.dev.customerapp.models.UserTypes
import com.dev.customerapp.viewModels.CreateUserViewModel


public class SelectUserTypeFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectUserTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectUserTypeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.setOnClickListener {
            sharedViewModel.setUserType(
                when (binding.userTypeSpinner.selectedItemPosition) {
                    0 -> UserTypes.STATE_OFFICER
                    1 -> UserTypes.DIVISIONAL_OFFICER
                    2 -> UserTypes.DISTRICT_OFFICER
                    3 -> UserTypes.BLOCK_OFFICER
                    else -> throw IllegalStateException("Unknown user type")
                }
            )
            sharedViewModel.setCurrentPage(1)
        }
    }

}