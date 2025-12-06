package com.brianml31.instamoon.utils

import java.io.File

class Constants {
    companion object {
        const val VERSION = "10.10"
        const val GITHUB_URL = "BT61H5aXEiUjmX0XmsvL/xiFj28jC5t2gXj8PxZeuuVd+X3jHwF7H4neVpPuspO1"
        private const val INSTAMOON_FOLDER = "InstaMoon"
        val FONTS_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + ".Fonts";
        val ID_NAME_MAPPING_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + "id_name_mappings"
        val BACKUPS_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + "Backups"
    }
}