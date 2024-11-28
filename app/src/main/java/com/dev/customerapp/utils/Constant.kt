package com.dev.customerapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CreateEmployeeData

import com.google.gson.Gson

class Constant(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveUserData(userModel: UserDataModel ) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(userModel)
        editor.putString("user_data", json)
        editor.putInt("loginType", 1)
        editor.apply()
    }
    fun saveEmployeeData(employeeData: EmployeeModel ) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(employeeData)
        editor.putString("user_data", json)
        editor.putInt("loginType", 2)
        editor.apply()
    }

    fun saveVendorData(vendorData: VendorModel ) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(vendorData)
        editor.putString("user_data", json)
        editor.putInt("loginType", 3)
        editor.apply()
    }

    fun saveCustomerData(customerData: CustomerModel ) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(customerData)
        editor.putString("user_data", json)
        editor.putInt("loginType", 4)
        editor.apply()
    }
    fun getLoginType(): Int {
        return sharedPreferences.getInt("loginType", 0)
    }


    fun getUserData(): UserDataModel? {
        val json = sharedPreferences.getString("user_data", null)
        val gson = Gson()
        return gson.fromJson(json, UserDataModel::class.java)
    }

    fun getEmployeeData(): EmployeeModel {
        val json = sharedPreferences.getString("user_data", null)
        val gson = Gson()
        return gson.fromJson(json, EmployeeModel::class.java)
    }
    fun getVendorData(): VendorModel {
        val json = sharedPreferences.getString("user_data", null)
        val gson = Gson()
        return gson.fromJson(json, VendorModel::class.java)
    }
    fun getCustomerData(): CustomerModel {
        val json = sharedPreferences.getString("user_data", null)
        val gson = Gson()
        return gson.fromJson(json, CustomerModel::class.java)
    }

    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("user_data")
        editor.remove("loginType")
        editor.apply()
    }

    fun getLoginUserIdAndLoginType(): Pair<Int, Int> {

        when(val loginType = sharedPreferences.getInt("loginType", 0)){
            1->{
                val user = getUserData()
                return Pair(user!!.userId, loginType)
            }
            2->{
                val user = getEmployeeData()
                return Pair(user.employeeId, loginType)
            }

            3->{
                val user = getVendorData()
                return Pair(user.vendorId, loginType)
            }
            4->{
                val user = getCustomerData()
                return Pair(user.customerId, loginType)
            }
            else -> {
                throw NullPointerException("No Login User Found")
            }

        }


    }

}
