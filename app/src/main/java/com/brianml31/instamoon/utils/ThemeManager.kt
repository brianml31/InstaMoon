package com.brianml31.instamoon.utils

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.widget.Toast


class ThemeManager {
    companion object {
        private val THEME_SYSTEM: Int = -1
        private val THEME_LIGHT: Int = 1
        private val THEME_DARK: Int = 2

        fun changeTheme(activity: Activity) {
            val currentTheme = ThemeManager.getTheme(activity)
            var nextTheme = THEME_DARK
            var message = "Dark Theme Applied"
            if (currentTheme == THEME_DARK) {
                nextTheme = THEME_LIGHT
                message = "Light Theme Applied"
            } else if (currentTheme == THEME_LIGHT) {
                nextTheme = THEME_DARK
                message = "Dark Theme Applied"
                if (Build.VERSION.SDK_INT >= 28) {
                    nextTheme = THEME_SYSTEM
                    message = "System Theme Applied"
                }
            } else if (currentTheme == THEME_SYSTEM) {
                nextTheme = THEME_DARK
                message = "Dark Theme Applied"
            }
            setTheme(activity, nextTheme)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            activity.recreate()
        }

        fun getThemeString(activity: Activity): String {
            if(getTheme(activity) == THEME_SYSTEM){
                return "System"
            }else if(getTheme(activity) == THEME_LIGHT){
                return "Light"
            }else{
                return "Dark"
            }
        }

        private fun getTheme(activity: Activity): Int {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            return prefs.getInt("dark_mode_toggle_setting", THEME_SYSTEM)
        }

        private fun setTheme(activity: Activity, theme: Int) {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putInt("dark_mode_toggle_setting", theme)
            editor.putInt("dark_mode_toggle_override_previous_value", theme)
            editor.apply()
        }

    }
}