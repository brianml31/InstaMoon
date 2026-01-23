package com.brianml31.instamoon.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri

class Utils {
    companion object{
        fun getVersionName(context: Context): String {
            try {
                val packageManager: PackageManager = context.packageManager
                val packageName: String = context.packageName
                val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                return packageInfo.versionName ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                return ""
            }
        }

        fun getAppIcon(context: Context): Drawable {
            val packageManager: PackageManager = context.packageManager
            val applicationInfo: ApplicationInfo = context.applicationInfo
            return applicationInfo.loadIcon(packageManager)
        }

        fun openLink(context: Context, url: String?) {
            try {
                val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }

    }
}