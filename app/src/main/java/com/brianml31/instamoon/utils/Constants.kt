package com.brianml31.instamoon.utils

import java.io.File

class Constants {
    companion object {
        const val VERSION = "8.00"
        const val VERSION_CHECK_URL = "68747470733a2f2f7261772e67697468756275736572636f6e74656e742e636f6d2f627269616e6d6c33312f496e7374614d6f6f6e2f496e7374614d6f6f6e2f56657273696f6e2e747874"
        const val GITHUB_URL = "68747470733a2f2f6769746875622e636f6d2f627269616e6d6c3331"
        const val EXTEND_SNOOZE_WARNING_DURATION = 2048
        val ID_NAME_MAPPING_OUTPUT_FOLDER = "InstaMoon"+File.separator+"id_name_mappings"
        val BACKUPS_OUTPUT_FOLDER = "InstaMoon"+File.separator+"Backups"
    }
}