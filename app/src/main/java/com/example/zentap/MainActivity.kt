package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.zentap.ui.screens.home.AppUiModel
import com.example.zentap.ui.screens.home.MainScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
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
            val categorizedApps by viewModel.categorizedApps.collectAsState()
            val isOverallToggleOn by viewModel.isOverallToggleOn.collectAsState()
            var showDialog by remember { mutableStateOf(false) }

            MainScreen(
                apps = categorizedApps,
                isOverallToggleOn = isOverallToggleOn,
                onOverallToggle = { isEnabled ->
                    if (isEnabled && !isAccessibilityServiceEnabled()) {
                        showDialog = true
                    } else {
                        viewModel.toggleOverallState(isEnabled, this)
                    }
                },
                onToggleBlock = { appUiModel, isBlocked ->
                    val appInfo = categorizedApps.blockedApps.find { it.packageName == appUiModel.packageName }
                        ?: categorizedApps.categories.values.flatten().find { it.packageName == appUiModel.packageName }
                    appInfo?.let {
                        if (isBlocked && !isAccessibilityServiceEnabled()) {
                            showDialog = true
                        } else {
                            viewModel.toggleAppBlocking(it, isBlocked, this)
                            val message =
                                if (isBlocked) "${it.name} is now blocked"
                                else "${it.name} blocking disabled"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                viewModel = viewModel
            )

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Accessibility Permission Required") },
                    text = {
                        Text("To block apps, this app needs accessibility permission. Please enable it in the settings.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            openAccessibilitySettings()
                            showDialog = false
                        }) {
                            Text("Go to Settings")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
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

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}