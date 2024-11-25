package com.dev.customerapp.fragments

import android.app.Dialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.dev.customerapp.R

open class BaseFragment  :Fragment() {

    private var progressDialog: Dialog? = null
    private var messageTextView: AppCompatTextView? = null

    fun showProgressDialog(show: Boolean , message : String = "Loading...") {

        if (show) {
            if (progressDialog == null) {
                progressDialog = Dialog(requireContext())
                progressDialog!!.setContentView(R.layout.dialog_progress_bar)
                messageTextView =progressDialog!!.findViewById(R.id.message)
                progressDialog!!.setCancelable(false)
                messageTextView!!.text = message
                progressDialog!!.setCanceledOnTouchOutside(false)
            }
            if (!isFinishing()) {
                messageTextView!!.text = message
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing) {
                if (!isFinishing()) {
                    progressDialog!!.dismiss()
                }
            }
        }

    }
    private fun isFinishing(): Boolean {
        return activity?.isFinishing == true
    }

}