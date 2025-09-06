package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.screens.MainScreen
import com.example.zentap.ui.screens.onboarding.OnboardingPermsActivity
import com.example.zentap.ui.screens.onboarding.OnboardingTagActivity
import com.example.zentap.ui.theme.ZenTapTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for accessibility service
        if (!isAccessibilityServiceEnabled()) {
            // Redirect to the accessibility onboarding screen
            val intent = Intent(this, OnboardingPermsActivity::class.java)
            startActivity(intent)
            finish() // Prevents the user from navigating back
            return
        }

        // Check for registered NFC tag
        if (!isTagRegistered(this)) {
            // Redirect to the tag onboarding screen
            val intent = Intent(this, OnboardingTagActivity::class.java)
            startActivity(intent)
            finish() // Prevents the user from navigating back
            return
        }

        setContent {
            ZenTapTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Uses caching in ViewModel (won’t reload if already loaded)
        viewModel.loadAndSortApps(packageManager, this)
    }


    fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    fun isTagRegistered(context: Context): Boolean {
        return NfcSettings.getRegisteredTag(context) != null
    }
}