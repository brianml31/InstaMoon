package com.brianml31.instamoon.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri

class Utils {
    companion object{
        fun getVersionName(ctx: Context): String {
            try {
                val packageManager = ctx.packageManager
                val packageName = ctx.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                return packageInfo.versionName ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                return ""
            }
        }

        fun getAppIcon(ctx: Context): Drawable {
            val packageManager = ctx.packageManager
            val applicationInfo = ctx.applicationInfo
            return applicationInfo.loadIcon(packageManager)
        }

        fun openLink(ctx: Context, url: String?) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ctx.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}