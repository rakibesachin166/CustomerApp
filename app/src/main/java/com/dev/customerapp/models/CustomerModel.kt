package com.dev.customerapp.models

data class CustomerModel(
    val customerId : Int,
    val customerName: String,
    val customerDob: String,
    val customerMobileNo: String,
    val customerEmail: String,
    val customerAddress: String,
    val customerHouseNo: String,
    val customerLocallity: String,
    val customerBlockId: Int,
    val customerDistrictId: Int,
    val customerPincode: Int,
    val customerPassword: String,
    val createdBy: Int,
    val customerRole: Int,
    val createdAt: String
)
