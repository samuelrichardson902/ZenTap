package com.zentap.android

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zentap.android.data.BlockedSettings
import com.zentap.android.data.NfcSettings
import com.zentap.android.data.targetPackageNames
import com.zentap.android.ui.screens.blocked.AppBlockerAccessibilityService
import com.zentap.android.util.ToastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.zentap.android.data.Schedule
import com.zentap.android.data.ScheduleSettings
import com.zentap.android.util.ScheduleManager

data class AppInfo(
    val name: String,
    val packageName: String,
    val isBlocked: Boolean,
    val category: String
)

data class CategorizedApps(
    val blockedApps: List<AppInfo>,
    val categories: Map<String, List<AppInfo>>
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _categorizedApps = MutableStateFlow(CategorizedApps(emptyList(), emptyMap()))
    val categorizedApps = _categorizedApps.asStateFlow()
    private var strictModeActivationJob: Job? = null

    private val _strictModeTimeLeft = MutableStateFlow(0L)
    val strictModeTimeLeft = _strictModeTimeLeft.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val expiration = BlockedSettings.getStrictUnlockExpiration(getApplication())
                val currentTime = System.currentTimeMillis()
                val timeLeftMillis = expiration - currentTime

                if (timeLeftMillis > 0) {
                    _strictModeTimeLeft.value = timeLeftMillis / 1000
                } else {
                    _strictModeTimeLeft.value = 0L
                }
                delay(1000)
            }
        }
    }

    fun loadAndSortApps(pm: PackageManager, context: Context, forceReload: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_categorizedApps.value.blockedApps.isNotEmpty() &&
                _categorizedApps.value.categories.isNotEmpty() &&
                !forceReload) {
                return@launch
            }

            val loadedApps = getInstalledApps(pm, context)
            val categorized = categorizeAndSortApps(loadedApps)
            _categorizedApps.update { categorized }
        }
    }

    private fun getInstalledApps(pm: PackageManager, context: Context): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        val prefs = context.getSharedPreferences(BLOCKED_APPS_PREFS, Context.MODE_PRIVATE)

        targetPackageNames.forEach { (packageName, category) ->
            try {
                val appInfo: ApplicationInfo = pm.getApplicationInfo(packageName, 0)
                val appName = appInfo.loadLabel(pm).toString()
                val isBlocked = prefs.getBoolean(packageName, false)
                apps.add(AppInfo(appName, packageName, isBlocked, category))
            } catch (_: PackageManager.NameNotFoundException) {
                // App not installed
            }
        }
        return apps
    }

    private fun categorizeAndSortApps(apps: List<AppInfo>): CategorizedApps {
        val blockedApps = apps.filter { it.isBlocked }
        val nonBlockedApps = apps.filter { !it.isBlocked }
        val categories = nonBlockedApps
            .groupBy { it.category }
            .toSortedMap()

        return CategorizedApps(blockedApps, categories)
    }

    fun toggleAppBlocking(app: AppInfo, isBlocked: Boolean, context: Context) {
        BlockedSettings.setAppBlockingState(app.packageName, isBlocked, context)
        loadAndSortApps(context.packageManager, context, forceReload = true)
    }

    fun toggleOverallState(isEnabled: Boolean, context: Context) {
        BlockedSettings.setBlockedMode(isEnabled, context)
        loadOverallState(context)
        if (isEnabled) {
            context.sendBroadcast(Intent(AppBlockerAccessibilityService.ACTION_CANCEL_REBLOCK))
        }
    }

    private val _isOverallToggleOn = MutableStateFlow(false)
    val isOverallToggleOn = _isOverallToggleOn.asStateFlow()

    fun loadOverallState(context: Context) {
        _isOverallToggleOn.value = BlockedSettings.getBlockedMode(context)
    }

    private val _blockingModeType = MutableStateFlow("Normal")
    val blockingModeType = _blockingModeType.asStateFlow()

    fun loadBlockingModeType(context: Context) {
        _blockingModeType.value = BlockedSettings.getBlockingModeType(context)
    }

    fun toggleBlockingModeType(context: Context) {
        strictModeActivationJob?.cancel()

        val currentMode = _blockingModeType.value
        val newMode = if (currentMode == "Normal") "Strict" else "Normal"
        BlockedSettings.setBlockingModeType(newMode, context)
        loadBlockingModeType(context)

        if (newMode == "Strict") {
            // BUG FIX 1: Clear any previous unlock timer to prevent incorrect countdown display.
            BlockedSettings.setStrictUnlockExpiration(0L, context)

            ToastManager.showToast(context, "Strict Mode on. Blocker activating in 15 seconds.")
            strictModeActivationJob = viewModelScope.launch {
                delay(15000)
                if (BlockedSettings.getBlockingModeType(context) == "Strict") {
                    toggleOverallState(true, context)
                }
            }
        } else {
            context.sendBroadcast(Intent(AppBlockerAccessibilityService.ACTION_CANCEL_REBLOCK))
        }
    }

    private val _strictUnlockDurationMinutes = MutableStateFlow(15)
    val strictUnlockDurationMinutes = _strictUnlockDurationMinutes.asStateFlow()

    fun loadStrictUnlockDuration(context: Context) {
        val durationMs = BlockedSettings.getStrictUnlockDuration(context)
        _strictUnlockDurationMinutes.value = (durationMs / 60000).toInt()
    }

    fun setStrictUnlockDuration(durationMinutes: Int, context: Context) {
        val minutes = if (durationMinutes < 0) 0 else durationMinutes
        _strictUnlockDurationMinutes.value = minutes
        val durationMs = minutes * 60000L
        BlockedSettings.setStrictUnlockDuration(durationMs, context)
    }

    // BUG FIX 2: New function to manually refresh the countdown state.
    fun refreshCountdown(context: Context) {
        val expiration = BlockedSettings.getStrictUnlockExpiration(context)
        val currentTime = System.currentTimeMillis()
        val timeLeftMillis = expiration - currentTime

        _strictModeTimeLeft.value = if (timeLeftMillis > 0) {
            timeLeftMillis / 1000
        } else {
            0L
        }
    }

    private val _registeredTags = MutableStateFlow<Map<String, String>>(emptyMap())
    val registeredTags = _registeredTags.asStateFlow()

    fun loadRegisteredTags(context: Context) {
        _registeredTags.value = NfcSettings.getRegisteredTags(context)
    }

    fun registerTag(context: Context, tagId: String): Boolean {
        val registeredTags = NfcSettings.getRegisteredTags(context)
        return if (registeredTags.containsKey(tagId)) {
            false
        } else {
            NfcSettings.registerNewTag(context, tagId)
            loadRegisteredTags(context)
            true
        }
    }

    fun removeTag(context: Context, tagId: String) {
        NfcSettings.removeTag(context, tagId)
        loadRegisteredTags(context)
    }

    fun renameTag(context: Context, tagId: String, newName: String) {
        NfcSettings.renameTag(context, tagId, newName)
        loadRegisteredTags(context)
    }

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()

    fun loadSchedules(context: Context) {
        _schedules.value = ScheduleSettings.getSchedules(context)
    }

    fun addSchedule(context: Context, schedule: Schedule) {
        val currentSchedules = _schedules.value.toMutableList()
        currentSchedules.add(schedule)
        ScheduleSettings.saveSchedules(context, currentSchedules)
        if (schedule.isEnabled) {
            ScheduleManager.setSchedule(context, schedule)
        }
        loadSchedules(context)
    }

    fun updateSchedule(context: Context, schedule: Schedule) {
        val currentSchedules = _schedules.value.toMutableList()
        val index = currentSchedules.indexOfFirst { it.id == schedule.id }
        if (index != -1) {
            val oldSchedule = currentSchedules[index]
            currentSchedules[index] = schedule
            ScheduleSettings.saveSchedules(context, currentSchedules)

            // Cancel the old alarm and set a new one if enabled
            ScheduleManager.cancelSchedule(context, oldSchedule)
            if (schedule.isEnabled) {
                ScheduleManager.setSchedule(context, schedule)
            }
            loadSchedules(context)
        }
    }

    fun removeSchedule(context: Context, schedule: Schedule) {
        val currentSchedules = _schedules.value.toMutableList()
        if (currentSchedules.remove(schedule)) {
            ScheduleSettings.saveSchedules(context, currentSchedules)
            ScheduleManager.cancelSchedule(context, schedule) // Always cancel when removing
            loadSchedules(context)
        }
    }

    fun toggleSchedule(context: Context, schedule: Schedule, isEnabled: Boolean) {
        val updatedSchedule = schedule.copy(isEnabled = isEnabled)
        updateSchedule(context, updatedSchedule)
    }


    companion object {
        const val OVERALL_TOGGLE_PREFS = "overall_toggle"
        const val OVERALL_TOGGLE_KEY = "blocked_mode"
        const val BLOCKED_APPS_PREFS = "blocked_apps"
        const val REGISTERED_TAG_PREF = "registered_tag_id"
        const val TEMP_UNLOCK_PREFS = "temp_unlock"
    }
}