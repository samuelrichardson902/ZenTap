package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.edit
import com.example.zentap.ui.screens.home.AppUiModel
import com.example.zentap.ui.screens.home.MainScreen
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : AppCompatActivity() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    private val apps = _apps.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState by apps.collectAsState()
            val appUiModels = appState.map { appInfo ->
                AppUiModel(
                    name = appInfo.name,
                    packageName = appInfo.packageName,
                    icon = rememberDrawablePainter(drawable = appInfo.icon),
                    blocked = appInfo.isBlocked
                )
            }

            MainScreen(
                apps = appUiModels,
                onToggleBlock = { appUiModel, isBlocked ->
                    val appInfo = apps.value.find { it.packageName == appUiModel.packageName }
                    appInfo?.let {
                        toggleAppBlocking(it, isBlocked)
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadAndSortApps()
    }

    private fun loadAndSortApps() {
        val loadedApps = getInstalledApps()

        loadedApps.sortWith(compareBy<AppInfo> {
            if (it.isBlocked) 0 else 1
        }.thenBy {
            if (it.packageName in priorityPackageNames) 0 else 1
        }.thenBy {
            it.name.lowercase()
        })

        _apps.update { loadedApps }
    }

    private val priorityPackageNames = setOf(
        "com.instagram.android", "com.snapchat.android", "com.reddit.frontpage",
        "com.twitter.android", "com.facebook.katana", "com.tiktok.android",
        "com.zhiliaoapp.musically", "com.google.android.youtube", "com.pinterest",
        "com.linkedin.android"
    )

    private fun getInstalledApps(): MutableList<AppInfo> {
        val pm: PackageManager = packageManager
        val apps = mutableListOf<AppInfo>()
        val prefs = getSharedPreferences("blocked_apps", Context.MODE_PRIVATE)
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfoList = pm.queryIntentActivities(intent, 0)

        for (resolveInfo in resolveInfoList) {
            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo

            if (appInfo.packageName != this.packageName && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                val appName = appInfo.loadLabel(pm).toString()
                val appIcon = appInfo.loadIcon(pm)
                val packageName = appInfo.packageName
                val isBlocked = prefs.getBoolean(packageName, false)
                apps.add(AppInfo(appName, packageName, appIcon, isBlocked))
            }
        }
        return apps
    }

    private fun toggleAppBlocking(appInfo: AppInfo, isBlocked: Boolean) {
        if (isBlocked && !isAccessibilityServiceEnabled()) {
            showAccessibilityPermissionDialog()
            return
        }

        appInfo.isBlocked = isBlocked
        saveBlockingState(appInfo.packageName, isBlocked)

        val message = if (isBlocked) "${appInfo.name} is now blocked" else "${appInfo.name} blocking disabled"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val currentList = _apps.value.toMutableList()
        val index = currentList.indexOfFirst { it.packageName == appInfo.packageName }
        if (index != -1) {
            currentList[index] = appInfo
        }
        currentList.sortWith(compareBy<AppInfo> {
            if (it.isBlocked) 0 else 1
        }.thenBy {
            if (it.packageName in priorityPackageNames) 0 else 1
        }.thenBy {
            it.name.lowercase()
        })
        _apps.update { currentList }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    private fun showAccessibilityPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Accessibility Permission Required")
            .setMessage("To block apps, this app needs accessibility permission. Please enable it in the settings.")
            .setPositiveButton("Go to Settings") { _, _ -> openAccessibilitySettings() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun saveBlockingState(packageName: String, isBlocked: Boolean) {
        val prefs = getSharedPreferences("blocked_apps", Context.MODE_PRIVATE)
        prefs.edit { putBoolean(packageName, isBlocked) }
    }
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    var isBlocked: Boolean
)