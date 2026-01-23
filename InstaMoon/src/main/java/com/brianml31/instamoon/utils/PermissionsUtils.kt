package com.brianml31.instamoon.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.brianml31.instamoon.activities.StoragePermissionActivity


class PermissionsUtils {
    companion object {
        fun checkPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return Environment.isExternalStorageManager()
            } else {
                return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        }

        fun requestPermission(context: Context) {
            val intentPermission: Intent = Intent(context, StoragePermissionActivity::class.java)
            intentPermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intentPermission)
        }
    }
}