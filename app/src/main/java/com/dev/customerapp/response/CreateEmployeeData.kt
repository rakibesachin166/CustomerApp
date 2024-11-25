package com.dev.customerapp.response

import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.google.gson.annotations.SerializedName

data class CreateEmployeeData(
    @SerializedName("districtName") val districtName: String?,
    @SerializedName("blockName") val blockName: String?,
)