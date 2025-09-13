package com.zentap.android.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object AppSettings {

    private const val PREFS_NAME = "app_settings"
    private const val KEY_WAIT_TIME = "key_wait_time_millis"
    private const val KEY_UNLOCK_TIME = "key_unlock_time_millis"

    private const val DEFAULT_WAIT_TIME = 30_000L
    private const val DEFAULT_UNLOCK_TIME = 60_000L

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // --- Wait Time (Initial Countdown) ---

    fun getWaitTime(context: Context): Long {
        return getPrefs(context).getLong(KEY_WAIT_TIME, DEFAULT_WAIT_TIME)
    }

    fun setWaitTime(context: Context, timeInMillis: Long) {
        getPrefs(context).edit { putLong(KEY_WAIT_TIME, timeInMillis) }
    }

    // --- Unlock Time (App Access) ---

    fun getUnlockTime(context: Context): Long {
        return getPrefs(context).getLong(KEY_UNLOCK_TIME, DEFAULT_UNLOCK_TIME)
    }

    fun setUnlockTime(context: Context, timeInMillis: Long) {
        getPrefs(context).edit { putLong(KEY_UNLOCK_TIME, timeInMillis) }
    }
}