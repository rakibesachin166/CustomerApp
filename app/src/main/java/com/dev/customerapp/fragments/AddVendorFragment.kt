package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentAddVendorBinding


class AddVendorFragment : Fragment() {
    private lateinit var binding: FragmentAddVendorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddVendorBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vendorSubmitButton.setOnClickListener {
            val vendorName = binding.editTextVendorName.text.toString().trim()
            val firmName = binding.editTextFirmName.text.toString().trim()
            val address = binding.editTextAddress.text.toString().trim()
            val mobileNumber = binding.editTextMobileNumber.text.toString().trim()
            val vendorType = binding.spinnerVendorType.selectedItem.toString()
            val businessCategory = binding.editTextBusinessCategory.text.toString().trim()
            val pin = binding.editTextPIN.text.toString().trim()

            if (vendorName.isEmpty()) {
                binding.editTextVendorName.requestFocus()
                binding.editTextVendorName.error = "Enter Vendor Name."
                return@setOnClickListener
            }

            if (firmName.isEmpty()) {
                binding.editTextFirmName.requestFocus()
                binding.editTextFirmName.error = "Enter Firm Name."
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.editTextFirmName.requestFocus()
                binding.editTextFirmName.error = "Enter Address."
                return@setOnClickListener
            }

            if (mobileNumber.isEmpty()) {
                binding.editTextMobileNumber.requestFocus()
                binding.editTextMobileNumber.error = "Enter Mobile Number."
                return@setOnClickListener
            }

            if (vendorType.isEmpty()) {
                binding.spinnerVendorType.requestFocus()
                Toast.makeText(requireContext(), "Please select a vendor type", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (businessCategory.isEmpty()) {
                binding.editTextBusinessCategory.requestFocus()
                binding.editTextBusinessCategory.error = "Enter Business Category."
                return@setOnClickListener
            }
            if (pin.isEmpty()) {
                binding.inputLayoutPIN.requestFocus()
                binding.inputLayoutPIN.error = "Enter PinCode."
                return@setOnClickListener
            }
        }
    }

}