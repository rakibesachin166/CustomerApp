package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.SetEmployeeStatus
import com.dev.customerapp.adapter.EmployeeStatusManageAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentEmployeeStatusBinding
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmployeeStatusFragment : BaseFragment(), SetEmployeeStatus {
    private lateinit var binding: FragmentEmployeeStatusBinding
    private lateinit var employeeStatusManageAdapter: EmployeeStatusManageAdapter
    private lateinit var employeeList: MutableList<EmployeeModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeStatusBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getEmployeeWithPendingStatus()
    }

    private fun getEmployeeWithPendingStatus() {
        showProgressDialog(true)
        ApiClient.getRetrofitInstance().getEmployeeWithPendingStatus().enqueue(object :
            Callback<CommonResponse<List<EmployeeModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<EmployeeModel>>>,
                response: Response<CommonResponse<List<EmployeeModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        employeeList = responseBody.data.toMutableList()
                        employeeStatusManageAdapter = EmployeeStatusManageAdapter(
                            employeeList,
                            this@EmployeeStatusFragment
                        )
                        binding.employeeStatusRecyclerView.adapter = employeeStatusManageAdapter
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<EmployeeModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    override fun setEmployeeStatus(employeeModel: EmployeeModel, status: Int, position: Int) {
        showProgressDialog(true)
        ApiClient.getRetrofitInstance().setEmployeeStatus(employeeModel.employeeId!!, status)
            .enqueue(object :
                Callback<CommonResponse<String>> {
                override fun onResponse(
                    call: Call<CommonResponse<String>>,
                    response: Response<CommonResponse<String>>
                ) {
                    showProgressDialog(false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.code == 200) {
                            employeeList.removeAt(position)
                            employeeStatusManageAdapter.notifyItemRemoved(position)
                        } else {
                            requireContext().showErrorToast(responseBody!!.message)
                        }
                    } else {
                        requireContext().showErrorToast("Error While Getting State List ")
                    }
                }

                override fun onFailure(
                    call: Call<CommonResponse<String>>,
                    t: Throwable
                ) {
                    showProgressDialog(false)
                    requireContext().showErrorToast(t.message.toString())
                }
            })
    }
}