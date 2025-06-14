package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class FileUtils {
    companion object{
        fun exportJsonBackup(ctx: Context) {
            if (!PermissionsUtils.checkPermission(ctx)) {
                PermissionsUtils.requestPermission(ctx)
            } else {
                try {
                    val mobileConfigDir = File(ctx.filesDir, "mobileconfig")
                    if (!mobileConfigDir.exists()) {
                        mobileConfigDir.mkdirs()
                    }
                    val fileMCOverrides = File(mobileConfigDir, "mc_overrides.json")
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

        fun loadMCOverridesFile(activity: Activity): File? {
            try {
                val mobileConfigDir = File(activity.filesDir, "mobileconfig")
                if (!mobileConfigDir.exists()) {
                    mobileConfigDir.mkdirs()
                }
                val fileMCOverrides = File(mobileConfigDir, "mc_overrides.json")
                if (!fileMCOverrides.exists()) {
                    fileMCOverrides.createNewFile()
                }
                return fileMCOverrides
            } catch (e: Exception) {
                return null
            }
        }

        fun writeContent(file: File?, content: String?): String? {
            var fileOutputStream: FileOutputStream? = null
            var osw: OutputStreamWriter? = null
            try {
                if(file!=null){
                    fileOutputStream = FileOutputStream(file)
                    osw = OutputStreamWriter(fileOutputStream)
                    osw.write(content!!)
                    return "SUCCESS"
                }else{
                    return "Failed to load the output file"
                }
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

        fun readFile(fileInput: File): String? {
            val stringBuilder = StringBuilder()
            var fileInputStream: FileInputStream? = null
            var reader: BufferedReader? = null
            try {
                fileInputStream = FileInputStream(fileInput)
                val isr = InputStreamReader(fileInputStream)
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
                    fileInputStream?.close()
                } catch (e: IOException) {
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