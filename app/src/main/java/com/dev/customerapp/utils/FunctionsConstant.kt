package com.dev.customerapp.utils;

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class FunctionsConstant {
    companion object {
        fun getUserRoleName(userType: Int): String {
            return when (userType) {
                1 -> "Admin"
                2 -> "State Vendor President"
                3 -> "Divisional Vendor President"
                4 -> "District Vendor President"
                5 -> "Block Vendor President"
                else -> "Unknown"
            }
        }
        fun getUserFullId(userType: Int, userId: Int): String {
            val formattedUserId = String.format("%07d", userId)
            val officerId = when (userType) {
                2 -> "ASGS$formattedUserId"
                3 -> "ASGM$formattedUserId"
                4 -> "ASGD$formattedUserId"
                5 -> "ASGB$formattedUserId"
                else -> "Admin"
            }
            return officerId
        }

        fun getRealPathFromURI(uri: Uri , context :  Activity): String? {
            val contentResolver: ContentResolver = context.contentResolver

            if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                    try {
                        val projection = arrayOf(MediaStore.Images.Media.DATA)

                        val cursor = contentResolver.query(uri, projection, null, null, null)

                        if (cursor != null) {
                            try {
                                if (cursor.moveToFirst()) {
                                    val columnIndex =
                                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                    return cursor.getString(columnIndex)
                                }
                            } finally {
                                cursor.close()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("getRealPathFromURI", "Error: " + e.message)
                    }
                } else {
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        val cursor = contentResolver.query(uri!!, null, null, null, null)

                        if (cursor != null) {
                            try {
                                if (cursor.moveToFirst()) {
                                    val displayName =
                                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                                    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                                    val selection = MediaStore.Images.Media.DISPLAY_NAME + " = ?"
                                    val selectionArgs = arrayOf(displayName)

                                    val cursor2 = contentResolver.query(
                                        contentUri,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null
                                    )

                                    if (cursor2 != null) {
                                        try {
                                            if (cursor2.moveToFirst()) {
                                                val columnIndex =
                                                    cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                                return cursor2.getString(columnIndex)
                                            }
                                        } finally {
                                            cursor2.close()
                                        }
                                    }
                                }
                            } finally {
                                cursor.close()
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            101
                        )
                        return null
                    }
                }
            } else if (ContentResolver.SCHEME_FILE == uri.scheme) {
                return uri.path
            }

            return null
        }
    }

}
