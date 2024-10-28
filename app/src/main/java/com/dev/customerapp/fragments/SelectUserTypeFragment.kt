package com.dev.customerapp.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentSelectUserTypeBinding
import com.dev.customerapp.models.UserTypes
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.viewModels.CreateUserViewModel


public class SelectUserTypeFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectUserTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectUserTypeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.setOnClickListener {
            sharedViewModel.setUserType(
                when (binding.userTypeSpinner.selectedItemPosition.toString()) {
                    "StateOfficer" -> UserTypes.STATE_OFFICER
                    "DivisionalOfficer" -> UserTypes.DIVISIONAL_OFFICER
                    "DistrictOfficer" -> UserTypes.DISTRICT_OFFICER
                    "BlockOfficer" -> UserTypes.BLOCK_OFFICER
                    else -> throw IllegalStateException("Unknown user type")
                }
            )
            sharedViewModel.setCurrentPage(1)
        }
        setBlockList()

    }
    private fun setBlockList() {
        var stateNames: Array<String> = resources.getStringArray(R.array.userTypeState)

        when (Constant(requireContext()).getUserData()?.userType){
            2-> {
                stateNames = resources.getStringArray(R.array.userTypeState)
            }
            3-> {
                stateNames = resources.getStringArray(R.array.userTypeDivision)
            }
            4-> {
                stateNames = resources.getStringArray(R.array.userTypeDistrict)
            }

        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userTypeSpinner.adapter = adapter
    }

}