package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class BackupUtils {
    companion object {
        fun readBackupToImport(activity: Activity, data: Uri?): String? {
            val stringBuilder = StringBuilder()
            var inputStream: InputStream? = null
            var reader: BufferedReader? = null
            try {
                inputStream = activity.contentResolver.openInputStream(data!!)
                val isr = InputStreamReader(inputStream)
                reader = BufferedReader(isr)
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                return stringBuilder.toString()
            } catch (fileNotFoundException: FileNotFoundException) {
                return null
            } catch (e: Exception) {
                return null
            } finally {
                try {
                    reader?.close()
                    inputStream?.close()
                } catch (e: IOException) {
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

        fun createInstamoonBackupJson(ctx: Context, hasPassword: Boolean, instamoonBackupContent: String, password: String): JSONObject {
            val backupInfo = JSONObject()
            backupInfo.put("backup_version", 1)
            backupInfo.put("instamoon_version", Constants.VERSION)
            backupInfo.put("instagram_version", Utils.getVersionName(ctx))
            backupInfo.put("is_instamoon", true)
            backupInfo.put("has_password", hasPassword)

            val fullJson = JSONObject()
            fullJson.put("backup_info", backupInfo)
            fullJson.put("instamoon_backup_content", AESUtils.encrypt(instamoonBackupContent, password))

            return fullJson
        }
    }
}