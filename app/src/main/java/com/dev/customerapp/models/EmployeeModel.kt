package com.dev.customerapp.models

data class EmployeeModel(
    val employeeName: String,
    val employeeDob: String,
    val employeeMobileNo: String,
    val employeeEmail: String,
    val customerAddress: String,
    val employeeHouseNo: String,
    val employeeLocallity: String,
    val employeeBlockId: String,
    val employeeDistrictId: String,
    val employeePincode: Int,
    val createdBy: String,
    val createdAt: String
)
