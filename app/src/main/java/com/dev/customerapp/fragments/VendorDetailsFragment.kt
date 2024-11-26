package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentVendorDetailsBinding
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Callback


class VendorDetailsFragment(private val vendorId  :Int) : BaseFragment() {

    private lateinit var binding: FragmentVendorDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVendorDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        getVendorDetails()
    }

    private fun getVendorDetails(){
        
        showProgressDialog(true)

        ApiClient.getRetrofitInstance().getVendorProfile(vendorId).enqueue(object :
            Callback<CommonResponse<VendorModel>> {
            override fun onResponse(
                call: retrofit2.Call<CommonResponse<VendorModel>>,
                response: retrofit2.Response<CommonResponse<VendorModel>>
            ) {
                showProgressDialog(false)

                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    if (userDetailsResponse.code == 200) {

                        setVendorData(userDetailsResponse.data)

                    } else {
                        requireContext().showErrorToast(userDetailsResponse.message)
                    }

                } else {
                    requireContext().showErrorToast("No Response From Server Please Try Again")
                }

            }

            override fun onFailure(
                call: retrofit2.Call<CommonResponse<VendorModel>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
        
    }

    private fun setVendorData(vendorModel: VendorModel)
    {

        binding.editTextVendorName.setText(vendorModel.vendorName)
        binding.editTextFirmName.setText(vendorModel.vendorFirm)
        binding.editTextAddress.setText(vendorModel.vendorAddress)
        binding.editTextMobileNumber.setText(vendorModel.vendorMobileNo)
        binding.editTextBusinessCategory.setText(vendorModel.vendorBusinessCategory)
        binding.editTextPIN.setText(vendorModel.vendorPinCode)


    }
}