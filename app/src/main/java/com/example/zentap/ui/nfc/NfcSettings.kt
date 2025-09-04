package com.example.zentap.ui.nfc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.zentap.MainViewModel

object NfcSettings {

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(MainViewModel.Companion.REGISTERED_TAG_PREF, Context.MODE_PRIVATE)
    }

    fun getRegisteredTag(context: Context): String? {
        return getPrefs(context).getString(MainViewModel.Companion.REGISTERED_TAG_PREF, null)
    }

    fun setRegisteredTag(context: Context, tagId: String) {
        getPrefs(context).edit { putString(MainViewModel.Companion.REGISTERED_TAG_PREF, tagId) }
    }
}