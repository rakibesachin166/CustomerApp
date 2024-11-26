package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.R
import com.dev.customerapp.adapter.CustomersAdapter
import com.dev.customerapp.adapter.EmployeeStatusManageAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentCustomerListBinding
import com.dev.customerapp.databinding.FragmentEmployeeStatusBinding
import com.dev.customerapp.databinding.FragmentUserListBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class CustomerListFragment : BaseFragment() {


    private lateinit var binding: FragmentCustomerListBinding
    private lateinit var customersAdapter: CustomersAdapter
    private lateinit var customerList: MutableList<CustomerModel>


    private var role: Int = 0
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginType = Constant(requireContext()).getLoginType()
        when (loginType) {
            0, 1 -> {
                val loginUser = Constant(requireContext()).getUserData()
                role = if (loginUser?.userType == 1) 2 else 1
                userId = loginUser!!.userId
            }
            2 -> {
                val loginUser = Constant(requireContext()).getEmployeeData()
                role = 1
                userId = loginUser.employeeId!!

            }
            else -> {
                throw NullPointerException("Do Not Have Access To Create Vendor Without Login")
            }
        }
        getCustomers()
    }
    private fun getCustomers() {
        showProgressDialog(true)
        ApiClient.getRetrofitInstance().getCustomersList(userId ,role).enqueue(object :
            Callback<CommonResponse<List<CustomerModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<CustomerModel>>>,
                response: Response<CommonResponse<List<CustomerModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        customerList = responseBody.data.toMutableList()
                        customersAdapter = CustomersAdapter(
                            customerList
                        )
                        binding.customersRecyclerView.adapter = customersAdapter
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<CustomerModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }
}