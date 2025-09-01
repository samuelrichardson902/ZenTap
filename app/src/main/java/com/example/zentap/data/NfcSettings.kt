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

    fun getRegisteredTags(context: Context): Set<String> {
        return getPrefs(context).getStringSet(MainViewModel.REGISTERED_TAGS_PREFS, emptySet()) ?: emptySet()
    }

    fun addRegisteredTag(context: Context, tagId: String) {
        val currentTags = getRegisteredTags(context).toMutableSet()
        currentTags.add(tagId)
        getPrefs(context).edit { putStringSet(MainViewModel.REGISTERED_TAGS_PREFS, currentTags) }
    }
}
