package com.dev.customerapp.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.databinding.FragmentCreateUserFormBinding
import com.dev.customerapp.utils.BottomSheetUtils
import com.dev.customerapp.viewModels.CreateUserViewModel
import java.io.File

class CreateUserFormFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateUserFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.applicantPhotoTextView.setOnClickListener {
            BottomSheetUtils.showImageSelectionBottomSheet(
                requireActivity(),
                object : BottomSheetUtils.ImageSelectionCallback {
                    override fun onImageSelected(imageFile: File?) {

                    }

                    override fun onImageSelected(imageUri: Uri) {
                    }
                })
        }

        binding.continueButton.setOnClickListener {
            val applicantName = binding.applicantNameEdiText.text.toString().trim()
            val applicantDOB = binding.applicantDOBEditText.text.toString().trim()
            val applicantAadhar = binding.applicantAadharEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val state = binding.stateEditText.text.toString().trim()
            val tehsil = binding.tehsilEditText.text.toString().trim()
            val post = binding.postEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val pincode = binding.pinCodeEditText.text.toString().trim()
            val mobileNo = binding.mobileNumberEditText.text.toString().trim()
            val alternateMobileNo = binding.alternativeMobileNOEditText.text.toString().trim()
            val emailAddress = binding.emailAddressEditText.text.toString().trim()
            val selectIdProof = binding.idProofTypeSpinner.selectedItem.toString()
            val addressProof = binding.addressProofSpinner.selectedItem.toString()
            val nomineeName = binding.nomineeNameEditText.text.toString().trim()
            val nomineeAadharNo = binding.nomineeAadharEditText.text.toString().trim()
            val nomineeDOB = binding.nomineeDOBEditText.text.toString().trim()
            val relationNominee = binding.relationNomineeEditText.text.toString().trim()
            val checkBoxApplicationAddress = binding.applicationAddressCheckBox.isChecked
            val nomineeAddress = binding.nomineeAddressEditText.text.toString().trim()
            val bankName = binding.bankNameEditText.text.toString().trim()
            val branchName = binding.branchNameEdittext.text.toString().trim()
            val accountNo = binding.accountNoEditText.text.toString().trim()
            val panNo = binding.panNoEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (applicantName.isEmpty()) {
                binding.applicantNameEdiText.requestFocus()
                binding.applicantNameEdiText.error = "Enter Applicant Name."
                return@setOnClickListener
            }

            if (applicantDOB.isEmpty()) {
                binding.applicantDOBEditText.requestFocus()
                binding.applicantDOBEditText.error = "Enter Applicant DOB."
                return@setOnClickListener
            }

            if (applicantAadhar.isEmpty() || applicantAadhar.length != 12) {
                binding.applicantAadharEditText.requestFocus()
                binding.applicantAadharEditText.error = "Enter valid 12-digit Aadhar number."
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.addressEditText.requestFocus()
                binding.addressEditText.error = "Enter Address."
                return@setOnClickListener
            }

            if (state.isEmpty()) {
                binding.stateEditText.requestFocus()
                binding.stateEditText.error = "Enter State."
                return@setOnClickListener
            }

            if (tehsil.isEmpty()) {
                binding.tehsilEditText.requestFocus()
                binding.tehsilEditText.error = "Enter Tehsil."
                return@setOnClickListener
            }

            if (post.isEmpty()) {
                binding.postEditText.requestFocus()
                binding.postEditText.error = "Enter Post."
                return@setOnClickListener
            }

            if (city.isEmpty()) {
                binding.cityEditText.requestFocus()
                binding.cityEditText.error = "Enter City."
                return@setOnClickListener
            }

            if (pincode.isEmpty() || !pincode.matches(Regex("\\d{6}"))) {
                binding.pinCodeEditText.requestFocus()
                binding.pinCodeEditText.error = "Enter a valid 6-digit Pincode."
                return@setOnClickListener
            }

            if (mobileNo.isEmpty() || !mobileNo.matches(Regex("\\d{10}"))) {
                binding.mobileNumberEditText.requestFocus()
                binding.mobileNumberEditText.error = "Enter a valid 10-digit mobile number."
                return@setOnClickListener
            }

            if (alternateMobileNo.isNotEmpty() && !alternateMobileNo.matches(Regex("\\d{10}"))) {
                binding.alternativeMobileNOEditText.requestFocus()
                binding.alternativeMobileNOEditText.error =
                    "Enter a valid 10-digit alternate mobile number."
                return@setOnClickListener
            }

            if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress)
                    .matches()
            ) {
                binding.emailAddressEditText.requestFocus()
                binding.emailAddressEditText.error = "Enter a valid email address."
                return@setOnClickListener
            }

            if (selectIdProof.isEmpty()) {
                binding.idProofTypeSpinner.requestFocus()
                Toast.makeText(requireContext(), "Select a Id Proof", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (addressProof.isEmpty()) {
                binding.addressProofSpinner.requestFocus()
                Toast.makeText(requireContext(), "Select a Address Proof", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            if (nomineeName.isEmpty()) {
                binding.nomineeNameEditText.requestFocus()
                binding.nomineeNameEditText.error = "Enter Nominee Name."
                return@setOnClickListener
            }

            if (nomineeAadharNo.isEmpty() || nomineeAadharNo.length != 12) {
                binding.nomineeAadharEditText.requestFocus()
                binding.nomineeAadharEditText.error = "Enter valid 12-digit Nominee Aadhar number."
                return@setOnClickListener
            }

            if (nomineeDOB.isEmpty()) {
                binding.nomineeDOBEditText.requestFocus()
                binding.nomineeDOBEditText.error = "Enter Nominee DOB."
                return@setOnClickListener
            }

            if (relationNominee.isEmpty()) {
                binding.relationNomineeEditText.requestFocus()
                binding.relationNomineeEditText.error = "Enter Relation with Nominee."
                return@setOnClickListener
            }

            if (!checkBoxApplicationAddress && nomineeAddress.isEmpty()) {
                binding.nomineeAddressEditText.requestFocus()
                binding.nomineeAddressEditText.error = "Enter Nominee Address."
                return@setOnClickListener
            }

            if (bankName.isEmpty()) {
                binding.bankNameEditText.requestFocus()
                binding.bankNameEditText.error = "Enter Bank Name."
                return@setOnClickListener
            }

            if (branchName.isEmpty()) {
                binding.branchNameEdittext.requestFocus()
                binding.branchNameEdittext.error = "Enter Branch Name."
                return@setOnClickListener
            }

            if (accountNo.isEmpty()) {
                binding.accountNoEditText.requestFocus()
                binding.accountNoEditText.error = "Enter Account Number."
                return@setOnClickListener
            }

            if (panNo.isEmpty() || panNo.length != 10) {
                binding.panNoEditText.requestFocus()
                binding.panNoEditText.error = "Enter a valid 10-character PAN number."
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                binding.passwordEditText.requestFocus()
                binding.passwordEditText.error = "Enter a password of at least 6 characters."
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty() || confirmPassword != password) {
                binding.confirmPasswordEditText.requestFocus()
                binding.confirmPasswordEditText.error = "Passwords do not match."
                return@setOnClickListener
            }

        }


    }

}
