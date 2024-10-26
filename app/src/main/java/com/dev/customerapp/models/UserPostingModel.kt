package com.dev.customerapp.models

data class UserPostingModel(
    var statePostingDataModel: StatePostingDataModel,
    val divisionalPostingDataModel: DivisionalPostingDataModel,
    val districtPostingDataModel: DistrictPostingDataModel,
    val blockPostingDataModel: BlockPostingDataModel
)
