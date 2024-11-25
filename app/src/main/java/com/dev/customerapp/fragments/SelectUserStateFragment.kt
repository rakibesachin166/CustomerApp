package com.dev.customerapp.fragments;

import android.R
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentSelectUserStateBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserPostingModel
import com.dev.customerapp.models.UserTypes
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast

import com.dev.customerapp.viewModels.CreateUserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class SelectUserStateFragment : Fragment() {

    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectUserStateBinding
    private lateinit var stateList: MutableList<StatePostingDataModel>
    private lateinit var divisionList: MutableList<DivisionalPostingDataModel>
    private lateinit var districtList: MutableList<DistrictPostingDataModel>
    private lateinit var blockList: MutableList<BlockPostingDataModel>
    private var progressDialog: ProgressDialog? = null


    fun clearSpinner(spinner: Spinner)
    {
        val emptyList = listOf<String>()
        val adapter = ArrayAdapter(binding.root.context, R.layout.simple_spinner_item, emptyList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectUserStateBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (sharedViewModel.userType.value) {
            UserTypes.STATE_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.GONE
                binding.districtCard.visibility = View.GONE
                binding.blockCard.visibility = View.GONE
                getStateList()
            }

            UserTypes.DIVISIONAL_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.GONE
                binding.blockCard.visibility = View.GONE
                getStateList()

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
                                getDivisionList(stateList[position].stateId)
                            }


                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }

            UserTypes.DISTRICT_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.VISIBLE
                binding.blockCard.visibility = View.GONE
                getStateList()
                binding.stateSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            divisionList.clear()
                            districtList.clear()
                            clearSpinner(binding.divisionSpinner)
                            clearSpinner(binding.districtSpinner)
                            if (position != 0) {
                                getDivisionList(stateList[position].stateId)
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
                                getDistrictList(divisionList[position].divisionId)
                            }


                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }

            UserTypes.BLOCK_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.VISIBLE
                binding.blockCard.visibility = View.VISIBLE
                getStateList()
                binding.stateSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            divisionList.clear()
                            districtList.clear()
                            blockList.clear()
                            clearSpinner(binding.divisionSpinner)
                            clearSpinner(binding.districtSpinner)
                            clearSpinner(binding.blockSpinner)
                            if (position != 0) {
                                getDivisionList(stateList[position].stateId)
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
                            blockList.clear()
                            clearSpinner(binding.districtSpinner)
                            clearSpinner(binding.blockSpinner)
                            if (position != 0) {
                                getDistrictList(divisionList[position].divisionId)
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
                            clearSpinner(binding.blockSpinner)
                            if (position != 0) {
                                getBlockList(districtList[position].districtId)
                            }


                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }
            else -> {
                throw IllegalStateException("Unknown user type")
            }
        }


        binding.submitButton.setOnClickListener {

            when (sharedViewModel.userType.value) {
                UserTypes.STATE_OFFICER -> {

                    if (binding.stateSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select state")
                        return@setOnClickListener
                    }
                    sharedViewModel.setUserPostingModel(
                        UserPostingModel(
                            stateList[binding.stateSpinner.selectedItemPosition],
                            null,
                            null,
                            null
                        )
                    )

                }

                UserTypes.DIVISIONAL_OFFICER -> {
                    if (binding.stateSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select state")
                        return@setOnClickListener
                    }

                    if (binding.divisionSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select state")
                        return@setOnClickListener
                    }

                    sharedViewModel.setUserPostingModel(
                        UserPostingModel(
                            stateList[binding.stateSpinner.selectedItemPosition],
                            divisionList[binding.divisionSpinner.selectedItemPosition],
                            null,
                            null
                        )
                    )

                }

                UserTypes.DISTRICT_OFFICER -> {
                    if (binding.stateSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select state")
                        return@setOnClickListener
                    }

                    if (binding.divisionSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select Division")
                        return@setOnClickListener
                    }
                    if (binding.districtSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select District")
                        return@setOnClickListener
                    }

                    sharedViewModel.setUserPostingModel(
                        UserPostingModel(
                            stateList[binding.stateSpinner.selectedItemPosition],
                            divisionList[binding.divisionSpinner.selectedItemPosition],
                            districtList[binding.districtSpinner.selectedItemPosition],
                            null
                        )
                    )
                }

                UserTypes.BLOCK_OFFICER -> {

                    if (binding.stateSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select state")
                        return@setOnClickListener
                    }

                    if (binding.divisionSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select Division")
                        return@setOnClickListener
                    }
                    if (binding.districtSpinner.selectedItemPosition < 1) {
                        requireContext().showErrorToast("Please select District")
                        return@setOnClickListener
                    }

                    if (binding.blockSpinner.selectedItemPosition < 1)
                    {
                        requireContext().showErrorToast("Please select Block")
                        return@setOnClickListener
                    }
                    sharedViewModel.setUserPostingModel(
                        UserPostingModel(
                            stateList[binding.stateSpinner.selectedItemPosition],
                            divisionList[binding.divisionSpinner.selectedItemPosition],
                            districtList[binding.districtSpinner.selectedItemPosition],
                            blockList[binding.blockSpinner.selectedItemPosition]
                        )
                    )

                }

                else -> {
                    throw IllegalStateException("Unknown user type")
                }
            }

            binding.stateSpinner.onItemSelectedListener = null
            sharedViewModel.setCurrentPage(2)
        }


        stateList = mutableListOf()
        divisionList = mutableListOf()
        districtList = mutableListOf()
        blockList = mutableListOf()


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
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
                        districtList.add(DistrictPostingDataModel(0, "--Select District--",0))
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
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
                        blockList.add(BlockPostingDataModel(0, "--Select Block--",0))
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
        val stateNames = blockList.map { it.blockName }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.blockSpinner.adapter = adapter
    }


}