package com.brianml31.insta_moon.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Environment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class FileUtils {
    companion object{
        fun exportBackup(ctx: Context) {
            if (!PermissionsUtils.checkPermission(ctx)) {
                PermissionsUtils.requestPermission(ctx)
            } else {
                try {
                    val fileMCOverrides = File(ctx.filesDir, "mobileconfig" + File.separator + "mc_overrides.json")
                    if (fileMCOverrides.exists()) {
                        DialogUtils.showFileNameDialog(ctx, fileMCOverrides)
                    } else {
                        ToastUtils.showShortToast(ctx, "There is no configuration file to export")
                    }
                } catch (e: Exception) {
                    ToastUtils.showShortToast(ctx, "Error: Could not export developer mode settings")
                }
            }
        }

        fun importJsonBackup(activity: Activity, uri: Uri?){
            val contentBackup = readBackupFile(activity, uri)
            if(contentBackup!=null){
                val state = writeBackupContent(activity, contentBackup, false)
                if(state.equals("SUCCESS")){
                    ToastUtils.showShortToast(activity, "The backup was imported successfully")
                    DialogUtils.showRestartAppDialog(activity)
                }else{
                    ToastUtils.showShortToast(activity, "Error: " + state)
                }
            } else {
                ToastUtils.showShortToast(activity, "Failed to read backup file")
            }
        }

        fun importIbackupBackup(activity: Activity, uri: Uri?){
            val contentBackup = readBackupFile(activity, uri)
            if(contentBackup!=null){
                val state = writeBackupContent(activity, contentBackup, true)
                if(state.equals("SUCCESS")){
                    ToastUtils.showShortToast(activity, "The backup was imported successfully")
                    DialogUtils.showRestartAppDialog(activity)
                }else{
                    ToastUtils.showShortToast(activity, "Error: " + state)
                }
            } else {
                ToastUtils.showShortToast(activity, "Failed to read backup file")
            }
        }

        private fun readBackupFile(activity: Activity, uri: Uri?): String? {
            val stringBuilder = StringBuilder()
            var inputStream: InputStream? = null
            var reader: BufferedReader? = null
            try {
                inputStream = activity.contentResolver.openInputStream(uri!!)
                reader = BufferedReader(InputStreamReader(inputStream))
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

        private fun writeBackupContent(activity: Activity, contentBackup: String, isIbackup: Boolean): String? {
            val fileMCOverrides = File(activity.filesDir, "mobileconfig" + File.separator + "mc_overrides.json")
            var fileOutputStream: FileOutputStream? = null
            var osw: OutputStreamWriter? = null
            try {
                if (!fileMCOverrides.exists()) {
                    fileMCOverrides.createNewFile()
                }
                fileOutputStream = FileOutputStream(fileMCOverrides)
                osw = OutputStreamWriter(fileOutputStream)
                if(isIbackup) {
                    val jsonBackupObject = JSONObject(contentBackup)
                    if (jsonBackupObject.has("backup")) {
                        osw.write(jsonBackupObject.getJSONObject("backup").toString())
                    } else {
                        return "Incompatible backup"
                    }
                }else{
                    osw.write(contentBackup)
                }
                return "SUCCESS"
            } catch (e: Exception) {
                return e.message
            } finally {
                try {
                    osw?.close()
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    return e.message
                }
            }

        }

        fun copyStream(fileInput: File?, fileOutput: File?) {
            try {
                val fileInputStream = FileInputStream(fileInput)
                val fileOutputStream = FileOutputStream(fileOutput)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while ((fileInputStream.read(buffer).also { bytesRead = it }) > 0) {
                    fileOutputStream.write(buffer, 0, bytesRead)
                }
                fileInputStream.close()
                fileOutputStream.close()
            } catch (e: Exception) {
            }
        }

        fun deleteMCOverrides(ctx: Context): Boolean {
            try {
                val file_mc_overrides = File(ctx.filesDir, "mobileconfig" + File.separator + "mc_overrides.json")
                if (file_mc_overrides.exists()) {
                    return file_mc_overrides.delete()
                }
                return false
            } catch (e: Exception) {
                return false
            }
        }

        fun saveFileIdNameMapping(ctx: Context) {
            if (!PermissionsUtils.checkPermission(ctx)) {
                PermissionsUtils.requestPermission(ctx)
            } else {
                try {
                    val fileIdNameMapping = File(ctx.filesDir, "mobileconfig" + File.separator + "id_name_mapping.json")
                    if (fileIdNameMapping.exists()) {
                        val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.ID_NAME_MAPPING_OUTPUT_FOLDER)
                        if (!directoryOutput.exists()) {
                            directoryOutput.mkdirs()
                        }
                        val fileOutput = File(directoryOutput, "id_name_mapping_" + Utils.getVersionName(ctx) + ".json")
                        if (!fileOutput.exists()) {
                            fileOutput.createNewFile()
                        }
                        copyStream(fileIdNameMapping, fileOutput)
                        ToastUtils.showShortToast(ctx, "File saved in" + fileOutput.path)
                    } else {
                        ToastUtils.showShortToast(ctx, "The file (id_name_mapping.json) does not exist")
                    }
                } catch (e: Exception) {
                    ToastUtils.showShortToast(ctx, "An error occurred while importing the file \"id name mapping.json\"")
                }
            }
        }

    }
}