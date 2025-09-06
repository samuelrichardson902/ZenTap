package com.example.zentap

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zentap.data.BlockedSettings
import com.example.zentap.data.NfcSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.zentap.data.targetPackageNames

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

class MainViewModel : ViewModel() {

    private val _categorizedApps = MutableStateFlow(CategorizedApps(emptyList(), emptyMap()))
    val categorizedApps = _categorizedApps.asStateFlow()


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
        _isOverallToggleOn.value = isEnabled
    }

    private val _isOverallToggleOn = MutableStateFlow(false)
    val isOverallToggleOn = _isOverallToggleOn.asStateFlow()

    fun loadOverallState(context: Context) {
        _isOverallToggleOn.value = BlockedSettings.getBlockedMode(context)
    }


    companion object {
        const val OVERALL_TOGGLE_PREFS = "overall_toggle"
        const val OVERALL_TOGGLE_KEY = "blocked_mode"
        const val BLOCKED_APPS_PREFS = "blocked_apps"
        const val REGISTERED_TAG_PREF = "registered_tag_id"
        const val TEMP_UNLOCK_PREFS = "temp_unlock"
    }
}