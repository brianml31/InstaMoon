package com.brianml31.instamoon.utils

class ExtraOptionsUtils {
    companion object {
        @JvmStatic
        fun disableAds(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[0], false)
        }

        fun disableAnalytics(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[1], false)
        }

        @JvmStatic
        fun disableVideoAutoplay(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[2], false)
        }

        @JvmStatic
        fun disableDoubleTapLike(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[3], false)
        }

        @JvmStatic
        fun hideSuggestedReels(z: Boolean): Boolean {
            if(PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[4], false)){
                return false
            }else{
                return z
            }
        }
    }
}