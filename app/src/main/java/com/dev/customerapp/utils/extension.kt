package com.dev.customerapp.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


fun Context.changeActivity(newActivity: Class<*> , isBackAllow :Boolean = true) {
    val intent = Intent(this, newActivity)
    if (!isBackAllow) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

fun Context.showToast(message: String, isShortToast: Boolean = true) {
    Toast.makeText(this, message, if (isShortToast) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}

fun Context.progressDialog(isCancellable: Boolean = false, message: String = "Loading ..."): ProgressDialog {
    return ProgressDialog(this).apply {
        setCancelable(isCancellable)
        setMessage(message)
    }
}
fun printLog(tag:String = "sachin",message: String){
    Log.d(tag, message)
}
