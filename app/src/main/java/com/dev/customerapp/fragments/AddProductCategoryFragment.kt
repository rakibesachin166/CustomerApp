package com.dev.customerapp.fragments

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient

import com.dev.customerapp.databinding.FragmentAddProductCategoryBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.Child1CategoryModel
import com.dev.customerapp.models.Child2CategoryModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.TopCategoryModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.PhotoResponse
import com.dev.customerapp.utils.FunctionsConstant
import com.dev.customerapp.utils.loadImage
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddProductCategoryFragment : BaseFragment() {
    private lateinit var binding: FragmentAddProductCategoryBinding

    private lateinit var topCategoryList: MutableList<TopCategoryModel>
    private lateinit var child1CategoryList: MutableList<Child1CategoryModel>
    private var categoryPhotoUri: Uri? = null

    private val pickMedia: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->

            if (uri != null) {
                categoryPhotoUri = uri
                binding.categoryImageView.loadImage(categoryPhotoUri!!)
                binding.suggester.visibility = View.GONE
            }
        }

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
        binding = FragmentAddProductCategoryBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topCategoryList = mutableListOf()

        child1CategoryList = mutableListOf()

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
                        binding.productImageCard.visibility = View.GONE
                        binding.productProfitCard.visibility = View.GONE
                        setTittle("Add Child1 Category")
                        getChild1CategoryList(topCategoryList[position].topCategoryId)
                    } else {

                        binding.productImageCard.visibility = View.VISIBLE
                        binding.productProfitCard.visibility = View.VISIBLE
                        setTittle("Add Top Category")
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

                    if (position != 0) {
                        setTittle("Add Child 2 Category")
                    } else {
                        setTittle("Add Child 1 Category")
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        binding.uploadCategoryImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        }

        binding.submitButton.setOnClickListener {

            if (binding.inputTextCategory.text.isNullOrEmpty()) {
                requireContext().showErrorToast("Please Enter Category Name")
                return@setOnClickListener
            }

            val topCategoryPosition = binding.topCategorySpinner.selectedItemPosition

            if (topCategoryPosition < 1) {
                //Add Top Category
                if (categoryPhotoUri == null) {
                    requireContext().showErrorToast("Please select product image")
                    return@setOnClickListener
                }

                val stateProfit = binding.stateProfit.text.toString()
                val divisionProfit = binding.divisionProfit.text.toString()
                val districtProfit = binding.districtProfit.text.toString()
                val blockProfit = binding.blockProfit.text.toString()

                if (stateProfit.isEmpty() || divisionProfit.isEmpty() || districtProfit.isEmpty() || blockProfit.isEmpty()) {
                    requireContext().showErrorToast("Please Enter Profit Percent")
                    return@setOnClickListener
                }

                uploadImages {
                    if (it != null) {
                        addCategory(
                            binding.inputTextCategory.text.toString(),
                            null, it,
                            null,
                            1,
                            stateProfit.toInt(),
                            divisionProfit.toInt(),
                            districtProfit.toInt(),
                            blockProfit.toInt(),
                        )

                    }
                }
                return@setOnClickListener
            }
            val child1Position = binding.child1CategorySpinner.selectedItemPosition

            if (child1Position < 1) {

                addCategory(
                    binding.inputTextCategory.text.toString(),
                    topCategoryList[topCategoryPosition].topCategoryId, null, null, 2
                )
                return@setOnClickListener
            }


            //add  Child 2
            addCategory(
                binding.inputTextCategory.text.toString(),
                topCategoryList[topCategoryPosition].topCategoryId, null,
                child1CategoryList[child1Position].child1CategoryId,
                3
            )


        }

        getTopCategoryList()
    }

    private fun uploadImages(callback: (String?) -> Unit) {
        showProgressDialog(true, "Uploading Image")

        val photoImage = FunctionsConstant.getRealPathFromURI(categoryPhotoUri!!, requireActivity())
        val photoFile = File(photoImage!!)

        val photoRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), photoFile)

        val photoPart =
            MultipartBody.Part.createFormData("categoryPhoto", photoFile.name, photoRequestBody)

        val apiService = ApiClient.getRetrofitInstance()
        apiService.uploadCategoryImage(
            photoPart,
        )
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
                            callback(responseBody.data)
                        } else {
                            requireContext().showErrorToast(responseBody!!.message)
                            callback(null)
                        }
                    } else {
                        requireContext().showErrorToast("Error While Getting State List ")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommonResponse<String>>, t: Throwable) {
                    showProgressDialog(false)
                    requireContext().showErrorToast(t.message.toString())
                    callback(null)
                }

            })

    }

    private fun addCategory(
        categoryName: String,
        topCategoryId: Int?,
        categoryPhoto: String?,
        child1CategoryId: Int?,
        categoryType: Int,
        stateProfit: Int = 0,
        divisionProfit: Int = 0,
        districtProfit: Int = 0,
        blockProfit: Int = 0,
    ) {
        showProgressDialog(true, "Saving Data")
        ApiClient.getRetrofitInstance()
            .addCategory(
                categoryName,
                topCategoryId,
                categoryPhoto,
                child1CategoryId,
                categoryType,
                stateProfit,
                divisionProfit,
                districtProfit,
                blockProfit
            )
            .enqueue(object : Callback<CommonResponse<String>> {
                override fun onResponse(
                    call: Call<CommonResponse<String>>,
                    response: Response<CommonResponse<String>>
                ) {
                    showProgressDialog(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.code == 200) {
                            binding.inputTextCategory.setText("")
                            requireContext().showSuccessToast(responseBody.message)
                            clearSpinner(binding.topCategorySpinner)
                            clearSpinner(binding.child1CategorySpinner)
                            categoryPhotoUri = null
                            binding.categoryImageView.setImageResource(0)
                            getTopCategoryList()

                        } else {
                            requireContext().showErrorToast(responseBody.message)
                        }

                    } else {
                        requireContext().showErrorToast("Failed To Add Category")
                    }
                }

                override fun onFailure(call: Call<CommonResponse<String>>, t: Throwable) {
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

    private fun setTittle(title: String) {
        binding.addCategoryTittle.hint = title
        binding.submitButton.text = title
    }
}