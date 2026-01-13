package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
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
        fun readFileFromUri(context: Context, data: Uri): String? {
            val stringBuilder: StringBuilder = StringBuilder()
            var inputStream: InputStream? = null
            var reader: BufferedReader? = null
            try {
                inputStream = context.contentResolver.openInputStream(data)
                val isr: InputStreamReader = InputStreamReader(inputStream)
                reader = BufferedReader(isr)
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                return stringBuilder.toString()
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

        fun loadMCOverridesFile(context: Context): File {
            val mobileConfigDir: File = File(context.filesDir, "mobileconfig")
            if (!mobileConfigDir.exists()) {
                mobileConfigDir.mkdirs()
            }
            val fileMCOverrides: File = File(mobileConfigDir, "mc_overrides.json")
            return fileMCOverrides
        }

        fun writeContent(file: File?, content: String): String? {
            var fileOutputStream: FileOutputStream? = null
            var osw: OutputStreamWriter? = null
            try {
                if (file != null) {
                    fileOutputStream = FileOutputStream(file)
                    osw = OutputStreamWriter(fileOutputStream)
                    osw.write(content)
                    return "SUCCESS"
                } else {
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

        fun readTextFromFile(fileInput: File?): String? {
            val stringBuilder: StringBuilder = StringBuilder()
            var fileInputStream: FileInputStream? = null
            var reader: BufferedReader? = null
            try {
                fileInputStream = FileInputStream(fileInput)
                val isr: InputStreamReader = InputStreamReader(fileInputStream)
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

        fun saveMappingFile(context: Context){
            if (!PermissionsUtils.checkPermission(context)) {
               PermissionsUtils.requestPermission(context)
            } else {
                try {
                    val idNameMappingFile: File = File(context.filesDir, "mobileconfig" + File.separator + "id_name_mapping.json")
                    if (idNameMappingFile.exists()) {
                        val contentFile: String? = readTextFromFile(idNameMappingFile)
                        if(contentFile != null){
                            val mappingsDir: File = StoragePaths.mappingsDir
                            if (!mappingsDir.exists()) {
                                mappingsDir.mkdirs()
                            }
                            val outputFile = File(mappingsDir, "id_name_mapping_" + Utils.getVersionName(context) + ".json")
                            if (!outputFile.exists()) {
                                outputFile.createNewFile()
                            }
                            val state: String? = writeContent(outputFile, contentFile)
                            if(state.equals("SUCCESS")){
                                ToastUtils.showShortToast(context, "File saved in" + outputFile.path)
                            }else{
                                ToastUtils.showShortToast(context, "Error: " + state)
                            }
                        }
                    } else {
                        ToastUtils.showShortToast(context, "The file (id_name_mapping.json) does not exist")
                    }
                } catch (e: Exception) {
                    ToastUtils.showShortToast(context, "An error occurred while save the file \"id name mapping.json\"")
                }
            }
        }

        fun importMappingFile(activity: Activity, dataUri: Uri){
            val contentFile: String? = readFileFromUri(activity, dataUri)
            if(contentFile != null){
                val mobileConfigDir: File = File(activity.filesDir, "mobileconfig")
                if (!mobileConfigDir.exists()) {
                    mobileConfigDir.mkdirs()
                }
                val idNameMappingFile: File = File(mobileConfigDir,  "id_name_mapping.json")
                if (!idNameMappingFile.exists()) {
                    idNameMappingFile.createNewFile()
                }
                val state: String? = writeContent(idNameMappingFile, contentFile)
                if(state.equals("SUCCESS")){
                    ToastUtils.showShortToast(activity, "The file was imported successfully")
                    DialogUtils.showRestartAppDialog(activity)
                }else{
                    ToastUtils.showShortToast(activity, "Error: " + state)
                }
            } else {
                ToastUtils.showShortToast(activity, "Error: Failed to read file")
            }
        }

        fun deleteMCOverrides(context: Context): String? {
            try {
                val fileMCOverrides: File? = loadMCOverridesFile(context)
                if(fileMCOverrides!!.exists()){
                    if(fileMCOverrides.delete()){
                        return "SUCCESS"
                    }else{
                        return "ERROR"
                    }
                }
                return "NO_FILE"
            } catch (e: Exception) {
                return e.message
            }
        }

    }
}