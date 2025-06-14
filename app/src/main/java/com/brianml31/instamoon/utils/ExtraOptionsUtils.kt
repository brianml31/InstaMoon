package com.brianml31.instamoon.utils

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

        fun hideSuggestedReels(z: Boolean): Boolean {
            if(PrefsUtils.getBoolean(PrefsUtils.arrayExtraOptionsKeys[4], false)){
                return false
            }else{
                return z
            }

        }
    }
}