package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.screens.MainScreen
import com.example.zentap.ui.screens.onboarding.OnboardingPermsActivity
import com.example.zentap.ui.screens.onboarding.OnboardingTagActivity
import com.example.zentap.ui.theme.ZenTapTheme
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : BaseActivity() {

    val viewModel: MainViewModel by viewModels()


    // Register a launcher for the OnboardingPermsActivity
    private val accessibilityPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // This callback is triggered when the user returns from OnboardingPermsActivity
        // We can re-check the permission status here
        if (isAccessibilityServiceEnabled()) {
            // Permission is granted, now check for the NFC tag
            if (!hasRegisteredTags(this)) {
                // Redirect to the tag onboarding screen
                val intent = Intent(this, OnboardingTagActivity::class.java)
                startActivity(intent)
            } else {
                // All permissions and settings are good, load the apps
                viewModel.loadAndSortApps(packageManager, this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZenTapTheme {
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadOverallState(this)


        // Check for accessibility service
        if (!isAccessibilityServiceEnabled()) {
            // Redirect to the accessibility onboarding screen
            val intent = Intent(this, OnboardingPermsActivity::class.java)
            accessibilityPermissionLauncher.launch(intent)
            return
        }

        // Check for registered NFC tag
        if (!hasRegisteredTags(this)) {
            startActivity(Intent(this, OnboardingTagActivity::class.java))
            return
        }

        // Uses caching in ViewModel (won’t reload if already loaded)
        viewModel.loadAndSortApps(packageManager, this)
    }

    override fun onPause() {
        super.onPause()
    }


    fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.packageName == packageName }
    }

    fun hasRegisteredTags(context: Context): Boolean {
        return NfcSettings.getRegisteredTags(context).isNotEmpty()
    }
}