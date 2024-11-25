package com.dev.customerapp.fragments

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.customerapp.R
import com.dev.customerapp.adapter.RadioButtonsAdapter
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.FragmentCreateUserFormBinding
import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.CreateUserModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.CreateUserData
import com.dev.customerapp.response.PhotoResponse
import com.dev.customerapp.utils.loadImage
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import com.dev.customerapp.utils.showToast
import com.dev.customerapp.viewModels.CreateUserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.util.Calendar


class CreateUserFormFragment : Fragment() {
    private val sharedViewModel: CreateUserViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateUserFormBinding
    private lateinit var marriedAdapter: RadioButtonsAdapter
    private var filteredDistricts : List<DistrictPostingDataModel> = emptyList()
    private lateinit var createUserData: CreateUserData
    private var currentPickMode: Int =
        0  //0-> User Photo 1-> Id Proof 2-> AAddress proof 3-> Bank Account Proof

    private var userPhotoUri: Uri? = null
    private var userIdProofUri: Uri? = null
    private var userIdProofUri2: Uri? = null
    private var userAddressProofUri: Uri? = null
    private var userAddressProofUri2: Uri? = null
    private var userBankAccountProofUri: Uri? = null
    private var signatureUri: Uri? = null

    private var progressDialog: ProgressDialog? = null
    private val pickMedia: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->

