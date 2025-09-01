package com.example.zentap

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppInfo(
    val name: String,
    val packageName: String,
    val isBlocked: Boolean,
    val category: String // New property
)

data class CategorizedApps(
    val blockedApps: List<AppInfo>,
    val categories: Map<String, List<AppInfo>>
)

class MainViewModel : ViewModel() {

    private val _categorizedApps = MutableStateFlow(CategorizedApps(emptyList(), emptyMap()))
    val categorizedApps = _categorizedApps.asStateFlow()

    private val _isOverallToggleOn = MutableStateFlow(true)
    val isOverallToggleOn = _isOverallToggleOn.asStateFlow()



    // Updated map with categories
    private val targetPackageNames: Map<String, String> = mapOf(
        // --- Social Media ---
        "com.instagram.android" to "Social Media",
        "com.snapchat.android" to "Social Media",
        "com.reddit.frontpage" to "Social Media",
        "com.twitter.android" to "Social Media",
        "com.facebook.katana" to "Social Media",
        "com.pinterest" to "Social Media",
        "com.linkedin.android" to "Social Media",
        "com.whatsapp" to "Social Media",
        "com.discord" to "Social Media",
        "com.tiktok.android" to "Social Media",
        "com.zhiliaoapp.musically" to "Social Media",
        "com.ss.android.ugc.trill" to "Social Media",

        // --- Streaming & Entertainment ---
        "com.google.android.youtube" to "Streaming & Entertainment",
        "com.spotify.music" to "Streaming & Entertainment",
        "com.soundcloud.android" to "Streaming & Entertainment",
        "tv.twitch.android.app" to "Streaming & Entertainment",
        "com.netflix.mediaclient" to "Streaming & Entertainment",
        "com.disney.disneyplus" to "Streaming & Entertainment",
        "com.amazon.avod.thirdpartyclient" to "Streaming & Entertainment",
        "com.google.android.videos" to "Streaming & Entertainment",
        "com.google.android.apps.tv" to "Streaming & Entertainment",
        "com.google.android.apps.youtube.gaming" to "Streaming & Entertainment",
        "com.itv.itvx" to "Streaming & Entertainment",
        "air.ITVMobilePlayer" to "Streaming & Entertainment",
        "com.channel4.channel4" to "Streaming & Entertainment",
        "com.channel4.ondemand" to "Streaming & Entertainment",
        "bbc.mobile.iplayer.android" to "Streaming & Entertainment",
        "bbc.iplayer.android" to "Streaming & Entertainment",

        // --- AI / Assistants ---
        "com.openai.chatgpt" to "AI / Assistants",
        "com.google.android.apps.bard" to "AI / Assistants",
        "com.deepseek.chat" to "AI / Assistants",
        "com.anthropic.claude" to "AI / Assistants",
        "ai.x.grok" to "AI / Assistants",

        // --- Dating Apps ---
        "co.hinge.app" to "Dating Apps",
        "com.tinder" to "Dating Apps",

        // --- Shopping Apps ---
        "com.depop" to "Shopping",
        "com.einnovation.temu" to "Shopping",
        "fr.vinted" to "Shopping",
        "com.amazon.mShop.android.shopping" to "Shopping",
    )

    fun loadAndSortApps(pm: PackageManager, context: Context, forceReload: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_categorizedApps.value.blockedApps.isNotEmpty() && _categorizedApps.value.categories.isNotEmpty() && !forceReload) {
                return@launch
            }
            val prefs = context.getSharedPreferences(OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
            val currentState = prefs.getBoolean("is_on", true)
            _isOverallToggleOn.update { currentState }

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

    fun toggleAppBlocking(appInfo: AppInfo, isBlocked: Boolean, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBlockingState(appInfo.packageName, isBlocked, context)

            val currentApps = _categorizedApps.value.blockedApps + _categorizedApps.value.categories.values.flatten()
            val updatedList = currentApps.map {
                if (it.packageName == appInfo.packageName) {
                    it.copy(isBlocked = isBlocked)
                } else {
                    it
                }
            }
            _categorizedApps.update { categorizeAndSortApps(updatedList) }
        }
    }

    fun toggleOverallState(isEnabled: Boolean, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val prefs = context.getSharedPreferences(OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
            prefs.edit { putBoolean("is_on", isEnabled) }
            _isOverallToggleOn.update { isEnabled }
        }
    }

    private fun categorizeAndSortApps(apps: List<AppInfo>): CategorizedApps {
        val blockedApps = apps.filter { it.isBlocked }.sortedBy { it.name.lowercase() }
        val nonBlockedApps = apps.filter { !it.isBlocked }
        val categories = nonBlockedApps
            .sortedBy { it.name.lowercase() }
            .groupBy { it.category }
            .toSortedMap()

        return CategorizedApps(blockedApps, categories)
    }

    private fun saveBlockingState(packageName: String, isBlocked: Boolean, context: Context) {
        val prefs = context.getSharedPreferences(BLOCKED_APPS_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(packageName, isBlocked)
        }
    }

    companion object {
        const val OVERALL_TOGGLE_PREFS = "overall_toggle"
        const val BLOCKED_APPS_PREFS = "blocked_apps"

        const val REGISTERED_TAG_PREFS = "registered_tag_id"
    }
}