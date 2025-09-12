package com.example.zentap.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ScheduleSettings {
    private const val PREFS_NAME = "auto_lock_schedules"
    private const val SCHEDULES_KEY = "schedules_list"
    private val gson = Gson()

    fun getSchedules(context: Context): List<Schedule> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(SCHEDULES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Schedule>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveSchedules(context: Context, schedules: List<Schedule>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(schedules)
        prefs.edit {
            putString(SCHEDULES_KEY, json)
        }
    }
}