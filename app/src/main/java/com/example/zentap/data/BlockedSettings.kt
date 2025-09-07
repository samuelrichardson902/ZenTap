package com.example.zentap.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.zentap.MainViewModel


object BlockedSettings {


    fun getBlockedMode(context: Context): Boolean {
        return try {
            val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
            prefs.getBoolean("blocked_mode", false)
        } catch (e: Exception) {
            false
        }
    }

    fun setBlockedMode(isEnabled: Boolean, context: Context): Boolean {
        return try {
            val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
            prefs.edit {
                putBoolean(MainViewModel.OVERALL_TOGGLE_KEY, isEnabled)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun setAppBlockingState(packageName: String, isBlocked: Boolean, context: Context) {
        val prefs = context.getSharedPreferences(MainViewModel.BLOCKED_APPS_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(packageName, isBlocked)
        }
    }

    fun getAppBlockedState(packageName: String, context: Context): Boolean {
        return try {
            val prefs = context.getSharedPreferences(MainViewModel.BLOCKED_APPS_PREFS, MODE_PRIVATE)
            return prefs.getBoolean(packageName, false)
        } catch (e: Exception) {
            false
        }
    }

    fun getTempUnlockExpiration(packageName: String, context: Context): Long {
        val prefs = context.getSharedPreferences(MainViewModel.TEMP_UNLOCK_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(packageName, 0L)
    }

    fun setTempUnlockExpiration(packageName: String, expirationTime: Long, context: Context) {
        val prefs = context.getSharedPreferences(MainViewModel.TEMP_UNLOCK_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(packageName, expirationTime)
        }
    }
}