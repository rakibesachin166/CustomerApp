package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.R
import com.dev.customerapp.adapter.VendorListForPurchaseProductAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentVendorListForPurchaseProductBinding
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field


class VendorListForPurchaseProductFragment(
    private val topCategoryId: Int,
    private val child1CategoryId: Int?,
    private val child2CategoryId: Int?
) : BaseFragment() {

    private lateinit var binding: FragmentVendorListForPurchaseProductBinding

    private lateinit var vendorList: MutableList<VendorModel>
    private lateinit var vendorListForPurchaseProductAdapter: VendorListForPurchaseProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVendorListForPurchaseProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getVendorListForPurchaseProduct()

    }

    private fun getVendorListForPurchaseProduct() {

        showProgressDialog(true)
        ApiClient.getRetrofitInstance().getVendorListForPurchaseProductFragment(
            topCategoryId,
            child1CategoryId,
            child2CategoryId
        ).enqueue(object :
            Callback<CommonResponse<List<VendorModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<VendorModel>>>,
                response: Response<CommonResponse<List<VendorModel>>>
            ) {
                showProgressDialog(false)
                val responseHandler = response.body()
                if (response.isSuccessful && responseHandler != null) {

                    if (responseHandler.code == 200) {
                        vendorList = responseHandler.data.toMutableList()
                        vendorListForPurchaseProductAdapter =
                            VendorListForPurchaseProductAdapter(vendorList)
                        binding.recyclerView.adapter = vendorListForPurchaseProductAdapter
                    } else {
                        requireContext().showErrorToast(responseHandler.message)
                    }
                } else {
                    requireContext().showErrorToast("Error fetching vendor list")
                }
            }

            override fun onFailure(call: Call<CommonResponse<List<VendorModel>>>, t: Throwable) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }

        })
    }

}