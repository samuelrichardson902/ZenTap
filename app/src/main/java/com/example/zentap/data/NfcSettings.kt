package com.example.zentap.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object NfcSettings {

    private const val PREFS_NAME = "nfc_settings"
    private const val REGISTERED_TAGS_KEY = "registered_tags"

    private val gson = Gson()

    // Get all registered tags as a map: tagId -> displayName
    fun getRegisteredTags(context: Context): Map<String, String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(REGISTERED_TAGS_KEY, "{}") ?: "{}"
        return try {
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson<Map<String, String>>(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    // Save the entire map of registered tags
    private fun saveRegisteredTags(context: Context, tags: Map<String, String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(REGISTERED_TAGS_KEY, gson.toJson(tags))
        }
    }

    // Add a new tag (default displayName = tagId if not provided)
    fun registerNewTag(context: Context, tagId: String, displayName: String? = null): Boolean {
        return try {
            val tags = getRegisteredTags(context).toMutableMap()
            tags[tagId] = displayName ?: tagId
            saveRegisteredTags(context, tags)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Remove a tag by ID
    fun removeTag(context: Context, tagId: String) {
        val tags = getRegisteredTags(context).toMutableMap()
        tags.remove(tagId)
        saveRegisteredTags(context, tags)
    }

    // Rename a tag
    fun renameTag(context: Context, tagId: String, newName: String) {
        val tags = getRegisteredTags(context).toMutableMap()
        if (tags.containsKey(tagId)) {
            tags[tagId] = newName
            saveRegisteredTags(context, tags)
        }
    }

    // Check if a tag is registered
    fun isTagRegistered(context: Context, tagId: String): Boolean {
        return getRegisteredTags(context).containsKey(tagId)
    }

    // Clear all tags
    fun clearAllTags(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(REGISTERED_TAGS_KEY) }
    }
}
