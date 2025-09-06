package com.example.zentap.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.zentap.MainViewModel

object NfcSettings {

    fun getRegisteredTag(context: Context): String? {
        return try {
            val prefs = context.getSharedPreferences(MainViewModel.REGISTERED_TAG_PREF, Context.MODE_PRIVATE)
            prefs.getString(MainViewModel.REGISTERED_TAG_PREF, null)
        } catch (e: Exception) {
            null
        }
    }

    fun setRegisteredTag(context: Context, tagId: String): Boolean {
        return try {
            val prefs = context.getSharedPreferences(MainViewModel.REGISTERED_TAG_PREF, Context.MODE_PRIVATE)
            prefs.edit { putString(MainViewModel.REGISTERED_TAG_PREF, tagId) }
            true
        } catch (e: Exception) {
            false
        }
    }
}