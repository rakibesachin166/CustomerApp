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
import com.dev.customerapp.Activity.ChangeActivity
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentAccountBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("MissingInflatedId")
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private var currentPickMode: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

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
