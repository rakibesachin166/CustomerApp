package com.dev.customerapp.activity

import android.app.Dialog
import android.os.Bundle
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityUserDetailsBinding
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.loadImage
import com.dev.customerapp.utils.showErrorToast
import retrofit2.Callback

class UserDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("userId", 0)
        if (userId == 0) {
            throw NullPointerException("No User Id Passed")
        }

        getUserDetails(userId)

    }

    private fun getUserDetails(userId: Int) {
        showProgressDialog(true)
        ApiClient.getRetrofitInstance().getUserProfile(userId).enqueue(object :
            Callback<CommonResponse<UserDataModel>> {
            override fun onResponse(
                call: retrofit2.Call<CommonResponse<UserDataModel>>,
                response: retrofit2.Response<CommonResponse<UserDataModel>>
            ) {
                showProgressDialog(false)

                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    if (userDetailsResponse.code == 200) {

                        setUserData(userDetailsResponse.data)

                    } else {
                        showErrorToast(userDetailsResponse.message)
                    }

                } else {
                    showErrorToast("No Response From Server Please Try Again")
                }

            }

            override fun onFailure(
                call: retrofit2.Call<CommonResponse<UserDataModel>>,
                t: Throwable
            ) {
                showProgressDialog(false)
                showErrorToast(t.message.toString())
            }
        })
    }

    private fun setUserData(data: UserDataModel) {

        //Personal Details

        binding.applicantNameEdiText.setText(data.userName)
        val genderMap = mapOf(
            1 to "Male",
            2 to "Female",
            3 to "Transgender"
        )

        binding.applicantGenderEdiText.setText(genderMap[data.gender] ?: "Other")
        val marriageMap = mapOf(
            1 to "Married",
            2 to "Unmarried",
            3 to "Widow/Widower",
            4 to "Divorced"
        )
        binding.applicantMaritalStatusEdiText.setText(marriageMap[data.maritalStatus] ?: "Other")

        binding.applicantDOBEditText.setText(data.userDob)
        binding.applicantAadharEditText.setText(data.userAadharNo)
        binding.applicantOccupationEditText.setText(data.occupation)


        ///work location

        binding.workStateEdiText.setText(data.stateName)
        binding.workDivisionEdiText.setText(data.divisionName)
        binding.workDistrictEdiText.setText(data.districtName)
        binding.workBlockEditText.setText(data.blockName)


        //Communication Details
        binding.addressEditText.setText(data.userAddress)
        binding.stateEditText.setText(data.userState)
        binding.divisionEditText.setText(data.userDivision)
        binding.districtEditText.setText(data.userDistrict)
        binding.cityEditText.setText(data.userCity)
        binding.tehsilEditText.setText(data.userTehsil)
        binding.blockEditText.setText(data.userBlock)
        binding.villageEditText.setText(data.userVillage)
        binding.postEditText.setText(data.userPost)
        binding.pinCodeEditText.setText(data.userPincode)
        binding.mobileNumberEditText.setText(data.userMobileNo)
        binding.alternativeMobileNOEditText.setText(data.userAlternativeNo)
        binding.emailAddressEditText.setText(data.userEmail)

        //Nominee Details

        binding.nomineeNameEditText.setText(data.nomineeName)
        binding.nomineeAadharEditText.setText(data.nomineeAadharNo)
        binding.nomineeDOBEditText.setText(data.nomineeDob)
        binding.relationNomineeEditText.setText(data.userRelWithNominee)
        binding.nomineeAddressEditText.setText(data.nomineeAddress)


        //Bank Details

        binding.bankNameEditText.setText(data.bankName)
        binding.bankStateEditText.setText(data.bankState)
        binding.branchNameEdittext.setText(data.branchName)
        binding.accountNoEditText.setText(data.bankAccountNo)
        binding.panNoEditText.setText(data.panNo)


        //Images
        binding.userPhotoImageView.loadImage(ApiClient.BASE_URL + data.userPhoto)

        binding.uploadOriginalIdImageView.loadImage(ApiClient.BASE_URL + data.idProofUrl)
        binding.uploadOriginalIdImageView2.loadImage(ApiClient.BASE_URL + data.idProofUrl2)

        binding.uploadAddressProofImageView.loadImage(ApiClient.BASE_URL + data.addressProofUrl)
        binding.userPhotoImageView.loadImage(ApiClient.BASE_URL + data.addressProofUrl2)


        binding.uploadBankDetailsImageView.loadImage(ApiClient.BASE_URL + data.bankProofPhoto)
        binding.uploadSignatureIdImageView.loadImage(ApiClient.BASE_URL + data.signaturePhoto)


    }


}