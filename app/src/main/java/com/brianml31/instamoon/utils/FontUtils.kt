package com.brianml31.instamoon.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Environment
import com.brianml31.instamoon.handlers.ActivityResultHandler
import com.brianml31.instamoon.tasks.DownloadFontTask
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File

class FontUtils {

    companion object{
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
                val appFontSelectedItem: Int = PrefsUtils.getInt("appFontSelectedItem",0)
                var fontPath: String? = null
                if(appFontSelectedItem == 1){
                    fontPath = PrefsUtils.getString("customFontPath", null)
                }else if(appFontSelectedItem >= 2 && appFontSelectedItem <=5){
                    fontPath = PrefsUtils.getString("fontPath", null)
                }

                if (fontPath != null) {
                    appTypeFace = Typeface.createFromFile(fontPath)
                }else{
                    appTypeFace = null
                }

            } catch (e: Exception) {
                appTypeFace = null
            }
        }


        fun requestFontFileToApply(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity);
            } else {
                val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                val mimeTypes: Array<String> = arrayOf(
                    "font/ttf",
                    "application/x-font-ttf",
                    "application/octet-stream"
                )
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                instagramMainActivity.startActivityForResult(intent, ActivityResultHandler.REQUEST_CODE_PICK_FONT)
            }
        }

        fun downloadFont(context: Context, fontName: String, fontEndpoint: String){
            if (!PermissionsUtils.checkPermission(context)) {
                PermissionsUtils.requestPermission(context)
            } else {
                if(!isFontDownloaded(fontName)){
                    DialogUtils.showDialog(
                        context,
                        "WARNING",
                        "This font is not available on your device, Do you want to download it?",
                        false,
                        null,
                        null,
                        true,
                        "NO",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        },
                        true,
                        "DOWNLOAD",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    val downloadFont: DownloadFontTask = DownloadFontTask(context,fontName)
                                    downloadFont.execute(UrlUtils.buildUrl(fontEndpoint))
                                } else {
                                    ToastUtils.showShortToast(context, "No internet connection")
                                }
                            }
                        }
                    )
                }
            }
        }

        private fun isFontDownloaded(fontName: String): Boolean {
            val dirFonts: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FONTS_OUTPUT_FOLDER)
            if (!dirFonts.exists()) {
                dirFonts.mkdirs();
            }
            val fontFile: File = File(dirFonts, fontName)
            return fontFile.exists()
        }

        fun applyFont(context: Context, selectedItem: Int, fontName: String){
            if(!isFontDownloaded(fontName)){
                ToastUtils.showShortToast(context, "Please download the font")
            } else {
                PrefsUtils.saveInt(context, "appFontSelectedItem", selectedItem)
                val dirFonts: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FONTS_OUTPUT_FOLDER)
                if (!dirFonts.exists()) {
                    dirFonts.mkdirs();
                }
                val fontFile: File = File(dirFonts, fontName)
                PrefsUtils.saveString(context, "fontPath", fontFile.path)
                ToastUtils.showShortToast(context, "Font applied")
                DialogUtils.showRestartAppDialog(context)
            }
        }
    }
}