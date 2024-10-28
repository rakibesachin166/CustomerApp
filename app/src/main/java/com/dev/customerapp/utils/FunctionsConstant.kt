package com.dev.customerapp.utils;

 class FunctionsConstant
{
    companion object{
        fun  getUserRoleName(userType :Int) :String {
            when (userType) {
                1 -> return "Admin"
                2 -> return "State Vendor President"
                3 -> return "Divisional Vendor President"
                4 -> return "District Vendor President"
                5 -> return "Block Vendor President"
                else -> return "Unknown"
            }
        }
    }

}
