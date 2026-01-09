package com.brianml31.instamoon.utils

import android.content.Context
import android.content.SharedPreferences
import com.brianml31.instamoon.app.AppContext

class PrefsUtils {
    companion object{
        val arrayGhostModeKeys: Array<String> = arrayOf(
            "hideSeenStories",
            "hideSeenDM",
            "hideTypingDM",
            "hideTookScreenshot",
            "hideOpenedMedia",
            "hideReplayedMedia",
            "hideSeenLiveVideos"
        )

        val arrayExtraOptionsKeys: Array<String> = arrayOf(
            "disableAds",
            "disableAnalytics",
            "disableVideoAutoplay",
            "disableDoubleTapLike",
            "hideSuggestedReels"
        )

        private fun getSharedPreferences(context: Context, ): SharedPreferences {
            return context.getSharedPreferences(context.packageName + "_brianml31", Context.MODE_PRIVATE)
        }

        fun getBoolean(key: String, defValue: Boolean): Boolean {
            return getSharedPreferences(AppContext.getContext()!!).getBoolean(key, defValue)
        }

        fun getString(key: String, defValue: String?): String? {
            return getSharedPreferences(AppContext.getContext()!!).getString(key, defValue)
        }

        fun getInt(key: String, defValue: Int): Int {
            return getSharedPreferences(AppContext.getContext()!!).getInt(key, defValue)
        }

        fun saveString(context: Context, key: String, value: String?) {
            val preferences: SharedPreferences = getSharedPreferences(context)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveInt(context: Context, key: String, value: Int) {
            val preferences: SharedPreferences = getSharedPreferences(context)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun removeString(context: Context, key: String) {
            val preferences: SharedPreferences = getSharedPreferences(context)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.remove(key)
            editor.apply()
        }

        fun loadPreferences(context: Context, options: Array<String>, keys: Array<String>): BooleanArray {
            val preferences: SharedPreferences = getSharedPreferences(context)
            val checkedItems: BooleanArray = BooleanArray(options.size)
            for (i in options.indices) {
                checkedItems[i] = preferences.getBoolean(keys[i], false)
            }
            return checkedItems
        }

        fun savePreferences(context: Context, checkedItems: BooleanArray, keys: Array<String>) {
            val preferences: SharedPreferences = getSharedPreferences(context)
            val editor: SharedPreferences.Editor = preferences.edit()
            for (i in checkedItems.indices) {
                editor.putBoolean(keys[i], checkedItems[i])
            }
            editor.apply()
        }

    }
}