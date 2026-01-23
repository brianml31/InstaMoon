package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.brianml31.instamoon.handlers.ActivityResultHandler
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File

class BackupManager {
    companion object {

        fun requestFileJsonToRestore(instagramMainActivity: InstagramMainActivity) {
            requestFileToRestore(instagramMainActivity, ActivityResultHandler.REQUEST_CODE_JSON_RESTORE)
        }

        fun requestFileIgMoonToRestore(instagramMainActivity: InstagramMainActivity) {
            requestFileToRestore(instagramMainActivity, ActivityResultHandler.REQUEST_CODE_IGMOON_RESTORE)
        }

        private fun requestFileToRestore(instagramMainActivity: InstagramMainActivity, requestCode: Int) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, requestCode)
            }
        }

        fun importBackup(activity: Activity, dataUri: Uri, typeBackup: String) {
            val contentFile: String? = FileUtils.readFileFromUri(activity, dataUri)
            if(contentFile != null){
                when (typeBackup) {
                    ".igmoon" -> {
                        if(BackupUtils.isInstamoonBackup(contentFile)){
                            if(BackupUtils.hasPasswordInBackup(contentFile)){
                                DialogUtils.showPasswordDialog(activity, contentFile)
                            }else{
                                BackupUtils.processBackupContent(activity, contentFile, "")
                            }
                        }else{
                            ToastUtils.showShortToast(activity, "The selected backup file is not compatible")
                        }
                    }
                    ".json" -> {
                        applyBackupToOverrides(activity, contentFile)
                    }
                }
            } else {
                ToastUtils.showShortToast(activity, "Unable to read the selected backup file")
            }
        }


        fun applyBackupToOverrides(context: Context, content: String) {
            val fileMCOverrides: File = FileUtils.loadMCOverridesFile(context)
            if (!fileMCOverrides.exists()) {
                fileMCOverrides.createNewFile()
            }
            val state: String? = FileUtils.writeContent(fileMCOverrides, content)
            if(state.equals("SUCCESS")){
                ToastUtils.showShortToast(context, "Backup imported successfully")
                DialogUtils.showRestartAppDialog(context)
            }else{
                ToastUtils.showShortToast(context, "An error occurred while importing the backup: " + state)
            }
        }



    }
}