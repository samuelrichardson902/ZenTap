package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState by viewModel.apps.collectAsState()
            var showDialog by remember { mutableStateOf(false) }

            val appUiModels = appState.map { appInfo ->
                AppUiModel(
                    name = appInfo.name,
                    packageName = appInfo.packageName,
                    icon = appInfo.icon,
                    blocked = appInfo.isBlocked
                )
            }

            MainScreen(
                apps = appUiModels,
                onToggleBlock = { appUiModel, isBlocked ->
                    val appInfo = appState.find { it.packageName == appUiModel.packageName }
                    appInfo?.let {
                        if (isBlocked && !isAccessibilityServiceEnabled()) {
                            showDialog = true
                        } else {
                            viewModel.toggleAppBlocking(it, isBlocked, this)
                            val message = if (isBlocked) "${it.name} is now blocked" else "${it.name} blocking disabled"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Accessibility Permission Required") },
                    text = { Text("To block apps, this app needs accessibility permission. Please enable it in the settings.") },
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
        viewModel.loadAndSortApps(packageManager, this)
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    var isBlocked: Boolean
)