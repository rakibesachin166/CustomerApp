package com.dev.customerapp.response

import com.google.gson.annotations.SerializedName

data class CommonResponse<A>(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: A
)