package com.brianml31.instamoon.utils

import android.os.Environment
import java.io.File

class StoragePaths {
    companion object {
        private val downloadsDir: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        private val instamoonDir: File = File(downloadsDir, "InstaMoon")
        val fontsDir: File = File(instamoonDir, ".fonts")
        val backupsDir: File = File(instamoonDir, "backups")
        val mappingsDir: File = File(instamoonDir, "mappings")
    }
}