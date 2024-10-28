package com.dev.customerapp.fragments

import android.app.ProgressDialog
import android.icu.text.CaseMap.Title
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
import com.dev.customerapp.databinding.FragmentAddLocationBinding
import com.dev.customerapp.databinding.FragmentSelectUserStateBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLocationFragment : Fragment() {
    private lateinit var stateList: MutableList<StatePostingDataModel>
    private lateinit var divisionList: MutableList<DivisionalPostingDataModel>
    private lateinit var districtList: MutableList<DistrictPostingDataModel>
    private lateinit var blockList: MutableList<BlockPostingDataModel>
    private lateinit var binding: FragmentAddLocationBinding
    private var progressDialog: ProgressDialog? = null
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddLocationBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateList = mutableListOf()
        divisionList = mutableListOf()
        districtList = mutableListOf()
        blockList = mutableListOf()

        binding.stateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    divisionList.clear()
                    clearSpinner(binding.divisionSpinner)
                    if (position != 0) {
                        setTittle("Add Division")
                        getDivisionList(stateList[position].stateId)
                    } else {
                        setTittle("Add State")
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.divisionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                    districtList.clear()
                    clearSpinner(binding.districtSpinner)
                    if (position != 0) {
                        setTittle("Add District")
                        getDistrictList(divisionList[position].divisionId)
                    } else {
                        setTittle("Add Division")
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.districtSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                    blockList.clear()
//                    clearSpinner(binding.blockSpinner)
                    if (position != 0) {
//                        getBlockList(districtList[position].districtId)
                        setTittle("Add Block")
                    } else {
                        setTittle("Add District")
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        getStateList()


        binding.submitButton.setOnClickListener {

            val statePosition = binding.stateSpinner.selectedItemPosition

            if (statePosition < 1) {
                //Add State
                addLocation(binding.inputTextLocation.text.toString(),1,null,null,null)
                return@setOnClickListener
            }
            val divisionPosition = binding.divisionSpinner.selectedItemPosition

            if (divisionPosition < 1) {
                //Add Division
                addLocation(binding.inputTextLocation.text.toString(),2,
                    stateList[statePosition].stateId,null,null)
                return@setOnClickListener
            }
            val districtPosition = binding.districtSpinner.selectedItemPosition

            if (districtPosition < 1) {
                //Add District
                addLocation(binding.inputTextLocation.text.toString(),3,
                    stateList[statePosition].stateId,divisionList[divisionPosition].divisionId,null)
                return@setOnClickListener
            }

            //add Block
            addLocation(binding.inputTextLocation.text.toString(),4,
                stateList[statePosition].stateId,divisionList[divisionPosition].divisionId,
                districtList[districtPosition].districtId)


        }
    }

    private fun addLocation( locationName :String , locationType :Int , stateId: Int? , divisionId: Int? , districtId: Int?  ){
        ApiClient.getRetrofitInstance().addLocation(stateId , divisionId ,districtId , locationType ,locationName).enqueue(object : Callback<CommonResponse<String>>{
            override fun onResponse(
                call: Call<CommonResponse<String>>,
                response: Response<CommonResponse<String>>
            ) {
                val responseBody = response.body()
                if (responseBody!=null){
                    if (responseBody.code ==200){
                        binding.inputTextLocation.setText("")
                        requireContext().showSuccessToast(responseBody.message)
                        clearSpinner(binding.stateSpinner)
                        clearSpinner(binding.divisionSpinner)
                        clearSpinner(binding.districtSpinner)
                        getStateList()

                    }else{
                        requireContext().showErrorToast(responseBody.message)
                    }

                }else{
                    requireContext().showErrorToast("Failed To Add Location")
                }
            }

            override fun onFailure(call: Call<CommonResponse<String>>, t: Throwable) {
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setTittle(title: String) {
        binding.addLocationTittle.hint = title
        binding.submitButton.text = title
    }

    private fun getStateList() {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Loading...")
        }
        progressDialog!!.show()
        ApiClient.getRetrofitInstance().getStateList().enqueue(object :
            Callback<CommonResponse<List<StatePostingDataModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<StatePostingDataModel>>>,
                response: Response<CommonResponse<List<StatePostingDataModel>>>
            ) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        stateList.clear()
                        stateList.add(StatePostingDataModel(0, "--Select State--"))
                        stateList.addAll(responseBody.data)
                        setStateList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<StatePostingDataModel>>>,
                t: Throwable
            ) {
                progressDialog!!.dismiss()
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setStateList() {

        val stateNames = stateList.map { it.stateName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.stateSpinner.adapter = adapter


    }


    private fun getDivisionList(stateId: Int) {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Loading...")
        }
        progressDialog!!.show()
        ApiClient.getRetrofitInstance().getDivisionList(stateId).enqueue(object :
            Callback<CommonResponse<List<DivisionalPostingDataModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<DivisionalPostingDataModel>>>,
                response: Response<CommonResponse<List<DivisionalPostingDataModel>>>
            ) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        divisionList.clear()
                        divisionList.add(DivisionalPostingDataModel(0, "--Select Division--"))
                        divisionList.addAll(responseBody.data)
                        setDivisionList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<DivisionalPostingDataModel>>>,
                t: Throwable
            ) {
                progressDialog!!.dismiss()
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setDivisionList() {
        val stateNames = divisionList.map { it.divisionName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.divisionSpinner.adapter = adapter
    }

    private fun getDistrictList(divisionId: Int) {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Loading...")
        }
        progressDialog!!.show()
        ApiClient.getRetrofitInstance().getDistrictList(divisionId).enqueue(object :
            Callback<CommonResponse<List<DistrictPostingDataModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<DistrictPostingDataModel>>>,
                response: Response<CommonResponse<List<DistrictPostingDataModel>>>
            ) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        districtList.clear()
                        districtList.add(DistrictPostingDataModel(0, "--Select District--"))
                        districtList.addAll(responseBody.data)
                        setDistrictList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<DistrictPostingDataModel>>>,
                t: Throwable
            ) {
                progressDialog!!.dismiss()
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setDistrictList() {
        val stateNames = districtList.map { it.districtName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.districtSpinner.adapter = adapter
    }

    private fun getBlockList(districtId: Int) {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Loading...")
        }
        progressDialog!!.show()
        ApiClient.getRetrofitInstance().getBlockList(districtId).enqueue(object :
            Callback<CommonResponse<List<BlockPostingDataModel>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<BlockPostingDataModel>>>,
                response: Response<CommonResponse<List<BlockPostingDataModel>>>
            ) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {
                        blockList.clear()
                        blockList.add(BlockPostingDataModel(0, "--Select Block--"))
                        blockList.addAll(responseBody.data)
                        setBlockList()
                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(
                call: Call<CommonResponse<List<BlockPostingDataModel>>>,
                t: Throwable
            ) {
                progressDialog!!.dismiss()
                requireContext().showErrorToast(t.message.toString())
            }
        })
    }

    private fun setBlockList() {
//        val stateNames = blockList.map { it.blockName }
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.blockSpinner.adapter = adapter
    }
}