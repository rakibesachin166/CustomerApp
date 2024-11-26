package com.dev.customerapp.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dev.customerapp.R

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    fun showProgressDialog(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = Dialog(this)
                progressDialog!!.setContentView(R.layout.dialog_progress_bar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setCanceledOnTouchOutside(false)
            }
            if (!isFinishing) {
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing) {
                if (!isFinishing) {
                    progressDialog!!.dismiss()
                }
            }
        }
    }

     fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}