package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentEmployeeDetailsBinding
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Callback


class EmployeeDetailsFragment(private val employeeId: Int) : BaseFragment() {

    private lateinit var binding: FragmentEmployeeDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmployeeDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getEmployeeDetails()
    }

    private fun getEmployeeDetails() {

        showProgressDialog(true)

        ApiClient.getRetrofitInstance().getEmployeeProfile(employeeId).enqueue(object :
            Callback<CommonResponse<EmployeeModel>> {
            override fun onResponse(
                call: retrofit2.Call<CommonResponse<EmployeeModel>>,
                response: retrofit2.Response<CommonResponse<EmployeeModel>>
            ) {
                showProgressDialog(false)

                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    if (userDetailsResponse.code == 200) {

                        setEmployeeData(userDetailsResponse.data)

                    } else {
                        requireContext().showErrorToast(userDetailsResponse.message)
                    }

                } else {
                    requireContext().showErrorToast("No Response From Server Please Try Again")
                }

            }

            override fun onFailure(
                call: retrofit2.Call<CommonResponse<EmployeeModel>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })

    }

    private fun setEmployeeData(employeeModel: EmployeeModel) {

        binding.employeeNameEditText.setText(employeeModel.employeeName)
        binding.employeeEditTextDOB.setText(employeeModel.employeeDob)
        binding.employeeEditTextMobile.setText(employeeModel.employeeMobileNo)
        binding.employeeEditTextEmail.setText(employeeModel.employeeEmail)
        binding.employeeEditTextAddress.setText(employeeModel.employeeAddress)
        binding.employeeEditTextHouseNo.setText(employeeModel.employeeHouseNo)

        binding.employeeEditTextLocality.setText(employeeModel.employeeLocallity)
        binding.employeeEditTextDistrict.setText(employeeModel.employeeDistrict)
        binding.employeeEditTextBlock.setText(employeeModel.employeeBlock)
        binding.employeeEditTextPinCode.setText(employeeModel.employeePincode)

    }
}
