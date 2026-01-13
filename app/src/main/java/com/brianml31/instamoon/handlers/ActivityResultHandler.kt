package com.brianml31.instamoon.handlers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.brianml31.instamoon.utils.BackupManager
import com.brianml31.instamoon.utils.FileUtils
import com.brianml31.instamoon.utils.PrefsUtils
import com.brianml31.instamoon.utils.ToastUtils
import com.hippo.unifile.UniFile

class ActivityResultHandler {
    companion object {
        const val REQUEST_CODE_PICK_FONT: Int = 347
        const val REQUEST_CODE_JSON_RESTORE: Int = 348
        const val REQUEST_CODE_IGMOON_RESTORE: Int = 349
        const val REQUEST_CODE_MAPPING_FILE_IMPORT: Int = 350
        

        fun handleActivityResult(
            activity: Activity,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            if (requestCode == REQUEST_CODE_PICK_FONT && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data
                if (uri!=null) {
                    val customFontPath: String? = UniFile.fromUri(activity, uri)?.filePath
                    if (customFontPath!!.lowercase().endsWith(".ttf")) {
                        PrefsUtils.saveString(activity, "customFontPath", customFontPath)
                        ToastUtils.showShortToast(activity, "Font selected successfully")
                    } else {
                        ToastUtils.showShortToast(activity, "Selected file is not a TTF font")
                    }
                }
            }
            if (requestCode == REQUEST_CODE_JSON_RESTORE && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    BackupManager.importBackup(activity, uri, ".json")
                }
            }
            if (requestCode == REQUEST_CODE_IGMOON_RESTORE && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    BackupManager.importBackup(activity, uri, ".igmoon")
                }
            }
            if (requestCode == REQUEST_CODE_MAPPING_FILE_IMPORT && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    FileUtils.importMappingFile(activity, uri)
                }
            }
        }

    }
}