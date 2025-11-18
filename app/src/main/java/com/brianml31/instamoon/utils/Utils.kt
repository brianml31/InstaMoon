package com.brianml31.instamoon.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri

class Utils {
    companion object{
        fun getVersionName(context: Context): String {
            try {
                val packageManager = context.packageManager
                val packageName = context.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                return packageInfo.versionName ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                return ""
            }
        }

        fun getAppIcon(context: Context): Drawable {
            val packageManager = context.packageManager
            val applicationInfo = context.applicationInfo
            return applicationInfo.loadIcon(packageManager)
        }

        fun openLink(context: Context, url: String?) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}