package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.adapter.TopCategoryAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentCategoriesBinding
import com.dev.customerapp.models.TopCategoryModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoriesFragment : BaseFragment() {
    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNestedCategories()
    }

    private fun getNestedCategories() {
        showProgressDialog(true)

        ApiClient.getRetrofitInstance().getNestedCategories().enqueue(object : Callback<CommonResponse<List<TopCategoryModel>>>{
            override fun onResponse(
                call: Call<CommonResponse<List<TopCategoryModel>>>,
                response: Response<CommonResponse<List<TopCategoryModel>>>
            ) {

                showProgressDialog(false)
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    if(responseBody!=null) {
                        if (responseBody.code == 200){
                            if(responseBody.data.isNotEmpty()) {
                            binding.topCategoryRecyclerView.adapter = TopCategoryAdapter(responseBody.data)
                            }else{
                                requireContext().showErrorToast("No Category Available")
                            }
                        }
                        else{
                            requireContext().showErrorToast(responseBody.message)
                        }


                    }else{
                        requireContext().showErrorToast("No Response From Server . Response Please try again")
                    }
                }else {
                    requireContext().showErrorToast("Error While getting Response Please try again")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<TopCategoryModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }

        })
    }

}