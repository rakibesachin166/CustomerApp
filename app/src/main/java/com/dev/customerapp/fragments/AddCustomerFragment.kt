package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentAddCustomerBinding


class AddCustomerFragment : Fragment() {
    private lateinit var binding: FragmentAddCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCustomerBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.customerSubmitButton.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val dob = binding.editTextDOB.text.toString().trim()
            val mobile = binding.editTextMobile.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val address = binding.editTextAddress.text.toString().trim()
            val houseNo = binding.editTextHouseNo.text.toString().trim()
            val locality = binding.editTextLocality.text.toString().trim()
            val block = binding.editTextBlock.text.toString().trim()
            val district = binding.editTextDistrict.text.toString().trim()
            val pinCode = binding.editTextPinCode.text.toString().trim()

            if (name.isEmpty()) {
                binding.editTextName.requestFocus()
                binding.editTextName.error = "Enter Name."
                return@setOnClickListener
            }

            if (dob.isEmpty()) {
                binding.editTextDOB.requestFocus()
                binding.editTextDOB.error = "Enter DOB."
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                binding.editTextMobile.requestFocus()
                binding.editTextMobile.error = "Enter Mobile Number."
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.editTextEmail.requestFocus()
                binding.editTextEmail.error = "Enter Email Id."
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.editTextAddress.requestFocus()
                binding.editTextAddress.error = "Enter Address."
                return@setOnClickListener
            }

            if (houseNo.isEmpty()) {
                binding.editTextHouseNo.requestFocus()
                binding.editTextHouseNo.error = "Enter House Number."
                return@setOnClickListener
            }

            if (locality.isEmpty()) {
                binding.editTextLocality.requestFocus()
                binding.editTextLocality.error = "Enter Locality."
                return@setOnClickListener
            }

            if (block.isEmpty()) {
                binding.editTextLocality.requestFocus()
                binding.editTextLocality.error = "Enter Block."
                return@setOnClickListener
            }

            if (district.isEmpty()) {
                binding.editTextLocality.requestFocus()
                binding.editTextLocality.error = "Enter District."
                return@setOnClickListener
            }

            if (pinCode.isEmpty()) {
                binding.editTextLocality.requestFocus()
                binding.editTextLocality.error = "Enter PinCode."
                return@setOnClickListener
            }
        }
    }

}