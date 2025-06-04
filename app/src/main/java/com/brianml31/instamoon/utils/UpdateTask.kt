package com.brianml31.instamoon.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import org.json.JSONObject
import java.io.DataInputStream
import java.net.URL

class UpdateTask : AsyncTask<String, String, String> {
    private val context : Context
    private var progressDialog: ProgressDialog? = null

    constructor(context: Context){
        this.context = context
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Connecting...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    override fun doInBackground(vararg strings: String): String? {
        try {
            val url = URL(strings[0])
            val inputStream = url.openStream()
            val dataInputStream = DataInputStream(inputStream)
            val stringBuilder = StringBuilder()
            var readLine: String?
            while ((dataInputStream.readLine().also { readLine = it }) != null) {
                stringBuilder.append(readLine)
            }
            return stringBuilder.toString()
        } catch (e: Exception) {
            return "?"
        }
    }

    override fun onPostExecute(result: String?) {
        this.dismissProgressDialog()
        if (result != null && !result.isEmpty()) {
            if (result.equals("?")) {
                DialogUtils.showUpdateDialog(this.context, "Error", "An error occurred!", true, "")
            } else {
                try {
                    val jsonObject = JSONObject(result)
                    val versionJson = jsonObject.getString("ver1") + "." + jsonObject.getString("ver2")
                    if (versionJson == Constants.VERSION) {
                        ToastUtils.showShortToast(context, "The latest version is already installed.")
                    } else {
                        DialogUtils.showUpdateDialog(context, "Latest version", "Last version: $versionJson", false, jsonObject.getString("url"))
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun dismissProgressDialog() {
        try {
            progressDialog!!.dismiss()
        } catch (e: IllegalArgumentException) {
        }
    }
}