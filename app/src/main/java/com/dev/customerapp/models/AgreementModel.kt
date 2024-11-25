package com.dev.customerapp.models

import com.google.gson.annotations.SerializedName

data class AgreementModel(
    @SerializedName("agreementId ") val agreementId: Int,
    @SerializedName("agreement") val agreement: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("createdAt") val createdAt: String,
)
