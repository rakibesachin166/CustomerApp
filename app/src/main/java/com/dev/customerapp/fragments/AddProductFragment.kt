package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentAddCustomerBinding
import com.dev.customerapp.databinding.FragmentAddProudctBinding
import com.dev.customerapp.models.Child1CategoryModel
import com.dev.customerapp.models.Child2CategoryModel
import com.dev.customerapp.models.ProductModel
import com.dev.customerapp.models.TopCategoryModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddProductFragment : BaseFragment() {

    private lateinit var binding: FragmentAddProudctBinding
    private val topCategoryList: MutableList<TopCategoryModel> = mutableListOf()
    private val child1CategoryList: MutableList<Child1CategoryModel> = mutableListOf()
    private val child2CategoryList: MutableList<Child2CategoryModel> = mutableListOf()
    fun clearSpinner(spinner: Spinner) {
        val emptyList = listOf<String>()
        val adapter =
            ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, emptyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddProudctBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topCategorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    child1CategoryList.clear()
                    clearSpinner(binding.child1CategorySpinner)
                    if (position != 0) {
                        getChild1CategoryList(topCategoryList[position].topCategoryId)
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.child1CategorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    child2CategoryList.clear()
                    clearSpinner(binding.child2CategorySpinner)
                    if (position != 0) {
                        getChild2CategoryList(child1CategoryList[position].child1CategoryId)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        binding.productSubmitButton.setOnClickListener {

            val topCategoryPosition = binding.topCategorySpinner.selectedItemPosition
            val child1Position = binding.child1CategorySpinner.selectedItemPosition
            val child2Position = binding.child2CategorySpinner.selectedItemPosition

            if (topCategoryPosition <= 0) {
                requireContext().showErrorToast("Please Select a Top category")
                return@setOnClickListener
            }

            if (child1Position <= 0) {
                requireContext().showErrorToast("Please Select a Child1 category")
                return@setOnClickListener

            }

            if (child2Position <= 0) {
                requireContext().showErrorToast("Please Select a Child2 category")
                return@setOnClickListener
            }

            val name = binding.nameEdt.text.toString().trim()
            val codeSku = binding.codeSkU.text.toString().trim()
            val mrp = binding.mrpEdt.text.toString().trim()
            val salePrice = binding.salePriceEdt.text.toString().trim()
            val discount = binding.discountEdt.text.toString().trim()
            val availableQuantity = binding.availableQuantityEdt.text.toString().trim()
            val description = binding.descriptionEdt.text.toString().trim()
            if (name.isEmpty()) {
                binding.nameEdt.requestFocus()
                binding.nameEdt.error = "Enter Product Name"
                return@setOnClickListener
            }

            if (codeSku.isEmpty()) {
                binding.codeSkU.requestFocus()
                binding.codeSkU.error = "Enter Product Code Sku"
                return@setOnClickListener
            }

            if (mrp.isEmpty()) {
                binding.mrpEdt.requestFocus()
                binding.mrpEdt.error = "Enter a MRP"
                return@setOnClickListener
            }

            if (salePrice.isEmpty()
            ) {
                binding.salePriceEdt.requestFocus()
                binding.salePriceEdt.error = "Enter a Sale Price"
                return@setOnClickListener
            }

            if (discount.isEmpty()) {
                binding.discountEdt.requestFocus()
                binding.discountEdt.error = "Enter Address."
                return@setOnClickListener
            }

            if (availableQuantity.isEmpty()) {
                binding.availableQuantityEdt.requestFocus()
                binding.availableQuantityEdt.error = "Enter Available Quantity."
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                binding.descriptionEdt.requestFocus()
                binding.descriptionEdt.error = "Enter Product Description."
                return@setOnClickListener
            }

            val loginUser = Constant(requireContext()).getLoginUserIdAndLoginType()
            val productModel = ProductModel(
                0,
                name,
                topCategoryList[topCategoryPosition].topCategoryId,
                child1CategoryList[child1Position].child1CategoryId,
                child2CategoryList[child2Position].child2CategoryId,
                codeSku,
                mrp.toInt(),
                salePrice.toInt(),
                discount.toInt(),
                availableQuantity.toInt(),
                description,
                loginUser.first,
                loginUser.second,
                ""
            )

            addProduct(productModel)
        }



        getTopCategoryList()
    }

    private fun addProduct(productModel: ProductModel) {
        showProgressDialog(true, "Adding Product ...")
        ApiClient.getRetrofitInstance().createProduct(productModel).enqueue(object :
            Callback<CommonResponse<String>> {
            override fun onResponse(
                call: Call<CommonResponse<String>>,
                response: Response<CommonResponse<String>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.code == 200) {
                            requireContext().showSuccessToast(responseBody.message)
                            requireActivity().onBackPressed()
                        } else {
                            requireContext().showErrorToast(responseBody.message)
                        }

                    } else {
                        requireContext().showErrorToast("Error creating Product . Please try again")
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting Child 1 Category")
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

    private fun getTopCategoryList() {
        showProgressDialog(true)
        ApiClient.getRetrofitInstance().getTopCategoryList().enqueue(object :
            Callback<CommonResponse<List<TopCategoryModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<TopCategoryModel>>>,
                response: Response<CommonResponse<List<TopCategoryModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        topCategoryList.clear()
                        topCategoryList.add(TopCategoryModel(0, "--Select Top Category--", " "))
                        topCategoryList.addAll(responseBody.data)
                        setTopCategoryList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting Top Category List ")
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

    private fun setTopCategoryList() {

        val topCategoryNames = topCategoryList.map { it.topCategoryName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, topCategoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.topCategorySpinner.adapter = adapter


    }

    private fun getChild1CategoryList(topCategoryId: Int) {
        showProgressDialog(true, "Loading Top Category ...")
        ApiClient.getRetrofitInstance().getChild1CategoryList(topCategoryId).enqueue(object :
            Callback<CommonResponse<List<Child1CategoryModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<Child1CategoryModel>>>,
                response: Response<CommonResponse<List<Child1CategoryModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        child1CategoryList.clear()
                        child1CategoryList.add(
                            Child1CategoryModel(
                                0,
                                "--Select Child 1 Category--", 0,
                                emptyList()
                            )
                        )
                        child1CategoryList.addAll(responseBody.data)
                        setChild1CategoryList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting Child 1 Category")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<Child1CategoryModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setChild1CategoryList() {
        val child1CategoryNames = child1CategoryList.map { it.child1CategoryName }
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                child1CategoryNames
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.child1CategorySpinner.adapter = adapter
    }

    private fun getChild2CategoryList(child1CategoryId: Int) {
        showProgressDialog(true, "Loading Top Category ...")
        ApiClient.getRetrofitInstance().getChild2CategoryList(child1CategoryId).enqueue(object :
            Callback<CommonResponse<List<Child2CategoryModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<Child2CategoryModel>>>,
                response: Response<CommonResponse<List<Child2CategoryModel>>>
            ) {
                showProgressDialog(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        child2CategoryList.clear()
                        child2CategoryList.add(
                            Child2CategoryModel(
                                0,
                                "--Select Child 1 Category--", 0, 0
                            )
                        )
                        child2CategoryList.addAll(responseBody.data)
                        setChild2CategoryList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting Child 1 Category")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<Child2CategoryModel>>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setChild2CategoryList() {
        val child1CategoryNames = child2CategoryList.map { it.child2CategoryName }
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                child1CategoryNames
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.child2CategorySpinner.adapter = adapter
    }

}