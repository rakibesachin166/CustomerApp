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
import com.dev.customerapp.activity.UserDetailsActivity
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginType = Constant(requireContext()).getLoginType()
        when (loginType) {
            0 -> {
                setNoUserLogin()
            }

            1 -> {
                val userData = Constant(requireContext()).getUserData()!!
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

                binding.tvUserType.text =
                    "$userTypeText ${getUserFullId(userData.userType, userData.userId)}"

                binding.linearLayout.setOnClickListener {
                    val intent = Intent(requireActivity(), UserDetailsActivity::class.java)
                    intent.putExtra("userId", userData.userId)
                    startActivity(intent)
                }
                binding.idProofTextView.visibility = View.VISIBLE

                binding.idProofTextView.setOnClickListener {
                    val intent = Intent(requireContext(), FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "idProof")
                    startActivity(intent)
                }

                binding.userProfile.setOnClickListener {
                    val intent = Intent(requireActivity(), UserDetailsActivity::class.java)
                    intent.putExtra("userId", userData.userId)
                    startActivity(intent)
                }

            }

            2 -> {
                val employeeData = Constant(requireContext()).getEmployeeData()
                binding.tvSignIn.text = employeeData.employeeName
                binding.profileCircleImageView.visibility = View.GONE
                binding.tvUserType.text = "Employee"
                binding.userProfile.setOnClickListener {
                    val intent = Intent(binding.root.context, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "employeeDetails")
                    intent.putExtra("employeeId", employeeData.employeeId)
                    startActivity(intent)
                }

            }

            3 -> {
                val vendorData = Constant(requireContext()).getVendorData()
                binding.tvSignIn.text = vendorData.vendorName
                binding.profileCircleImageView.visibility = View.GONE
                binding.tvUserType.text = "Vendor"
                val intent = Intent(binding.root.context, FragmentActivity::class.java)
                intent.putExtra("fragment_type", "vendorDetails")
                intent.putExtra("vendorId", vendorData.vendorId)
                startActivity(intent)

            }

            4 -> {
                val customerData = Constant(requireContext()).getCustomerData()
                binding.tvSignIn.text = customerData.customerName
                binding.profileCircleImageView.visibility = View.GONE
                binding.tvUserType.text = "Customer"
                binding.userProfile.setOnClickListener {
                    val intent = Intent(binding.root.context, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "customerDetails")
                    intent.putExtra("customerId", customerData.customerId)
                    startActivity(intent)
                }


            }

        }

        binding.logoutTextView.setOnClickListener {
            bottomSheet.show()
        }
    }

    private fun setNoUserLogin() {
        binding.tvSignIn.setOnClickListener({
            requireContext().changeActivity(LoginActivity::class.java, true)
        })
        binding.idProofTextView.visibility = View.GONE
        binding.userProfile.visibility = View.GONE
        binding.logoutTextView.visibility = View.VISIBLE
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
