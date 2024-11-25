package com.dev.customerapp.response

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("userPhoto") val userPhoto: String,
    @SerializedName("idPhoto") val idPhoto: String,
    @SerializedName("idPhoto2") val idPhoto2: String?,
    @SerializedName("addressPhoto") val addressPhoto: String,
    @SerializedName("addressPhoto2") val addressPhoto2: String?,
    @SerializedName("bankPhoto") val bankPhoto: String,
    @SerializedName("signaturePhoto") val signaturePhoto: String,
)