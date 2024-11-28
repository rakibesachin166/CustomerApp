package com.dev.customerapp.models

data class VendorModel(
    val vendorId: Int,
    val vendorName: String,
    val vendorFirm: String,
    val vendorAddress: String,
    val vendorMobileNo: String,
    val vendorType: String,
    val vendorRole :Int,
    val vendorBusinessCategory: String,
    val vendorPinCode: String,
    val vendorCreateBy: String,
    val vendorPassword: String,
    val createdAt: String
)
