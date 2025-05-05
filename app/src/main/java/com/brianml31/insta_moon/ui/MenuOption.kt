package com.brianml31.insta_moon.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuOption(
    val id: Int,
    val icon: String,
    val text: String
) : Parcelable
