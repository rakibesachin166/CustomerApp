package com.dev.customerapp.response

import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.StatePostingDataModel
import com.google.gson.annotations.SerializedName

data class CreateUserData(
    @SerializedName("stateList") val stateList: List<StatePostingDataModel>,
    @SerializedName("divisionList") val divisionList: List<DivisionalPostingDataModel>?,
    @SerializedName("districtList") val districtList: List<DistrictPostingDataModel>?,
    @SerializedName("blockList") val blockList: List<BlockPostingDataModel>?,
    @SerializedName("officerName") val officerName: String?,
    @SerializedName("officerId") val officerId: String?,
)