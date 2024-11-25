package com.dev.customerapp.response

import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.models.VendorModel
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("user") val user: UserDataModel,// $type = 1;// User
    @SerializedName("employee") val employee: EmployeeModel, // $type = 2;  // Employee
    @SerializedName("vendor") val vendor: VendorModel,  //   $type = 3;  // Vendor
    @SerializedName("customer") val customer: CustomerModel, // $type = 4;  // Customer
    @SerializedName("type") val type: Int,
)