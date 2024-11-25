package com.dev.customerapp.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.dev.customerapp.R
import com.dev.customerapp.activity.MainActivity
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.api.ApiService
import com.dev.customerapp.databinding.FragmentAddVendorBinding
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.ResponseHandler
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import com.dev.customerapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddVendorFragment : Fragment() {
    private lateinit var binding: FragmentAddVendorBinding
    private lateinit var apiService: ApiService
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddVendorBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        apiService = ApiClient.getRetrofitInstance()

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

            if (mobileNumber.isEmpty() || !mobileNumber.matches(Regex("\\d{10}"))) {
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
            if (pin.isEmpty() || !pin.matches(Regex("\\d{6}"))) {
                binding.inputLayoutPIN.requestFocus()
                binding.inputLayoutPIN.error = "Enter PinCode."
                return@setOnClickListener
            }
          val loginUser =   Constant(requireContext()).getUserData()

            val vendorRole = if (loginUser?.userType == 1) 2 else 1

            val vendor = VendorModel(
                vendorName,
                firmName,
                address,
                mobileNumber,
                vendorType,
                vendorRole,
                businessCategory,
                loginUser?.userId.toString(),
                ""
            )
            showProgressDialog(true)
            val call: Call<ResponseHandler<List<VendorModel>>> = apiService.addVendor(vendor)
            call.enqueue(object : Callback<ResponseHandler<List<VendorModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<VendorModel>>>,
                    response: Response<ResponseHandler<List<VendorModel>>>
                ) {
                    showProgressDialog(false)
                    if (response.isSuccessful && response.body() != null) {
                        val responseHandler = response.body()
                        val code = responseHandler?.code
                        val message = responseHandler?.message
                        if (code == 200) {
                            requireContext().showSuccessToast(message.toString())
                            requireActivity().onBackPressed()
                        }
                        if (code == 201) {
                            requireContext().showErrorToast(message.toString())
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<VendorModel>>>,
                    t: Throwable
                ) {
                    showProgressDialog(false)
                    requireContext().showErrorToast(t.message.toString())
                }

            })

        }
    }

    private fun showProgressDialog(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = Dialog(requireContext())
                progressDialog!!.setContentView(R.layout.dialog_progress_bar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setCanceledOnTouchOutside(false)
            }
            if (!isFinishing()) {
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing) {
                if (!isFinishing()) {
                    progressDialog!!.dismiss()
                }
            }
        }
    }

    private fun isFinishing(): Boolean {
        return activity?.isFinishing == true
    }

}