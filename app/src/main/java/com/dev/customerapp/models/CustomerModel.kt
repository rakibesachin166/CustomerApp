package com.dev.customerapp.models

data class CustomerModel(
    val customerName: String,
    val customerDob: String,
    val customerMobileNo: String,
    val customerEmail: String,
    val customerAddress: String,
    val customerHouseNo: String,
    val customerLocallity: String,
    val customerBlockId: String,
    val customerDistrictId: String,
    val customerPincode: Int,
    val createdBy: String,
    val createdAt: String
)
