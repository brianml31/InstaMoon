package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.instagram.mainactivity.InstagramMainActivity
import org.json.JSONObject

class BackupManager {
    companion object {
        private const val REQUEST_CODE_JSON_RESTORE = 74565
        private const val REQUEST_CODE_IGMOON_RESTORE = 74566

        fun after_onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_JSON_RESTORE && data.data != null && resultCode == -1) {
                    importBackup(activity, data.data, ".igmoon")
                }
                if (requestCode == REQUEST_CODE_IGMOON_RESTORE && data.data != null && resultCode == -1) {
                    importBackup(activity, data.data, ".json")
                }
            }
        }

        fun requestFileJsonToRestore(instagramMainActivity: InstagramMainActivity) {
            requestFileToRestore(instagramMainActivity, REQUEST_CODE_JSON_RESTORE)
        }

        fun requestFileIgMoonToRestore(instagramMainActivity: InstagramMainActivity) {
            requestFileToRestore(instagramMainActivity, REQUEST_CODE_IGMOON_RESTORE)
        }

        private fun requestFileToRestore(instagramMainActivity: InstagramMainActivity, requestCode: Int) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, requestCode)
            }
        }

        private fun importBackup(activity: Activity, data: Uri?, typeBackup: String){
            val contentBackup = FileUtils.readFileFromUri(activity, data)
            if(contentBackup!=null){
                when (typeBackup) {
                    ".igmoon" -> {
                        if(BackupUtils.isInstamoonBackup(contentBackup)){
                            if(BackupUtils.hasPasswordInBackup(contentBackup)){
                                DialogUtils.showPasswordDialog(activity, contentBackup)
                            }else{
                                val instamoon_backup_content = JSONObject(contentBackup).getString("instamoon_backup_content")
                                BackupUtils.applyBackupToOverrides(activity, AESUtils.decryptTextWithPassword(instamoon_backup_content, ""))
                            }
                        }else{
                            ToastUtils.showShortToast(activity, "Error: Incompatible backup")
                        }
                    }
                    ".json" -> {
                        BackupUtils.applyBackupToOverrides(activity, contentBackup)
                    }
                }
            } else {
                ToastUtils.showShortToast(activity, "Error: Failed to read backup file")
            }
        }
    }
}