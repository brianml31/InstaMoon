package com.brianml31.insta_moon.utils

import android.content.Context
import android.content.SharedPreferences
import com.brianml31.insta_moon.Brian

class PrefsUtils {
    companion object{
        val arrayGhostModeKeys = arrayOf("hide_seen_stories", "hide_seen_dm","hide_seen_live_videos")
        val arrayExtraOptionsKeys = arrayOf("hide_ads", "disable_analytics")

        fun getBoolean(key: String?, defValue: Boolean): Boolean {
            return getSharedPreferences(Brian.getCtx()!!).getBoolean(key, defValue)
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.packageName + "_brianml31", 0)
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
            val checkedItems = booleanArrayOf(false, false, false)
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(arrayGhostModeKeys[i], false)
            }
            return checkedItems
        }

        fun loadPreferencesExtraOptions(ctx: Context): BooleanArray {
            val checkedItems = booleanArrayOf(false, false)
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(arrayExtraOptionsKeys[i], false)
            }
            return checkedItems
        }
    }
}