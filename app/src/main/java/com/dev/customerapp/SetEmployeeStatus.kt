package com.dev.customerapp

import com.dev.customerapp.models.EmployeeModel

interface SetEmployeeStatus {
    fun setEmployeeStatus(employeeModel: EmployeeModel, status  :Int ,position: Int)
}