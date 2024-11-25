package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentCreateIDProofBinding
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.FunctionsConstant.Companion.getUserFullId
import com.dev.customerapp.utils.FunctionsConstant.Companion.getUserRoleName
import com.dev.customerapp.utils.loadImage


class CreateIDProofFragment : Fragment() {
    private lateinit var binding: FragmentCreateIDProofBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateIDProofBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userData = Constant(requireActivity()).getUserData()

        if (
            userData != null
        ) {
            binding.userName.text = userData.userName
            binding.userId.text = getUserFullId(userData.userType , userData.userId)
            binding.userDesignation.text = getUserRoleName(userData.userType)
            binding.userMobileNumber.text = userData.userMobileNo
            binding.userCity.text = userData.userCity
            binding.userEmail.text = userData.userEmail
            binding.userImage.loadImage(ApiClient.BASE_URL + userData.userPhoto)
        }
    }
}