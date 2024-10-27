package com.dev.customerapp.utils


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dev.customerapp.R


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
 fun Context.showErrorToast(message: String , isShortToast: Boolean = false) {
    val inflater = LayoutInflater.from(this)
    val layout: View = inflater.inflate(R.layout.toast_error, null)
    val text = layout.findViewById<TextView>(R.id.toast_text)
    text.text = message
    Toast(this).apply {
        duration = if (isShortToast) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        view = layout
        show()
    }
}
fun Context.showSuccessToast(message: String , isShortToast: Boolean = false) {
    val inflater = LayoutInflater.from(this)
    val layout: View = inflater.inflate(R.layout.toast_success, null)
    val text = layout.findViewById<TextView>(R.id.toast_text)
    text.text = message
    Toast(this).apply {
        duration = if (isShortToast) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        view = layout
        show()
    }
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
fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.icon_person)
        .error(R.drawable.icon_person)
        .into(this)
}