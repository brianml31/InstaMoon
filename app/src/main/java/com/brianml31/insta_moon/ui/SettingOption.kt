package com.brianml31.insta_moon.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingOption(
    val id: Int,
    val text: String,
    var isChecked: Boolean
) : Parcelable
