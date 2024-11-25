package com.dev.customerapp.models

data class VendorModel(
    val vendorName: String,
    val vendorFirm: String,
    val vendorAddress: String,
    val vendorMobileNo: String,
    val vendorType: String,
    val vendorRole :Int,
    val vendorBusinessCategory: String,
    val vendorCreateBy: String,
    val createdAt: String
)
