package com.example.zentap.data

import java.util.UUID

enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

// NEW: An enum to define the schedule's purpose
enum class ScheduleAction {
    TURN_ON,
    TURN_OFF
}

data class Schedule(
    val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val repeatDays: Set<DayOfWeek>,
    // MODIFIED: Add the action field with a default value
    val action: ScheduleAction = ScheduleAction.TURN_ON
)