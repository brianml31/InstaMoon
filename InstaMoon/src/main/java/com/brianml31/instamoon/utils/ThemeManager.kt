package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class ThemeManager {
    companion object {
        private val THEME_SYSTEM: Int = -1
        private val THEME_LIGHT: Int = 1
        private val THEME_DARK: Int = 2

        fun changeTheme(activity: Activity) {
            val currentTheme: Int = getTheme(activity)
            var message: String = "Light theme applied"
            if(currentTheme == THEME_SYSTEM || currentTheme == THEME_LIGHT){
                setTheme(activity, THEME_DARK)
                message = "Dark theme applied"
            }else{
                setTheme(activity, THEME_LIGHT)
            }
            activity.recreate()
            ToastUtils.showShortToast(activity, message)
        }

        fun isDarkModeEnabled(context: Context): Boolean {
            if (getTheme(context) == THEME_DARK) {
                return true
            }
            return false
        }

        private fun getTheme(context: Context): Int {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getInt("dark_mode_toggle_setting", THEME_SYSTEM)
        }

        private fun setTheme(context: Context, theme: Int) {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putInt("dark_mode_toggle_setting", theme)
            editor.putInt("dark_mode_toggle_override_previous_value", theme)
            editor.apply()
        }

    }
}