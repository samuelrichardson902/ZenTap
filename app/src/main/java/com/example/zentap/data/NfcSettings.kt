package com.example.zentap.data

import android.content.Context
import android.content.SharedPreferences
import com.example.zentap.MainViewModel
import androidx.core.content.edit

object NfcSettings {

    private const val PREFS_NAME = "nfc_settings"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getRegisteredTag(context: Context): String? {
        return getPrefs(context).getString(MainViewModel.REGISTERED_TAG_PREF, null)
    }

    fun setRegisteredTag(context: Context, tagId: String) {
        getPrefs(context).edit { putString(MainViewModel.REGISTERED_TAG_PREF, tagId) }
    }
}
