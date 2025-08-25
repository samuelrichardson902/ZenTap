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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        appAdapter = AppAdapter { appInfo, isBlocked ->
            toggleAppBlocking(appInfo, isBlocked)
        }
        recyclerView.adapter = appAdapter
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

        appAdapter.submitList(loadedApps)
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

    // --- START OF FIXED SECTION ---
    private fun toggleAppBlocking(appInfo: AppInfo, isBlocked: Boolean) {
        if (isBlocked && !isAccessibilityServiceEnabled()) {
            showAccessibilityPermissionDialog()
            // When user returns from settings, onResume() will reload and fix the UI state.
            return
        }

        // Update the state in the object and save it to SharedPreferences
        appInfo.isBlocked = isBlocked
        saveBlockingState(appInfo.packageName, isBlocked)

        val message = if (isBlocked) "${appInfo.name} is now blocked" else "${appInfo.name} blocking disabled"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // The sorting logic is now here, directly inside the function.
        // This resolves the "unresolved reference" errors.
        val currentList = appAdapter.currentList.toMutableList()
        currentList.sortWith(compareBy<AppInfo> {
            if (it.isBlocked) 0 else 1
        }.thenBy {
            if (it.packageName in priorityPackageNames) 0 else 1
        }.thenBy {
            it.name.lowercase()
        })

        appAdapter.submitList(currentList)
    }
    // --- END OF FIXED SECTION ---

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    private fun showAccessibilityPermissionDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
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

// Ensure isBlocked is a 'var' so you can modify it directly.
data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    var isBlocked: Boolean
)