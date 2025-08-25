package com.example.zentap

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    private val priorityPackageNames = setOf(
        "com.instagram.android", "com.snapchat.android", "com.reddit.frontpage",
        "com.twitter.android", "com.facebook.katana", "com.tiktok.android",
        "com.zhiliaoapp.musically", "com.google.android.youtube", "com.pinterest",
        "com.linkedin.android"
    )

    fun loadAndSortApps(pm: PackageManager, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedApps = getInstalledApps(pm, context)
            val sortedApps = sortApps(loadedApps)
            _apps.update { sortedApps }
        }
    }

    private fun getInstalledApps(pm: PackageManager, context: Context): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        val prefs = context.getSharedPreferences("blocked_apps", Context.MODE_PRIVATE)
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfoList = pm.queryIntentActivities(intent, 0)

        for (resolveInfo in resolveInfoList) {
            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo

            if (appInfo.packageName != context.packageName && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                val appName = appInfo.loadLabel(pm).toString()
                val appIcon = appInfo.loadIcon(pm)
                val packageName = appInfo.packageName
                val isBlocked = prefs.getBoolean(packageName, false)
                apps.add(AppInfo(appName, packageName, appIcon, isBlocked))
            }
        }
        return apps
    }

    fun toggleAppBlocking(appInfo: AppInfo, isBlocked: Boolean, context: Context) {
        appInfo.isBlocked = isBlocked
        saveBlockingState(appInfo.packageName, isBlocked, context)

        val currentList = _apps.value.toMutableList()
        val index = currentList.indexOfFirst { it.packageName == appInfo.packageName }
        if (index != -1) {
            currentList[index] = appInfo
        }
        val sortedList = sortApps(currentList)
        _apps.update { sortedList }
    }

    private fun sortApps(apps: List<AppInfo>): List<AppInfo> {
        return apps.sortedWith(compareBy<AppInfo> {
            if (it.isBlocked) 0 else 1
        }.thenBy {
            if (it.packageName in priorityPackageNames) 0 else 1
        }.thenBy {
            it.name.lowercase()
        })
    }

    private fun saveBlockingState(packageName: String, isBlocked: Boolean, context: Context) {
        val prefs = context.getSharedPreferences("blocked_apps", Context.MODE_PRIVATE)
        prefs.edit().putBoolean(packageName, isBlocked).apply()
    }
}
