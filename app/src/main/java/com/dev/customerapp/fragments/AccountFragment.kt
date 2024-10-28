package com.dev.customerapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

import com.dev.customerapp.R
import com.dev.customerapp.activity.ChangeActivity
import com.dev.customerapp.activity.MainActivity
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentAccountBinding
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.loadImage
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("MissingInflatedId")
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.idProofTextView.setOnClickListener {
            val intent = Intent(requireContext(), ChangeActivity::class.java)
            intent.putExtra("fragment_type", "idProof")
            startActivity(intent)
        }


        binding.logoutTextView.setOnClickListener {
            bottomSheet.show()
        }
        val userData = Constant(requireActivity()).getUserData()

        if (
            userData == null
        ) {
            binding.logoutTextView.visibility = View.GONE
        } else {
            binding.tvSignIn.text = userData.userName
            binding.profileCircleImageView.loadImage(ApiClient.BASE_URL + userData.userPhoto)
            when (userData.userType) {
                1 -> {
                    binding.tvUserType.text = "Admin"
                }

                2 -> {
                    binding.tvUserType.text = "State Officer"
                }

                3 -> {
                    binding.tvUserType.text = "Divisional Officer"
                }

                4 -> {
                    binding.tvUserType.text = "District Officer"
                }

                5 -> {
                    binding.tvUserType.text = "Block Officer"
                }

                else -> {

                    println("Unknown user type")
                }
            }

        }
    }


    private val bottomSheet: BottomSheetDialog by lazy {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_logout, null)
        val logoutButton = view.findViewById<AppCompatButton>(R.id.logoutButton)
        val cancelButton = view.findViewById<AppCompatButton>(R.id.cancelButton)


        cancelButton.setOnClickListener {

            bottomSheetDialog.dismiss()
        }


        logoutButton.setOnClickListener {
            Constant(requireActivity()).clearUserData()
            bottomSheetDialog.dismiss()
            requireActivity().changeActivity(MainActivity::class.java, false)
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog
    }
}
