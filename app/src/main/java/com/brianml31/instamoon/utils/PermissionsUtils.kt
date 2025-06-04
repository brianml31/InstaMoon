package com.brianml31.instamoon.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.brianml31.instamoon.PermissionDialog

class PermissionsUtils {
    companion object {
        fun checkPermission(ctx: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return Environment.isExternalStorageManager()
            } else {
                return ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        }

        fun requestPermission(ctx: Context) {
            val intentPermission = Intent(ctx, PermissionDialog::class.java)
            intentPermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intentPermission)
        }
    }
}