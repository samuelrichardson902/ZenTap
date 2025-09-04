package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.zentap.ui.screens.MainScreen
import com.example.zentap.ui.theme.ZenTapTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()
    private var packageReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register receiver for app install/uninstall events
        packageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_PACKAGE_ADDED ||
                    intent?.action == Intent.ACTION_PACKAGE_REMOVED
                ) {
                    context?.let {
                        viewModel.loadAndSortApps(it.packageManager, it, forceReload = true)
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        registerReceiver(packageReceiver, filter)

        setContent {
            ZenTapTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val pm = packageManager
        // Uses caching in ViewModel (won’t reload if already loaded)
        viewModel.loadAndSortApps(pm, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        packageReceiver?.let { unregisterReceiver(it) }
        packageReceiver = null
    }

    fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}