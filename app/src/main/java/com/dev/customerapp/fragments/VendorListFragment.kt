package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.adapter.VendorsAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentVendorListBinding
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VendorListFragment : BaseFragment() {
    
    
    private lateinit var binding: FragmentVendorListBinding
    private lateinit var vendorAdapter: VendorsAdapter
    private lateinit var vendorList: MutableList<VendorModel>


    private var role: Int = 0
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVendorListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginType = Constant(requireContext()).getLoginType()
        when (loginType) {
             1 -> {
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
        ApiClient.getRetrofitInstance().getVendorsList(userId ,role).enqueue(object :
            Callback<CommonResponse<List<VendorModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<VendorModel>>>,
                response: Response<CommonResponse<List<VendorModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        vendorList = responseBody.data.toMutableList()
                        vendorAdapter = VendorsAdapter(
                            vendorList
                        )
                        binding.vendorsRecyclerView.adapter = vendorAdapter
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<VendorModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }
}