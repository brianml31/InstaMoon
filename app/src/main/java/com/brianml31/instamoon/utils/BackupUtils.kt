package com.brianml31.instamoon.utils

import android.content.Context
import android.widget.EditText
import org.json.JSONObject
import java.io.File

class BackupUtils {
    companion object {
        fun exportDevSettings(context: Context) {
            if (!PermissionsUtils.checkPermission(context)) {
                PermissionsUtils.requestPermission(context)
            } else {
                try {
                    val fileMCOverrides: File = FileUtils.loadMCOverridesFile(context)
                    if (fileMCOverrides.exists()) {
                        DialogUtils.showBackupExportDialog(context, fileMCOverrides)
                    } else {
                        ToastUtils.showShortToast(context, "No configuration file available to export")
                    }
                } catch (e: Exception) {
                    ToastUtils.showShortToast(context, "Unable to export developer mode settings")
                }
            }
        }

        fun isInstamoonBackup(contentFile: String): Boolean {
            try {
                val backupJson: JSONObject = JSONObject(contentFile)
                if (!backupJson.has("backupInfo") || !backupJson.has("backupContent")) {
                    return false
                }
                val backupInfo: JSONObject = backupJson.getJSONObject("backupInfo")
                return backupInfo.has("backupVersion") &&
                        backupInfo.has("instamoonVersion") &&
                        backupInfo.has("instagramVersion") &&
                        backupInfo.has("isInstamoon") &&
                        backupInfo.getBoolean("isInstamoon")
            } catch (e: Exception) {
                return false
            }
        }

        fun hasPasswordInBackup(contentFile: String): Boolean {
            try {
                val backupJson: JSONObject = JSONObject(contentFile)
                val backupInfo: JSONObject = backupJson.getJSONObject("backupInfo")
                return backupInfo.getBoolean("hasPassword")
            } catch (e: Exception) {
                return false
            }
        }

        fun processBackupContent(context: Context, contentFile: String, password: String){
            val backupJson: JSONObject = JSONObject(contentFile)
            val encryptedBackupContent: String = backupJson.getString("backupContent")
            val decryptedBackupContent: String? = AESUtils.decryptTextWithPassword(encryptedBackupContent, password)
            if(decryptedBackupContent != null){
                BackupManager.applyBackupToOverrides(context, decryptedBackupContent)
            } else {
                ToastUtils.showLongToast(context, "Incorrect password or corrupted backup data")
            }
        }

        fun exportInstaMoonBackup(
            outputFileName: String,
            inputPassword: EditText,
            context: Context,
            contentFile: String
        ){
            val backupsDir: File = StoragePaths.backupsDir
            if (!backupsDir.exists()) {
                backupsDir.mkdirs()
            }
            val fileOutput: File = File(backupsDir, outputFileName + ".igmoon")
            if (!fileOutput.exists()) {
                fileOutput.createNewFile()
            }
            var hasPassword: Boolean = true
            if(inputPassword.text.toString().isEmpty()){
                hasPassword = false
            }
            val backupInfo: JSONObject = JSONObject()
            backupInfo.put("backupVersion", 2)
            backupInfo.put("instamoonVersion", Constants.VERSION)
            backupInfo.put("instagramVersion", Utils.getVersionName(context))
            backupInfo.put("isInstamoon", true)
            backupInfo.put("hasPassword", hasPassword)

            val fullJson: JSONObject = JSONObject()
            fullJson.put("backupInfo", backupInfo)
            val backupContent: String = AESUtils.encryptTextWithPassword(contentFile, inputPassword.text.toString())

            fullJson.put("backupContent", backupContent)

            val state: String? = FileUtils.writeContent(fileOutput, fullJson.toString())
            if(state.equals("SUCCESS")){
                ToastUtils.showShortToast(context, "File exported in " + fileOutput.path)
            }else {
                ToastUtils.showShortToast(context, "An error occurred while exporting the backup: " + state)
            }
        }

    }
}