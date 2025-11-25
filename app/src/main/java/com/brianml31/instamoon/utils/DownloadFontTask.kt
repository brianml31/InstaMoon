package com.brianml31.instamoon.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadFontTask : AsyncTask<String, Int, String> {
    private var context: Context
    private var progressDialog: ProgressDialog? = null
    private var fontName: String
    private var fontFile: File? = null

    constructor(context: Context, fontName: String){
        this.context = context
        this.fontName = fontName
    }


    override fun onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog(context);
        progressDialog!!.setMessage("Downloading font...");
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog!!.setIndeterminate(false);
        progressDialog!!.setMax(100);
        progressDialog!!.setProgress(0);
        progressDialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                cancel(true);
                dialog.dismiss();
            }
        });
        progressDialog!!.setCancelable(false);
        progressDialog!!.show();
    }

    override fun doInBackground(vararg urls: String): String? {
        val urlFont = UrlUtils.buildUrl(urls[0])
        val dirFonts = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FONTS_OUTPUT_FOLDER)
        if (!dirFonts.exists()) {
            dirFonts.mkdirs()
        }
        fontFile = File(dirFonts, this.fontName)
        var connection: HttpURLConnection? = null
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        try {
            val url = URL(urlFont)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                val fileLength = connection.contentLength
                outputStream = FileOutputStream(fontFile)
                inputStream = connection.getInputStream()
                val buffer = ByteArray(4096)
                var bytesRead: Int
                var totalBytesRead: Long = 0
                while ((inputStream.read(buffer).also { bytesRead = it }) > 0) {
                    if (isCancelled) {
                        return null
                    }
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead
                    val progress = (totalBytesRead * 100 / fileLength).toInt()
                    publishProgress(progress)
                }
                val intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
                intent.setData(Uri.fromFile(fontFile))
                context.sendBroadcast(intent)
                return fontFile!!.path
            }
            return null
        } catch (e: Exception) {
            if (this.fontFile != null && fontFile!!.exists()) {
                fontFile!!.delete()
            }
            return "ERROR"
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values);
        progressDialog!!.setProgress(if (values[0] != null) values[0]!! else 0);
    }

    override fun onPostExecute(response: String?) {
        super.onPostExecute(response)
        progressDialog!!.dismiss()
        if (response != null) {
            if (response == "ERROR") {
                ToastUtils.showShortToast(this.context, "Error downloading the file")
            } else {
                PrefsUtils.saveString(this.context, "appFontPath", response)
                ToastUtils.showShortToast(this.context, "Font Applied")
                DialogUtils.showRestartAppDialog(this.context)
            }
        }
    }

    override fun onCancelled() {
        super.onCancelled()
        progressDialog!!.dismiss()
        if (this.fontFile != null && fontFile!!.exists()) {
            fontFile!!.delete()
        }
        ToastUtils.showShortToast(this.context, "Download cancelled")
    }

}