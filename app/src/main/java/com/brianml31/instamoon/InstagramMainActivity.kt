package com.brianml31.instamoon

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.brianml31.instamoon.utils.AESUtils
import com.brianml31.instamoon.utils.BackupUtils
import com.brianml31.instamoon.utils.DialogUtils
import com.brianml31.instamoon.utils.FileUtils
import com.brianml31.instamoon.utils.PermissionsUtils
import com.brianml31.instamoon.utils.ToastUtils
import com.instagram.mainactivity.InstagramMainActivity
import org.json.JSONObject

class InstagramMainActivity {
    companion object {
        private const val REQUEST_CODE_JSON_RESTORE = 74565
        private const val REQUEST_CODE_IGMOON_RESTORE = 74566

        fun after_onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_JSON_RESTORE && data.data != null && resultCode == -1) {
                    importBackup(activity, data.data, false)
                }
                if (requestCode == REQUEST_CODE_IGMOON_RESTORE && data.data != null && resultCode == -1) {
                    importBackup(activity, data.data, true)
                }
            }
        }

        fun requestFileJsonToRestore(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, REQUEST_CODE_JSON_RESTORE)
            }
        }

        fun requestFileIgMoonToRestore(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, REQUEST_CODE_IGMOON_RESTORE)
            }
        }

        fun importBackup(activity: Activity, data: Uri?, isIgMoon: Boolean){
            val contentBackup = BackupUtils.readBackupToImport(activity, data)
            if(contentBackup!=null){
                if(isIgMoon){
                    if(BackupUtils.isInstamoonBackup(contentBackup)){
                        if(BackupUtils.hasPasswordInBackup(contentBackup)){
                            DialogUtils.showPasswordDialog(activity, contentBackup)
                        }else{
                            val mc_overrides = FileUtils.loadMCOverridesFile(activity)
                            val instamoon_backup_content = JSONObject(contentBackup).getString("instamoon_backup_content")
                            val state = FileUtils.writeContent(mc_overrides, AESUtils.decrypt(instamoon_backup_content, ""))
                            if(state.equals("SUCCESS")){
                                ToastUtils.showShortToast(activity, "The backup was imported successfully")
                                DialogUtils.showRestartAppDialog(activity)
                            }else{
                                ToastUtils.showShortToast(activity, "Error: " + state)
                            }
                        }
                    }else{
                        ToastUtils.showShortToast(activity, "Error: Incompatible backup")
                    }
                }else{
                    val mc_overrides = FileUtils.loadMCOverridesFile(activity)
                    val state = FileUtils.writeContent(mc_overrides, contentBackup)
                    if(state.equals("SUCCESS")){
                        ToastUtils.showShortToast(activity, "The backup was imported successfully")
                        DialogUtils.showRestartAppDialog(activity)
                    }else{
                        ToastUtils.showShortToast(activity, "Error: " + state)
                    }
                }
            } else {
                ToastUtils.showShortToast(activity, "Error: Failed to read backup file")
            }
        }
    }
}