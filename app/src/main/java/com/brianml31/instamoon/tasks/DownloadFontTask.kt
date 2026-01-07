package com.brianml31.instamoon.tasks

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import com.brianml31.instamoon.utils.Constants
import com.brianml31.instamoon.utils.ToastUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadFontTask : AsyncTask<String, Int, DownloadResult> {
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
        progressDialog!!.setMessage("DOWNLOADING...");
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

    override fun doInBackground(vararg urls: String): DownloadResult {
        val urlFont: String = urls[0]
        val dirFonts: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.FONTS_OUTPUT_FOLDER)
        if (!dirFonts.exists()) {
            dirFonts.mkdirs()
        }
        fontFile = File(dirFonts, this.fontName)
        var connection: HttpURLConnection? = null
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        try {
            val url: URL = URL(urlFont)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if(connection.responseCode != HttpURLConnection.HTTP_OK){
                return DownloadResult.HTTP_ERROR
            }
            val fileLength: Int = connection.contentLength
            outputStream = FileOutputStream(fontFile)
            inputStream = connection.inputStream
            val buffer: ByteArray = ByteArray(4096)
            var bytesRead: Int
            var totalBytesRead: Long = 0
            while ((inputStream.read(buffer).also { bytesRead = it }) > 0) {
                if (isCancelled) {
                    return DownloadResult.CANCELLED
                }
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                val progress: Int = (totalBytesRead * 100 / fileLength).toInt()
                publishProgress(progress)
            }
            val intent: Intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
            intent.data = Uri.fromFile(fontFile)
            context.sendBroadcast(intent)
            return DownloadResult.SUCCESS
        } catch (e: Exception) {
            if (this.fontFile != null && fontFile!!.exists()) {
                fontFile!!.delete()
            }
            return DownloadResult.ERROR
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
                if (connection != null) {
                    connection.disconnect()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values);
        progressDialog!!.setProgress(if (values[0] != null) values[0]!! else 0);
    }

    override fun onPostExecute(result: DownloadResult) {
        super.onPostExecute(result)
        progressDialog!!.dismiss()
        when (result) {
            DownloadResult.SUCCESS -> {
                ToastUtils.showShortToast(this.context, "Font \""+ fontName +"\" downloaded successfully")
            }
            DownloadResult.HTTP_ERROR -> {
                ToastUtils.showShortToast(this.context, "Network error while downloading font")
            }
            DownloadResult.ERROR -> {
                ToastUtils.showShortToast(this.context, "Error downloading font")
            }
            DownloadResult.CANCELLED -> {

            }
        }

    }

    override fun onCancelled() {
        super.onCancelled()
        if (this.fontFile != null && fontFile!!.exists()) {
            fontFile!!.delete()
        }
        ToastUtils.showShortToast(this.context, "Download cancelled")
    }

}