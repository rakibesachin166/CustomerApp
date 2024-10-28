package com.dev.customerapp.activity;

import android.R
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityLoginBinding
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.AgreementTxt
import com.dev.customerapp.utils.Constant
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

            call.enqueue(object : Callback<CommonResponse<UserDataModel>> {
                override fun onResponse(
                    call: Call<CommonResponse<UserDataModel>>,
                    response: Response<CommonResponse<UserDataModel>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.code == 200) {
                            val userDataModel = responseBody.data

                            Constant(this@LoginActivity).saveUserData(userDataModel)
                            showSuccessToast(responseBody.message)
                            fullScreenDialog()


                        } else {
                            showErrorToast(responseBody.message)
                        }
                    } else {
                        showErrorToast("Something went wrong. Please try again")
                    }

                    progressDialog?.dismiss()
                }

                override fun onFailure(call: Call<CommonResponse<UserDataModel>>, t: Throwable) {
                    progressDialog?.dismiss()
                    showErrorToast(t.message.toString())
                }
            })
        }
    }

    private fun fullScreenDialog(){
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(com.dev.customerapp.R.layout.dialog_agreement, null)
        dialog.setContentView(dialogView)

        // Set up the WebView
        val webView: WebView = dialogView.findViewById(com.dev.customerapp.R.id.dialogWebview)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/AASTHAGROUPSAGREEMENT.html");
        Handler().postDelayed({
            changeActivity(MainActivity::class.java, true)
        },5000)
        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.black))

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        progressDialog?.dismiss()
    }
}
