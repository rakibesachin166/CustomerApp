package com.dev.customerapp.models

data class EmployeeModel(
    val employeeId: Int,
    var employeeName: String,
    var employeeDob: String,
    var employeeMobileNo: String,
    var employeeEmail: String,
    var employeeAddress: String,
    var employeeHouseNo: String,
    var employeeLocallity: String,
    var employeeBlockId: Int,
    var employeeDistrictId: Int,
    var employeePincode: Int,
    var employeePassword: String,
    val createdBy  :Int ,
    val employeeBlock : String = "",
    val employeeDistrict : String = "",
)
