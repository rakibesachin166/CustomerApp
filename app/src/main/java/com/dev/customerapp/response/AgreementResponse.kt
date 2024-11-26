package com.dev.customerapp.response

import com.dev.customerapp.models.AgreementModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.google.gson.annotations.SerializedName

data class AgreementResponse(
    @SerializedName("agreement") val agreement: String?,
    @SerializedName("isAccepted") val isAccepted: Int,
    @SerializedName("agreementData") val agreementData: AgreementModel?,
)
