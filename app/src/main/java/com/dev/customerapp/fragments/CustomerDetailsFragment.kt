package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentCustomerDetailsBinding
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Callback


class CustomerDetailsFragment(private val customerId: Int) : BaseFragment() {

    private lateinit var binding: FragmentCustomerDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCustomerDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCustomerDetails()
    }

    private fun getCustomerDetails() {

        showProgressDialog(true)

        ApiClient.getRetrofitInstance().getCustomerProfile(customerId).enqueue(object :
            Callback<CommonResponse<CustomerModel>> {
            override fun onResponse(
                call: retrofit2.Call<CommonResponse<CustomerModel>>,
                response: retrofit2.Response<CommonResponse<CustomerModel>>
            ) {
                showProgressDialog(false)

                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    if (userDetailsResponse.code == 200) {

                        setCustomerData(userDetailsResponse.data)

                    } else {
                        requireContext().showErrorToast(userDetailsResponse.message)
                    }

                } else {
                    requireContext().showErrorToast("No Response From Server Please Try Again")
                }

            }

            override fun onFailure(
                call: retrofit2.Call<CommonResponse<CustomerModel>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })

    }

    private fun setCustomerData(customerModel: CustomerModel) {

        binding.editTextName.setText(customerModel.customerName)
        binding.editTextDOB.setText(customerModel.customerDob)
        binding.editTextMobile.setText(customerModel.customerMobileNo)
        binding.editTextEmail.setText(customerModel.customerEmail)
        binding.editTextAddress.setText(customerModel.customerAddress)
        binding.editTextHouseNo.setText(customerModel.customerHouseNo)

        binding.editTextLocality.setText(customerModel.customerLocallity)
        binding.editTextBlock.setText("Work GOING oN")
        binding.editTextDistrict.setText("Work GOING oN")
        binding.editTextPinCode.setText(customerModel.customerPincode)

    }
}
    

   