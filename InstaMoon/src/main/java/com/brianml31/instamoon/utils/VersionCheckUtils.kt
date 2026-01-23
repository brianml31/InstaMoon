package com.brianml31.instamoon.utils

import android.app.Activity
import com.winsontan520.WVersionManager


class VersionCheckUtils {
    companion object{
        @JvmStatic
        fun checkVersion(activity: Activity) {
            try {
                val wVersionManager: WVersionManager = WVersionManager(activity)
                val url: String = UrlUtils.buildUrl(UrlUtils.CHECK_VERSION)
                wVersionManager.setVersionContentUrl(url)
                wVersionManager.setUpdateNowLabel("Update Now")
                wVersionManager.setRemindMeLaterLabel("Remind me later")
                wVersionManager.setIgnoreThisVersionLabel("Cancel")
                wVersionManager.checkVersion()
            } catch (ignored: Exception) {
            }
        }
    }
}