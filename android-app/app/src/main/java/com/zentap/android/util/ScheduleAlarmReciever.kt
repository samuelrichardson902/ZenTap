package com.zentap.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zentap.android.data.BlockedSettings
import com.zentap.android.data.ScheduleAction
import com.zentap.android.data.ScheduleSettings
import com.zentap.android.ui.screens.blocked.AppBlockerAccessibilityService
import java.util.Calendar

class ScheduleAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ScheduleDebug", "--- ALARM RECEIVED ---")

        val scheduleId = intent.getStringExtra("SCHEDULE_ID") ?: return
        val schedule = ScheduleSettings.getSchedules(context).find { it.id == scheduleId }

        if (schedule != null && schedule.isEnabled && isTodayInRepeatDays(schedule.repeatDays)) {
            // MODIFIED: Check the schedule's action
            val newState = schedule.action == ScheduleAction.TURN_ON
            Log.d("ScheduleDebug", "Conditions MET. Action: ${schedule.action}. Setting blocked mode to: $newState")
            BlockedSettings.setBlockedMode(newState, context)

            if (newState) {
                val checkIntent = Intent(AppBlockerAccessibilityService.ACTION_TRIGGER_IMMEDIATE_BLOCK_CHECK)
                context.sendBroadcast(checkIntent)
                Log.d("ScheduleDebug", "Sent immediate check broadcast to the service.")
            }
        } else {
            Log.d("ScheduleDebug", "Conditions NOT MET. Schedule null, disabled, or wrong day.")
        }

        if (schedule != null) {
            ScheduleManager.setSchedule(context, schedule)
        }
    }
    private fun isTodayInRepeatDays(repeatDays: Set<com.zentap.android.data.DayOfWeek>): Boolean {
        if (repeatDays.isEmpty()) return true // If no days specified, assume it's for today only

        val calendar = Calendar.getInstance()
        val today = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> com.zentap.android.data.DayOfWeek.MONDAY
            Calendar.TUESDAY -> com.zentap.android.data.DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> com.zentap.android.data.DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> com.zentap.android.data.DayOfWeek.THURSDAY
            Calendar.FRIDAY -> com.zentap.android.data.DayOfWeek.FRIDAY
            Calendar.SATURDAY -> com.zentap.android.data.DayOfWeek.SATURDAY
            Calendar.SUNDAY -> com.zentap.android.data.DayOfWeek.SUNDAY
            else -> null
        }
        return today in repeatDays
    }
}