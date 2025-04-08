package com.brianml31.insta_moon.utils

import android.content.Context
import android.content.SharedPreferences
import com.brianml31.insta_moon.Brian

class PrefsUtils {
    companion object{
        fun getBoolean(key: String?, defValue: Boolean): Boolean {
            return getSharedPreferences(Brian.getCtx()!!).getBoolean(key, defValue)
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.packageName + "_brian", 0)
        }

        fun savePreferencesGhostMode(ctx: Context, checkedItems: BooleanArray) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            for (i in checkedItems.indices) {
                editor.putBoolean(getKeysPreferences(i), checkedItems[i])
            }
            editor.apply()
        }

        fun savePreferencesExtraOptions(ctx: Context, checkedItems: BooleanArray) {
            val preferences = getSharedPreferences(ctx)
            val editor = preferences.edit()
            for (i in checkedItems.indices) {
                editor.putBoolean(getKeysPreferences(i+3), checkedItems[i])
            }
            editor.apply()
        }

        fun loadPreferencesGhostMode(ctx: Context, checkedItems: BooleanArray): BooleanArray {
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(getKeysPreferences(i), false)
            }
            return checkedItems
        }

        fun loadPreferencesExtraOptions(ctx: Context, checkedItems: BooleanArray): BooleanArray {
            val preferences = getSharedPreferences(ctx)
            for (i in checkedItems.indices) {
                checkedItems[i] = preferences.getBoolean(getKeysPreferences(i+3), false)
            }
            return checkedItems
        }

        fun getKeysPreferences(position: Int): String {
            val keys = arrayOf("hide_seen_stories", "hide_seen_dm","hide_seen_live_videos", "hide_ads", "disable_analytics")
            return keys[position]
        }
    }
}