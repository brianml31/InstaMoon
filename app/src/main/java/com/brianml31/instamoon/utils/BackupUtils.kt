package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import org.json.JSONObject
import java.io.File

class BackupUtils {
    companion object {
        fun exportBackup(context: Context) {
            if (!PermissionsUtils.checkPermission(context)) {
                PermissionsUtils.requestPermission(context)
            } else {
                try {
                    val mobileConfigDir = File(context.filesDir, "mobileconfig")
                    if (!mobileConfigDir.exists()) {
                        mobileConfigDir.mkdirs()
                    }
                    val fileMCOverrides = File(mobileConfigDir, "mc_overrides.json")
                    if (fileMCOverrides.exists()) {
                        DialogUtils.showFileNameDialog(context, fileMCOverrides)
                    } else {
                        ToastUtils.showShortToast(context, "There is no configuration file to export")
                    }
                } catch (e: Exception) {
                    ToastUtils.showShortToast(context, "Error: Could not export developer mode settings")
                }
            }
        }

        fun isInstamoonBackup(jsonString: String?): Boolean {
            try {
                val json = JSONObject(jsonString)
                if (!json.has("backup_info") || !json.has("instamoon_backup_content")) {
                    return false
                }
                val backupInfo = json.getJSONObject("backup_info")

                return backupInfo.has("backup_version") && backupInfo.has("instamoon_version") && backupInfo.has("instagram_version") && backupInfo.has("is_instamoon") && backupInfo.getBoolean("is_instamoon")
            } catch (e: Exception) {
                return false
            }
        }

        fun hasPasswordInBackup(jsonString: String?): Boolean {
            try {
                val json = JSONObject(jsonString)
                val backupInfo = json.getJSONObject("backup_info")
                return backupInfo.getBoolean("has_password")
            } catch (e: Exception) {
                return false
            }
        }

        fun applyBackupToOverrides(activity: Activity, content: String?) {
            val mc_overrides = FileUtils.loadMCOverridesFile(activity)
            val state = FileUtils.writeContent(mc_overrides, content)
            if(state.equals("SUCCESS")){
                ToastUtils.showShortToast(activity, "The backup was imported successfully")
                DialogUtils.showRestartAppDialog(activity)
            }else{
                ToastUtils.showShortToast(activity, "Error: " + state)
            }
        }

        fun createInstamoonBackupJson(ctx: Context, hasPassword: Boolean, instamoonBackupContent: String, password: String): JSONObject {
            val backupInfo = JSONObject()
            backupInfo.put("backup_version", 1)
            backupInfo.put("instamoon_version", Constants.VERSION)
            backupInfo.put("instagram_version", Utils.getVersionName(ctx))
            backupInfo.put("is_instamoon", true)
            backupInfo.put("has_password", hasPassword)

            val fullJson = JSONObject()
            fullJson.put("backup_info", backupInfo)
            fullJson.put("instamoon_backup_content", AESUtils.encryptTextWithPassword(instamoonBackupContent, password))

            return fullJson
        }

    }
}