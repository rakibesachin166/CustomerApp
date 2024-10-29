package com.dev.customerapp.models
data class CreateUserModel(
    val userType: Int,
    val stateId: Int,
    val divisionId: Int? = null,
    val districtId: Int? = null,
    val blockId: Int? = null,
    val userName: String,
    val gender: Int,
    val maritalStatus: Int,
    val userPhoto: String,
    val userDob: String,
    val userAadharNo: String,
    val occupation: String,
    val userAddress: String,
    val userState: String,
    val userDistrict: String,
    val userTehsil: String,
    val userPost: String,
    val userCity: String,
    val userPincode: String,
    val userMobileNo: String,
    val userAlternativeNo: String,
    val userEmail: String,
    val idProofType: String,
    val idProofUrl: String,
    val addressProofType: String,
    val addressProofUrl: String,
    val nomineeName: String,
    val nomineeAadharNo: String,
    val nomineeDob: String, // Consider using LocalDate for date fields
    val userRelWithNominee: String,
    val nomineeAddress: String,
    val bankName: String,
    val bankState: String,
    val branchName: String,
    val bankAccountNo: String,
    val panNo: String,
    val bankProofPhoto: String,
    val signaturePhoto: String,
    val userPassword: String,
    val createdAt: String // Consider using LocalDateTime for date-time fields
)
