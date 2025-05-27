package com.brianml31.insta_moon.utils

class ExtraOptionsUtils {
    companion object {
        fun disableAds(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[0], false)
        }

        fun disableAnalytics(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[1], false)
        }

        fun disableVideoAutoplay(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[2], false)
        }

        fun disableDoubleTapLike(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[3], false)
        }
    }
}