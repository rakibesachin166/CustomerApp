package com.dev.customerapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.R
import com.dev.customerapp.activity.LoginActivity
import com.dev.customerapp.activity.MainActivity
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentAccountBinding
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.FunctionsConstant.Companion.getUserFullId
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.loadImage
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("MissingInflatedId")
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private var currentPickMode: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userData = Constant(requireContext()).getUserData()
        if (userData != null) {
            binding.tvSignIn.text = userData.userName
            binding.profileCircleImageView.loadImage(ApiClient.BASE_URL + userData.userPhoto)

            val userTypeText = when (userData.userType) {
                1 -> "Admin"
                2 -> "State Vendor President"
                3 -> "Divisional Vendor President"
                4 -> "District Vendor President"
                5 -> "Block Vendor President"
                else -> "Unknown User Type"
            }

            binding.tvUserType.text = "$userTypeText ${getUserFullId(userData.userType, userData.userId)}"


        } else {
            binding.tvSignIn.setOnClickListener({
                requireContext().changeActivity(LoginActivity::class.java, true)
            })
            binding.idProofTextView.visibility = View.GONE
            binding.logoutTextView.visibility = View.VISIBLE
        }

        binding.idProofTextView.setOnClickListener {
            val intent = Intent(requireContext(), FragmentActivity::class.java)
            intent.putExtra("fragment_type", "idProof")
            startActivity(intent)
        }


        binding.logoutTextView.setOnClickListener {
            bottomSheet.show()
        }
    }


    private val bottomSheet: BottomSheetDialog by lazy {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_logout, null)
        val logoutButton = view.findViewById<AppCompatButton>(R.id.logoutButton)
        val cancelButton = view.findViewById<AppCompatButton>(R.id.cancelButton)


        cancelButton.setOnClickListener {
            currentPickMode = 0
            bottomSheetDialog.dismiss()
        }


        logoutButton.setOnClickListener {
            Constant(requireContext()).clearUserData()
            bottomSheetDialog.dismiss()
            requireContext().changeActivity(MainActivity::class.java, false)
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog
    }



}
