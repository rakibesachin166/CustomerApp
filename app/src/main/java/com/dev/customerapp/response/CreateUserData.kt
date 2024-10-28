package com.dev.customerapp.response

import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.google.gson.annotations.SerializedName

data class CreateUserData(
    @SerializedName("stateList") val stateList: List<StatePostingDataModel>,
    @SerializedName("districtList") val districtList: List<DistrictPostingDataModel>
)