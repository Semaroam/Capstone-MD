package com.dicoding.semaroam.utils

import android.content.Context

class PreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    fun setLoggedInStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", status)
        editor.apply()
    }

    fun getLoggedInStatus(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}