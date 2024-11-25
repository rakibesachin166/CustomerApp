package com.dev.customerapp.activity;

import android.R
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityLoginBinding
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.LoginResponse
import com.dev.customerapp.utils.AgreementTxt
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.FunctionsConstant
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.progressDialog
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.userLoginButton?.setOnClickListener {
            val username = binding?.userNameEdt?.text.toString()
            val password = binding?.passwordEdt?.text.toString()

            if (username.isEmpty()) {
                binding?.userNameEdt?.error = "Please enter username"
                binding?.userNameEdt?.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding?.passwordEdt?.error = "Please enter password"
                binding?.passwordEdt?.requestFocus()
                return@setOnClickListener
            }
            if (progressDialog == null) {
                progressDialog = progressDialog(message = "Loading...")
            }
            val call = ApiClient.getRetrofitInstance().loginUser(username, password)

            call.enqueue(object : Callback<CommonResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<CommonResponse<LoginResponse>>,
                    response: Response<CommonResponse<LoginResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.code == 200) {
                            val loginResponseModel = responseBody.data
                            showSuccessToast(responseBody.message)
                            when(loginResponseModel.type){
                                1->{
                                    // $type = 1;// User
                                    Constant(this@LoginActivity).saveUserData(loginResponseModel.user)
                                    changeActivity( MainActivity::class.java,false)
                                }
                                2-> {
                                    // $type = 2;  // Employee
                                    Constant(this@LoginActivity).saveEmployeeData(loginResponseModel.employee)
                                    changeActivity( EmployeeMainActivity::class.java,false)

                                }
                                3->{
                                    //   $type = 3;  // Vendor
                                    Constant(this@LoginActivity).saveVendorData(loginResponseModel.vendor)
                                    changeActivity( VendorMainActivity::class.java,false)
                                }
                                4->{
                                    // $type = 4;  // Customer
                                    Constant(this@LoginActivity).saveCustomerData(loginResponseModel.customer)
                                    changeActivity( CustomerMainActivity::class.java,false)
                                }

                            }

                            finish()


                        } else {
                            showErrorToast(responseBody.message)
                        }
                    } else {
                        showErrorToast("Something went wrong. Please try again")
                    }

                    progressDialog?.dismiss()
                }

                override fun onFailure(call: Call<CommonResponse<LoginResponse>>, t: Throwable) {
                    progressDialog?.dismiss()
                    showErrorToast(t.message.toString())
                }
            })
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
        progressDialog?.dismiss()
    }
}
