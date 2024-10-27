package com.dev.customerapp.models

import com.google.gson.annotations.SerializedName

data class UserDataModel(
    @SerializedName("userType") val userType: Int,
    @SerializedName("userName") val userName: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("maritalStatus") val maritalStatus: Int,
    @SerializedName("userPhoto") val userPhoto: String,
    @SerializedName("userDob") val userDob: String,
    @SerializedName("userAadharNo") val userAadharNo: String,
    @SerializedName("userFatherName") val userFatherName: String,
    @SerializedName("occupation") val occupation: String,
    @SerializedName("userAddress") val userAddress: String,
    @SerializedName("userState") val userState: String,
    @SerializedName("userDistrict") val userDistrict: String,
    @SerializedName("userTehsil") val userTehsil: String?,
    @SerializedName("userPost") val userPost: String?,
    @SerializedName("userCity") val userCity: String,
    @SerializedName("userPincode") val userPincode: String,
    @SerializedName("userMobileNo") val userMobileNo: String,
    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("idproofType") val idProofType: String,
    @SerializedName("idProofUrl") val idProofUrl: String,
    @SerializedName("nomineeName") val nomineeName: String,
    @SerializedName("nomineeAadharNo") val nomineeAadharNo: String,
    @SerializedName("nomineeDob") val nomineeDob: String,
    @SerializedName("userRelWithNominee") val userRelWithNominee: String,
    @SerializedName("nomineeAddress") val nomineeAddress: String,
    @SerializedName("bankName") val bankName: String,
    @SerializedName("bankState") val bankState: String,
    @SerializedName("branchName") val branchName: String,
    @SerializedName("bankAccountNo") val bankAccountNo: String,
    @SerializedName("panNo") val panNo: String?,
    @SerializedName("bankProofPhoto") val bankProofPhoto: String,
    @SerializedName("userPassword") val userPassword: String
)

