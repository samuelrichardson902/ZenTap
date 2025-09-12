package com.example.zentap.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.zentap.MainViewModel


object BlockedSettings {

    const val BLOCKING_MODE_TYPE_KEY = "blocking_mode_type"
    const val STRICT_UNLOCK_DURATION_KEY = "strict_unlock_duration"
    const val STRICT_UNLOCK_EXPIRATION_KEY = "strict_unlock_expiration" // Re-added

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

    fun setBlockingModeType(mode: String, context: Context) {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putString(BLOCKING_MODE_TYPE_KEY, mode)
        }
    }

    fun getBlockingModeType(context: Context): String {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(BLOCKING_MODE_TYPE_KEY, "Normal") ?: "Normal"
    }

    fun setStrictUnlockDuration(durationMs: Long, context: Context) {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(STRICT_UNLOCK_DURATION_KEY, durationMs)
        }
    }

    fun getStrictUnlockDuration(context: Context): Long {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(STRICT_UNLOCK_DURATION_KEY, 900000L)
    }

    // --- THIS FUNCTION WAS MISSING ---
    fun getStrictUnlockExpiration(context: Context): Long {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(STRICT_UNLOCK_EXPIRATION_KEY, 0L)
    }

    // --- THIS FUNCTION IS NEEDED TO SET THE EXPIRATION ---
    fun setStrictUnlockExpiration(expirationTime: Long, context: Context) {
        val prefs = context.getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(STRICT_UNLOCK_EXPIRATION_KEY, expirationTime)
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