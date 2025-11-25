package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Environment
import com.hippo.unifile.UniFile
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File

class FontUtils {

    companion object{
        private const val REQUEST_CODE_PICK_FONT = 74567
        private var appTypeFace: Typeface? = null

        fun getCustomFont(typeface: Typeface): Typeface {
            if (appTypeFace != null) {
                return appTypeFace!!
            } else {
                return typeface
            }
        }

        fun initFont() {
            try {
                appTypeFace = null
                val string: String? = PrefsUtils.getString("appFontPath", null)
                if (string != null) {
                    appTypeFace = Typeface.createFromFile(string)
                }
            } catch (e: Exception) {
                appTypeFace = null
            }
        }

        fun requestFontFileToApply(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity);
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                val mimeTypes = arrayOf("font/ttf", "application/x-font-ttf", "application/octet-stream")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                instagramMainActivity.startActivityForResult(intent, REQUEST_CODE_PICK_FONT)
            }
        }

        fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent){
            if (requestCode == REQUEST_CODE_PICK_FONT && data.data != null && resultCode == -1) {
                val appFontPath = UniFile.fromUri(activity, data.data!!).filePath
                PrefsUtils.saveString(activity, "appFontPath", appFontPath)
                ToastUtils.showShortToast(activity, "Done")
                DialogUtils.showRestartAppDialog(activity)
            }
        }

        fun clearFont(context: Context) {
            PrefsUtils.removeString(context, "appFontPath")
            ToastUtils.showShortToast(context, "Font Cleaned")
            DialogUtils.showRestartAppDialog(context)
        }

        fun downloadFont(context: Context, fontName: String, urlFont: String) {
            if (!PermissionsUtils.checkPermission(context)) {
                PermissionsUtils.requestPermission(context)
            } else {
                val dirFonts = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FONTS_OUTPUT_FOLDER)
                if (!dirFonts.exists()) {
                    dirFonts.mkdirs();
                }
                val fontFile = File(dirFonts, fontName)
                if (fontFile.exists()) {
                    PrefsUtils.saveString(context, "appFontPath", fontFile.absolutePath)
                    ToastUtils.showShortToast(context, "Font Applied")
                    DialogUtils.showRestartAppDialog(context)
                } else {
                    if (NetworkUtils.isInternetAvailable(context)) {
                        DownloadFontTask(context, fontName).execute(urlFont)
                    } else {
                        ToastUtils.showShortToast(context, "No internet connection")
                    }
                }
            }
        }
    }
}