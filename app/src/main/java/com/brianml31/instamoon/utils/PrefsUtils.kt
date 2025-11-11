package com.brianml31.instamoon.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsUtils {
    companion object{
        val arrayGhostModeKeys = arrayOf("hideSeenStories", "hideSeenDM", "hideTypingDM", "hideTookScreenshot", "hideOpenedMedia", "hideReplayedMedia", "hideSeenLiveVideos")
        val arrayExtraOptionsKeys = arrayOf("disableAds", "disableAnalytics", "disableVideoAutoplay", "disableDoubleTapLike", "hideSuggestedReels")

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.packageName + "_brianml31", 0)
        }

        fun getBoolean(key: String?, defValue: Boolean): Boolean {
            return getSharedPreferences(AppContext.getContext()!!).getBoolean(key, defValue)
        }

        fun getString(key: String?, defValue: String?): String? {
            return getSharedPreferences(AppContext.getContext()!!).getString(key, defValue)
        }

        fun savePreferencesGhostMode(ctx: Context, checkedItems: BooleanArray) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            for (i in checkedItems.indices) {
                editor.putBoolean(arrayGhostModeKeys[i], checkedItems[i])
            }
            editor.apply()
        }

        fun savePreferencesExtraOptions(ctx: Context, checkedItems: BooleanArray) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            for (i in checkedItems.indices) {
                editor.putBoolean(arrayExtraOptionsKeys[i], checkedItems[i])
            }
            editor.apply()
        }

        fun loadPreferencesGhostMode(ctx: Context): BooleanArray {
            val checkedItems = booleanArrayOf(false, false, false, false, false, false, false)
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(arrayGhostModeKeys[i], false)
            }
            return checkedItems
        }

        fun loadPreferencesExtraOptions(ctx: Context): BooleanArray {
            val checkedItems = booleanArrayOf(false, false, false, false, false)
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(arrayExtraOptionsKeys[i], false)
            }
            return checkedItems
        }

        fun saveString(ctx: Context, key: String, value: String?) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun removeString(ctx: Context, key: String) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            editor.remove(key)
            editor.apply()
        }

    }
}