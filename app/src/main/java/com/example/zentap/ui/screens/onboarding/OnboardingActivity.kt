package com.example.zentap.ui.screens.onboarding

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.zentap.MainActivity
import com.example.zentap.ui.nfc.NfcSettings
import androidx.core.content.edit
import com.example.zentap.MainViewModel

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // clear preferences on first launch of the app
        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("is_first_launch", true)

        if (isFirstLaunch) {
            clearAllSharedPreferences()
            prefs.edit {
                putBoolean("is_first_launch", false)
            }
        }

        setContent {
            OnboardingPermsScreen(onGrantPermission = {
                openAccessibilitySettings()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAccessibilityServiceEnabled()) {
            if (NfcSettings.getRegisteredTag(this) == null) {
                navigateToOnboardingTagActivity()
            } else {
                navigateToMainActivity()
            }
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
            AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun navigateToOnboardingTagActivity() {
        val intent = Intent(this, OnboardingTagActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearAllSharedPreferences() {

        getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, Context.MODE_PRIVATE)
            .edit {
                clear()
            }

        getSharedPreferences(MainViewModel.BLOCKED_APPS_PREFS, Context.MODE_PRIVATE)
            .edit {
                clear()
            }

        getSharedPreferences(MainViewModel.REGISTERED_TAG_PREF, Context.MODE_PRIVATE)
            .edit {
                clear()
            }
    }
}