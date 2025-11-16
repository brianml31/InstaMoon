package com.brianml31.instamoon.utils

import java.io.File

class Constants {
    companion object {
        const val VERSION = "10.00"
        private const val BASE_URL = "OWRxuGjfMNeJcK+EODaLMqK4ICBvVNzNAkdfbmcN3Pxf5dzOyFpZAI92VajCXMbVLh+Ah12bpQsapQUChdk+ZyIayM64d9OMSIi1ef9xsyU="
        const val VERSION_CHECK = BASE_URL + "36dONL3Nn3p8ItLjGzUFXq4TITAs26Tt2eRMUUo3+Y9xP7qwtITJxY0im8j8OBzw"
        const val FONT_FACEBOOK = BASE_URL + "HFgeTX7w4kwGdrWI8oPfoQj+Y2cp88UaXcTMcoe8uxDOLBSEZojipDVL41UTBR19"
        const val FONT_IOS_18_4 = BASE_URL + "+yJJlSOlaKyGZhERztWh2qQTm2w+o4N1YrmOzHhuIS0DnuXDEstaVsaFipWAkiWg"
        const val FONT_IOS_18 = BASE_URL + "Is9jQhVfqpvp0Yyp1HdyFNbaA6S6/K3XJjlw6OZ/NW3jVL6PSGJSos+fz6vp1hDd"
        const val FONT_WHATSAPP = BASE_URL + "Nv+bRRsVn8j+oVZK0sJ0aSYxvjdFtNcdEPiBPfKnhBuHoZbLPsW5hSKZ6bH+ssmO"
        const val GITHUB_URL = "BT61H5aXEiUjmX0XmsvL/xiFj28jC5t2gXj8PxZeuuVd+X3jHwF7H4neVpPuspO1"
        private const val INSTAMOON_FOLDER = "InstaMoon"
        val FONTS_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + ".Fonts";
        val ID_NAME_MAPPING_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + "id_name_mappings"
        val BACKUPS_OUTPUT_FOLDER = INSTAMOON_FOLDER + File.separator + "Backups"
    }
}