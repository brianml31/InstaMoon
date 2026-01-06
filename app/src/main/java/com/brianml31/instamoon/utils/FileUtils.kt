package com.brianml31.instamoon.utils

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

        fun loadMCOverridesFile(context: Context): File? {
            try {
                val mobileConfigDir: File = File(context.filesDir, "mobileconfig")
                if (!mobileConfigDir.exists()) {
                    mobileConfigDir.mkdirs()
                }
                val fileMCOverrides: File = File(mobileConfigDir, "mc_overrides.json")
                return fileMCOverrides
            } catch (e: Exception) {
                return null
            }
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

//        fun copyStream(fileInput: File?, fileOutput: File?) {
//            try {
//                val fileInputStream = FileInputStream(fileInput)
//                val fileOutputStream = FileOutputStream(fileOutput)
//                val buffer = ByteArray(1024)
//                var bytesRead: Int
//                while ((fileInputStream.read(buffer).also { bytesRead = it }) > 0) {
//                    fileOutputStream.write(buffer, 0, bytesRead)
//                }
//                fileInputStream.close()
//                fileOutputStream.close()
//            } catch (e: Exception) {
//            }
//        }

        fun deleteMCOverrides(context: Context): String? {
            try {
                val fileMCOverrides: File? = FileUtils.loadMCOverridesFile(context)
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

//        fun saveFileIdNameMapping(context: Context) {
//            if (!PermissionsUtils.checkPermission(context)) {
//                PermissionsUtils.requestPermission(context)
//            } else {
//                try {
//                    val fileIdNameMapping = File(context.filesDir, "mobileconfig" + File.separator + "id_name_mapping.json")
//                    if (fileIdNameMapping.exists()) {
//                        val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                            Constants.ID_NAME_MAPPING_OUTPUT_FOLDER
//                        )
//                        if (!directoryOutput.exists()) {
//                            directoryOutput.mkdirs()
//                        }
//                        val fileOutput = File(directoryOutput, "id_name_mapping_" + Utils.getVersionName(context) + ".json")
//                        if (!fileOutput.exists()) {
//                            fileOutput.createNewFile()
//                        }
//                        copyStream(fileIdNameMapping, fileOutput)
//                        ToastUtils.showShortToast(context, "File saved in" + fileOutput.path)
//                    } else {
//                        ToastUtils.showShortToast(context, "The file (id_name_mapping.json) does not exist")
//                    }
//                } catch (e: Exception) {
//                    ToastUtils.showShortToast(context, "An error occurred while importing the file \"id name mapping.json\"")
//                }
//            }
//        }

    }
}