            if (uri != null) {

                when (currentPickMode) {
                    0 -> {
                        userPhotoUri = uri
                        binding.userPhotoImageView.loadImage(userPhotoUri!!)
                        binding.suggester0.visibility = View.GONE
                    }

                    1 -> {
                        userIdProofUri = uri
                        binding.uploadOriginalIdImageView.loadImage(userIdProofUri!!)
                        binding.suggester1.visibility = View.GONE

                    }
                    2 -> {
                        userIdProofUri2 = uri
                        binding.uploadOriginalIdImageView2.loadImage(userIdProofUri2!!)
                        binding.suggester2.visibility = View.GONE

                    }
                    3 -> {
                        userAddressProofUri = uri
                        binding.uploadAddressProofImageView.loadImage(userAddressProofUri!!)
                        binding.suggester3.visibility = View.GONE
                    }
                    4 -> {
                        userAddressProofUri2 = uri
                        binding.uploadAddressProofImageView2.loadImage(userAddressProofUri2!!)
                        binding.suggester4.visibility = View.GONE
                    }

                    5 -> {
                        userBankAccountProofUri = uri
                        binding.uploadBankDetailsImageView.loadImage(userBankAccountProofUri!!)
                        binding.suggester5.visibility = View.GONE
                    }

                    6 -> {
                        signatureUri = uri
                        binding.uploadSignatureIdImageView.loadImage(signatureUri!!)
                        binding.suggester6.visibility = View.GONE
                    }

                }


            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUserFormBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val bottomSheet: BottomSheetDialog by lazy {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.select_image_bottom_sheet, null)
        val cameraImageView = view.findViewById<ImageView>(R.id.camera)
        val galleryImageView = view.findViewById<ImageView>(R.id.gallery)

        galleryImageView.setOnClickListener { v: View? ->

            pickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
            bottomSheetDialog.dismiss()
        }

        cameraImageView.setOnClickListener { v: View? ->

            requireContext().showToast("Work Going to Camera")

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog
    }

    private fun showDatePickerDialog(applicantDOBEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                applicantDOBEditText.setText(selectedDate)
            }, year, month, day)
        datePickerDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         binding.idProofTypeSpinner.onItemSelectedListener
        binding.idProofTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position == 0) {
                        binding.idProofBackPhotoLy.visibility=View.VISIBLE
                    }else{
                        binding.idProofBackPhotoLy.visibility=View.GONE
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.addressProofSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position == 0) {
                        binding.addressProofBackPhotoLy.visibility=View.VISIBLE
                    }else{
                        binding.addressProofBackPhotoLy.visibility=View.GONE
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }



        binding.applicantPhotoTextView.setOnClickListener {
            currentPickMode = 0
            bottomSheet.show()
        }

        binding.uploadOriginalIdTextView.setOnClickListener {
            currentPickMode = 1
            bottomSheet.show()
        }

        binding.uploadOriginalIdTextView2.setOnClickListener {
            currentPickMode = 2
            bottomSheet.show()
        }


        binding.uploadAddressProofTextView.setOnClickListener {
            currentPickMode = 3
            bottomSheet.show()
        }
        binding.uploadAddressProofTextView2.setOnClickListener {
            currentPickMode = 4
            bottomSheet.show()
        }

        binding.uploadBankDetailsTextView.setOnClickListener {
            currentPickMode = 5
            bottomSheet.show()
        }
        binding.uploadSignatureIdTextView.setOnClickListener {
            currentPickMode = 6
            bottomSheet.show()
        }



        binding.applicantDOBEditText.setOnClickListener {
            showDatePickerDialog(binding.applicantDOBEditText)
        }
        binding.nomineeDOBEditText.setOnClickListener {
            showDatePickerDialog(binding.nomineeDOBEditText)
        }

        binding.applicationAddressCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.nomineeAddressEditTextParent.visibility = View.GONE
            } else {
                binding.nomineeAddressEditTextParent.visibility = View.VISIBLE
            }
        }

        val list = listOf("Married", "Unmarried", "Widow/Widower", "Divorced")

        binding.recyclerViewMarriedStatus.setHasFixedSize(true)
        marriedAdapter = RadioButtonsAdapter(list)
        binding.recyclerViewMarriedStatus.adapter = marriedAdapter

        binding.stateEditText.setText(sharedViewModel.userPostingModel.value!!.statePostingDataModel.stateName)

        binding.continueButton.setOnClickListener {
            val applicantName = binding.applicantNameEdiText.text.toString().trim()
            val userGender = when (binding.selectGenderRadioButton.checkedRadioButtonId) {
                R.id.maleRadioButton -> 1
                R.id.feMaleRadioButton -> 2
                R.id.transGenderRadioButton -> 3
                else -> {
                    0
                }
            }
            val maritalStatus = marriedAdapter.getSelectedPosition()
            val applicantDOB = binding.applicantDOBEditText.text.toString().trim()
            val applicantAadhar = binding.applicantAadharEditText.text.toString().trim()
            val occupation = binding.occupationSpinner.selectedItem.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val state = binding.stateEditText.text.toString().trim()
            var division = ""
            var district = ""
            var block = ""


            if (binding.divisionSpinner.selectedItem != null) {
                division = binding.divisionSpinner.selectedItem.toString()
            } else {
                requireContext().showErrorToast("Please Select Division")
                return@setOnClickListener
            }



            if (binding.districtSpinner.selectedItem != null) {
                district = binding.districtSpinner.selectedItem.toString()
            } else {
                requireContext().showErrorToast("Please Select District")
                return@setOnClickListener
            }

            if (binding.blockSpinner.selectedItem != null) {
                block = binding.blockSpinner.selectedItem.toString()
            } else {
                requireContext().showErrorToast("Please Select Block")
                return@setOnClickListener
            }


            val village = binding.villageEditText.text.toString().trim()
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
            val relationNominee = binding.relationNomineeSpinner.selectedItem.toString()
            val checkBoxApplicationAddress = binding.applicationAddressCheckBox.isChecked
            val nomineeAddress: String
            nomineeAddress = if (checkBoxApplicationAddress) {
                address
            } else {
                binding.nomineeAddressEditText.text.toString().trim()
            }

            val bankName = binding.bankNameEditText.text.toString().trim()
            val bankState = binding.bankStateSpinner.selectedItem.toString().trim()
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

            if (village.isEmpty()) {
                binding.villageEditText.requestFocus()
                binding.villageEditText.error = "Enter Village."
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

            if (userPhotoUri == null) {
                requireContext().showErrorToast("Please Select a Application Photo")
                return@setOnClickListener
            }

            if (userIdProofUri == null) {
                requireContext().showErrorToast("Please Select a Id Proof Photo")
                return@setOnClickListener
            }
            if (binding.idProofTypeSpinner.selectedItemPosition == 0) {
                if (userIdProofUri2 == null) {
                    requireContext().showErrorToast("Please Select a Id Proof Back Photo")
                    return@setOnClickListener
                }
            }



            if (userAddressProofUri == null) {
                requireContext().showErrorToast("Please Select a Address Proof Photo")
                return@setOnClickListener
            }
            if (binding.addressProofSpinner.selectedItemPosition == 0) {
                if (userAddressProofUri2 == null) {
                    requireContext().showErrorToast("Please Select a Address Proof Back Photo")
                    return@setOnClickListener
                }

            }


            if (userBankAccountProofUri == null) {
                requireContext().showErrorToast("Please Select a Bank Account Proof Photo")
                return@setOnClickListener
            }
            if (signatureUri == null) {
                requireContext().showErrorToast("Please Uplaod Signature Photo")
                return@setOnClickListener
            }

            if (!binding.checkBoxfinal.isChecked) {
                requireContext().showErrorToast("Please Accept Terms and Conditions")
                return@setOnClickListener
            }


            uploadImages { photoResponse ->
                if (photoResponse != null) {
                    val userPhoto = photoResponse.userPhoto
                    val idPhoto = photoResponse.idPhoto
                    val idPhoto2 = photoResponse.idPhoto2
                    val addressPhoto = photoResponse.addressPhoto
                    val addressPhoto2 = photoResponse.addressPhoto2
                    val bankPhoto = photoResponse.bankPhoto
                    val signaturePhoto = photoResponse.signaturePhoto



                    progressDialog = requireContext().progressDialog(message = "Saving Data ...")

                    progressDialog!!.show()
                    val apiService = ApiClient.getRetrofitInstance()
                    val userInfo = CreateUserModel(
                        sharedViewModel.userType.value!!.code,
                        sharedViewModel.userPostingModel.value!!.statePostingDataModel.stateId,
                        sharedViewModel.userPostingModel.value?.divisionalPostingDataModel?.divisionId,
                        sharedViewModel.userPostingModel.value?.districtPostingDataModel?.districtId,
                        sharedViewModel.userPostingModel.value?.blockPostingDataModel?.blockId,
                        applicantName,
                        userGender,
                        maritalStatus,
                        userPhoto,
                        applicantDOB,
                        applicantAadhar,
                        occupation,
                        address,
                        state,
                        division,
                        district,
                        block,
                        tehsil,
                        post,
                        city,
                        village,
                        pincode,
                        mobileNo,
                        alternateMobileNo,
                        emailAddress,
                        selectIdProof,
                        idPhoto,
                        idPhoto2,
                        addressProof,
                        addressPhoto,
                        addressPhoto2,
                        nomineeName,
                        nomineeAadharNo,
                        nomineeDOB,
                        relationNominee,
                        nomineeAddress,
                        bankName,
                        bankState,
                        branchName,
                        accountNo,
                        panNo,
                        bankPhoto,
                        signaturePhoto,
                        password,
                        ""
                    )

                    apiService.createUser(userInfo)
                        .enqueue(object : Callback<CommonResponse<String>> {
                            override fun onResponse(
                                call: Call<CommonResponse<String>>,
                                response: Response<CommonResponse<String>>
                            ) {
                                progressDialog!!.dismiss()
                                if (response.body()?.code == 200) {
                                    requireContext().showSuccessToast("User created successfully")
                                    requireActivity().onBackPressed()
                                } else {
                                    requireContext().showErrorToast(response.body()?.message.toString())
                                }
                            }

                            override fun onFailure(
                                call: Call<CommonResponse<String>>,
                                t: Throwable
                            ) {
                                progressDialog!!.dismiss()
                                requireContext().showErrorToast(t.message.toString())
                            }

                        })

                }
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


                    clearSpinner(binding.districtSpinner)


                    createUserData.districtList?.let {
                        setDistrictList(
                            it,
                            createUserData.divisionList?.get(position)?.divisionId ?: 0
                        )
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


                    clearSpinner(binding.blockSpinner)

                    createUserData.blockList?.let {
                        setBlockList(
                            it,
                            filteredDistricts[position].districtId
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        getCreateFromData()
    }

    private fun uploadImages(callback: (PhotoResponse?) -> Unit) {
        if (progressDialog == null) {
            progressDialog = requireContext().progressDialog(message = "Uploading Images...")
        }
        progressDialog!!.show()
        val photoImage = getRealPathFromURI(userPhotoUri!!)

        val idProofImage = getRealPathFromURI(userIdProofUri!!)


        var idProofPart2 : MultipartBody.Part?
        if (binding.idProofTypeSpinner.selectedItemPosition==0)
        {
            val idProofImage2 = getRealPathFromURI(userIdProofUri2!!)
            val idProofFile2 = File(idProofImage2!!)
            val idProofRequestBody2 = RequestBody.create("image/*".toMediaTypeOrNull(), idProofFile2)
            idProofPart2 =
                MultipartBody.Part.createFormData("idPhoto2", idProofFile2.name, idProofRequestBody2)
        }else{
            idProofPart2 = null
        }

        val addressProofImage = getRealPathFromURI(userAddressProofUri!!)

        val addressProofPart2 : MultipartBody.Part?

        if (binding.addressProofSpinner.selectedItemPosition==0)
        {
            val addressProofImage2 = getRealPathFromURI(userAddressProofUri2!!)
            val addressProofFile2 = File(addressProofImage2!!)
            val addressProofRequestBody2 =
                RequestBody.create("image/*".toMediaTypeOrNull(), addressProofFile2)
             addressProofPart2 = MultipartBody.Part.createFormData(
                "addressPhoto2",
                addressProofFile2.name,
                addressProofRequestBody2
            )
        }else{
            addressProofPart2 = null
        }

        val bankPassImage = getRealPathFromURI(userBankAccountProofUri!!)
        val signatureImage = getRealPathFromURI(signatureUri!!)

        val photoFile = File(photoImage!!)
        val idProofFile = File(idProofImage!!)



        val addressProofFile = File(addressProofImage!!)



        val bankPassFile = File(bankPassImage!!)
        val signatureImageFile = File(signatureImage!!)

        val photoRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), photoFile)

        val idProofRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), idProofFile)


        val addressProofRequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), addressProofFile)

        val bankPassRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), bankPassFile)

        val signatureBody = RequestBody.create("image/*".toMediaTypeOrNull(), signatureImageFile)


        val photoPart =
            MultipartBody.Part.createFormData("userPhoto", photoFile.name, photoRequestBody)

        val idProofPart =
            MultipartBody.Part.createFormData("idPhoto", idProofFile.name, idProofRequestBody)

        val addressProofPart = MultipartBody.Part.createFormData(
            "addressPhoto",
            addressProofFile.name,
            addressProofRequestBody
        )
        val bankPassPart =
            MultipartBody.Part.createFormData("bankPhoto", bankPassFile.name, bankPassRequestBody)

        val signaturePhotoPart =
            MultipartBody.Part.createFormData(
                "signaturePhoto",
                signatureImageFile.name,
                signatureBody
            )

        val apiService = ApiClient.getRetrofitInstance()
        apiService.uploadDocuments(
            photoPart,
            idProofPart,
            idProofPart2,
            addressProofPart,
            addressProofPart2,
            bankPassPart,
            signaturePhotoPart
        )
            .enqueue(object :
                Callback<CommonResponse<PhotoResponse>> {
                override fun onResponse(
                    call: Call<CommonResponse<PhotoResponse>>,
                    response: Response<CommonResponse<PhotoResponse>>
                ) {
                    progressDialog!!.dismiss()

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.code == 200) {
                            callback(responseBody.data)
                        } else {
                            requireContext().showErrorToast(responseBody!!.message)
                            callback(null)
                        }
                    } else {
                        requireContext().showErrorToast("Error While Getting State List ")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommonResponse<PhotoResponse>>, t: Throwable) {
                    progressDialog!!.dismiss()
                    requireContext().showErrorToast(t.message.toString())
                    callback(null)
                }

            })

    }

    private fun getCreateFromData() {
        ApiClient.getRetrofitInstance().createUserData(
            sharedViewModel.userPostingModel.value!!.statePostingDataModel.stateId,
            sharedViewModel.userPostingModel.value?.divisionalPostingDataModel?.divisionId,
            sharedViewModel.userPostingModel.value?.districtPostingDataModel?.districtId,
            sharedViewModel.userPostingModel.value?.blockPostingDataModel?.blockId,
        ).enqueue(object : Callback<CommonResponse<CreateUserData>> {


            override fun onResponse(
                call: Call<CommonResponse<CreateUserData>>,
                response: Response<CommonResponse<CreateUserData>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {

                        createUserData = responseBody.data


                        if (responseBody.data.divisionList != null) {
                            setDivisionList(responseBody.data.divisionList)
                        }


                        setBankStateList(responseBody.data.stateList)

                        binding.officerId.setText(responseBody.data.officerId)

                        binding.officerName.setText(responseBody.data.officerName)

                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(call: Call<CommonResponse<CreateUserData>>, t: Throwable) {
                requireContext().showErrorToast(t.message.toString())
            }

        })


    }

    private fun setBankStateList(stateList: List<StatePostingDataModel>) {
        val stateNames = stateList.map { it.stateName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bankStateSpinner.adapter = adapter
    }

    private fun setDivisionList(divisionalList: List<DivisionalPostingDataModel>) {
        val divisionName = divisionalList.map { it.divisionName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisionName)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.divisionSpinner.adapter = adapter
    }

    private fun setDistrictList(
        districtList: List<DistrictPostingDataModel>,
        selectedDivisionId: Int
    ) {

        Log.d("sachinDistrictFilter", selectedDivisionId.toString())
         filteredDistricts = districtList.filter { it.divisionId == selectedDivisionId }

        Log.d("sachin", filteredDistricts.toString())
        val divisionNames = filteredDistricts.map { it.districtName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisionNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.districtSpinner.adapter = adapter
    }

    private fun setBlockList(blockList: List<BlockPostingDataModel>, selectedDistrictId: Int) {
        Log.d("sachinBlockFilter", selectedDistrictId.toString())

        val filteredBlocks = blockList.filter { it.districtId == selectedDistrictId }
        Log.d("sachinBlockList", filteredBlocks.toString())
        val blockNames = filteredBlocks.map { it.blockName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.blockSpinner.adapter = adapter
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val contentResolver: ContentResolver = requireContext().getContentResolver()

        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                try {
                    val projection = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(uri, projection, null, null, null)

                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                val columnIndex =
                                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                return cursor.getString(columnIndex)
                            }
                        } finally {
                            cursor.close()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("getRealPathFromURI", "Error: " + e.message)
                }
            } else {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    val cursor = contentResolver.query(uri!!, null, null, null, null)

                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                val displayName =
                                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                val projection = arrayOf(MediaStore.Images.Media.DATA)
                                val selection = MediaStore.Images.Media.DISPLAY_NAME + " = ?"
                                val selectionArgs = arrayOf(displayName)

                                val cursor2 = contentResolver.query(
                                    contentUri,
                                    projection,
                                    selection,
                                    selectionArgs,
                                    null
                                )

                                if (cursor2 != null) {
                                    try {
                                        if (cursor2.moveToFirst()) {
                                            val columnIndex =
                                                cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                            return cursor2.getString(columnIndex)
                                        }
                                    } finally {
                                        cursor2.close()
                                    }
                                }
                            }
                        } finally {
                            cursor.close()
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        101
                    )
                    return null
                }
            }
        } else if (ContentResolver.SCHEME_FILE == uri.scheme) {
            return uri.path
        }

        return null
    }


    fun clearSpinner(spinner: Spinner) {
        val emptyList = listOf<String>()
        val adapter =
            ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, emptyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


}
