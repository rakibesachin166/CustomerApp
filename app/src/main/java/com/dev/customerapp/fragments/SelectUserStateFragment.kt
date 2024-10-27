package com.dev.customerapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels

import com.dev.customerapp.R;
import com.dev.customerapp.databinding.FragmentSelectUserStateBinding
import com.dev.customerapp.models.BlockModel
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StateModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserPostingModel
import com.dev.customerapp.models.UserTypes
import com.dev.customerapp.utils.showToast
import com.dev.customerapp.viewModels.CreateUserViewModel

public class SelectUserStateFragment : Fragment() {

    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectUserStateBinding
    private lateinit var stateList: List<StatePostingDataModel>
    private lateinit var divisionList: List<DivisionalPostingDataModel>
    private lateinit var districtList: List<DistrictPostingDataModel>
    private lateinit var blockList: List<BlockPostingDataModel>


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

            }

            UserTypes.DIVISIONAL_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.GONE
                binding.blockCard.visibility = View.GONE
            }

            UserTypes.DISTRICT_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.VISIBLE
                binding.blockCard.visibility = View.GONE
            }

            UserTypes.BLOCK_OFFICER -> {
                binding.stateCard.visibility = View.VISIBLE
                binding.divisionCard.visibility = View.VISIBLE
                binding.districtCard.visibility = View.VISIBLE
                binding.blockCard.visibility = View.VISIBLE
            }

            else -> {
                throw IllegalStateException("Unknown user type")
            }
        }


        binding.submitButton.setOnClickListener {


//            when (sharedViewModel.userType.value) {
//                UserTypes.STATE_OFFICER -> {
//
//                    sharedViewModel.setUserPostingModel(
//                        UserPostingModel(
//                            stateList[binding.stateSpinner.selectedItemPosition],
//                            null,
//                            null,
//                            null
//                        )
//                    )
//
//                }
//
//                UserTypes.DIVISIONAL_OFFICER -> {
//                    sharedViewModel.setUserPostingModel(
//                        UserPostingModel(
//                            stateList[binding.stateSpinner.selectedItemPosition],
//                            divisionList[binding.divisionSpinner.selectedItemPosition],
//                            null,
//                            null
//                        )
//                    )
//
//                }
//
//                UserTypes.DISTRICT_OFFICER -> {
//                    sharedViewModel.setUserPostingModel(
//                        UserPostingModel(
//                            stateList[binding.stateSpinner.selectedItemPosition],
//                            divisionList[binding.divisionSpinner.selectedItemPosition],
//                            districtList[binding.districtSpinner.selectedItemPosition],
//                            null
//                        )
//                    )
//                }
//
//                UserTypes.BLOCK_OFFICER -> {
//                    UserPostingModel(
//                        stateList[binding.stateSpinner.selectedItemPosition],
//                        divisionList[binding.divisionSpinner.selectedItemPosition],
//                        districtList[binding.districtSpinner.selectedItemPosition],
//                        blockList[binding.blockSpinner.selectedItemPosition]
//                    )
//                }
//
//                else -> {
//                    throw IllegalStateException("Unknown user type")
//                }
//            }
            sharedViewModel.setCurrentPage(2)
        }
    }

}