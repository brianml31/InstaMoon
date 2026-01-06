package com.brianml31.instamoon.app

import android.app.Application
import android.content.Context
import com.brianml31.instamoon.utils.FontUtils
import org.acra.ACRA

class AppContext {
    companion object {
        private var context: Context? = null

        fun getContext(): Context? {
            return context
        }

        fun setContext(application: Application){
            ACRA.init(application)
            context = application.applicationContext
            FontUtils.initFont()
        }
    }
}