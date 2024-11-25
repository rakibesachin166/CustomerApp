package com.dev.customerapp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dev.customerapp.adapter.UsersAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentUserListBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.printLog
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserListFragment : Fragment() {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var stateList: MutableList<StatePostingDataModel>
    private lateinit var divisionList: MutableList<DivisionalPostingDataModel>
    private lateinit var districtList: MutableList<DistrictPostingDataModel>
    private lateinit var blockList: MutableList<BlockPostingDataModel>
    private lateinit var userdata: UserDataModel
    fun clearSpinner(spinner: Spinner) {
        val emptyList = listOf<String>()
        val adapter =
            ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, emptyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private var progressDialog: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateList = mutableListOf()
        divisionList = mutableListOf()
        districtList = mutableListOf()
        blockList = mutableListOf()
        userdata = Constant(requireContext()).getUserData()!!
        binding.stateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                    printLog("pointer", "1")
                    divisionList.clear()
                    clearSpinner(binding.divisionSpinner)
                    if (position > -1) {
                        printLog("pointer", "2")
                        printLog("pointer", stateList[position].stateId.toString())
                        if (stateList[position].stateId > 0) {
                            printLog("pointer", "3")

                            getDivisionList(stateList[position].stateId)
                        }
                        printLog("pointer", "4")

                    }

                    printLog("pointer", "5")

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


                    if (position > -1) {

                        if (divisionList[position].divisionId > 0) {

                            getDistrictList(divisionList[position].divisionId)
                        }

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


                    if (position > -1) {
                        if (districtList[position].districtId > 0) {

                            getBlockList(districtList[position].districtId)
                        }
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.blockSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        getStateList()


        binding.filterButton.setOnClickListener {

            val statePosition = binding.stateSpinner.selectedItemPosition

            if (statePosition < 0 || stateList[statePosition].stateId == 0) {
                requireContext().showErrorToast("Please Select State")
                return@setOnClickListener
            }

            val divisionPosition = binding.divisionSpinner.selectedItemPosition

            if (divisionPosition < 0 || divisionList[divisionPosition].divisionId == 0) {
                //Add Division
                getUserOfLocation(
                    1, stateList[statePosition].stateId
                )
                return@setOnClickListener
            }
            val districtPosition = binding.districtSpinner.selectedItemPosition

            if (districtPosition < 0 || districtList[districtPosition].districtId == 0) {
                //Add District
                getUserOfLocation(
                    2, divisionList[divisionPosition].divisionId
                )
                return@setOnClickListener
            }


            val blockPosition = binding.blockSpinner.selectedItemPosition

            if (blockPosition < 0 || blockList[blockPosition].blockId == 0) {
                //Add block
                getUserOfLocation(
                    3, districtList[districtPosition].districtId
                )
                return@setOnClickListener
            }
            //add Block
            getUserOfLocation(
                4, blockList[blockPosition].blockId
            )
            return@setOnClickListener

        }
    }

    private fun getUserOfLocation(locationType: Int, locationId: Int) {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Loading...")
        }
        progressDialog!!.show()

        ApiClient.getRetrofitInstance().getUserOfLocation(locationId, locationType)
            .enqueue(object : Callback<CommonResponse<List<UserDataModel>>> {
                override fun onResponse(
                    call: Call<CommonResponse<List<UserDataModel>>>,
                    response: Response<CommonResponse<List<UserDataModel>>>
                ) {
                    progressDialog!!.dismiss()
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.code == 200) {
                            binding.recyclerView.adapter = UsersAdapter(responseBody.data)
                        } else {
                            binding.recyclerView.adapter = UsersAdapter(emptyList())
                            requireContext().showErrorToast(responseBody!!.message)
                        }
                    } else {
                        binding.recyclerView.adapter = UsersAdapter(emptyList())
                        requireContext().showErrorToast("Error While Getting State List ")
                    }
                }

                override fun onFailure(
                    call: Call<CommonResponse<List<UserDataModel>>>,
                    t: Throwable
                ) {
                    progressDialog!!.dismiss()
                    requireContext().showErrorToast(t.message.toString())
                }

            })
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


        if (userdata.userType >= 2) {
            stateList = stateList.filter { it.stateId == userdata.stateId }.toMutableList()
        }

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

        if (userdata.userType >= 3) {
            divisionList =
                divisionList.filter { it.divisionId == userdata.divisionId }.toMutableList()
        }

        val divisionNames = divisionList.map { it.divisionName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisionNames)
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
                        districtList.add(DistrictPostingDataModel(0, "--Select District--", 0))
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

        if (userdata.userType >= 4) {
            districtList =
                districtList.filter { it.districtId == userdata.districtId }.toMutableList()
        }
        val districtNames = districtList.map { it.districtName }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, districtNames)
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
                        blockList.add(BlockPostingDataModel(0, "--Select Block--", 0))
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

        if (userdata.userType >= 5) {
            blockList = blockList.filter { it.blockId == userdata.blockId }.toMutableList()
        }

        val blockNames = blockList.map { it.blockName }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.blockSpinner.adapter = adapter
    }
}