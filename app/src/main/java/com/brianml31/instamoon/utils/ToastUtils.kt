package com.brianml31.instamoon.utils

import android.content.Context
import android.widget.Toast

class ToastUtils {
    companion object{

        fun showShortToast(context: Context, message: String) {
            showToast(context, message, Toast.LENGTH_SHORT)
        }

        fun showLongToast(context: Context, message: String) {
            showToast(context, message, Toast.LENGTH_LONG)
        }

        private fun showToast(context: Context, message: String, duration: Int) {
            Toast.makeText(context, message, duration).show()
        }
    }
}