package com.dev.customerapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.dev.customerapp.models.UserDataModel

import com.google.gson.Gson

class Constant(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveUserData(userModel: UserDataModel) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(userModel)
        editor.putString("user_data", json)
        editor.apply()
    }

    fun getUserData(): UserDataModel? {
        val json = sharedPreferences.getString("user_data", null)
        val gson = Gson()
        return gson.fromJson(json, UserDataModel::class.java)
    }

    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("user_data")
        editor.apply()
    }

}
