package com.example.zentap

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.screens.MainScreen
import com.example.zentap.ui.screens.blocked.AppBlockerAccessibilityService
import com.example.zentap.ui.screens.onboarding.OnboardingPermsActivity
import com.example.zentap.ui.screens.onboarding.OnboardingTagActivity
import com.example.zentap.ui.theme.ZenTapTheme

class MainActivity : BaseActivity() {

    val viewModel: MainViewModel by viewModels()

    // --- THIS IS THE FIX ---
    // A receiver that listens for the signal from the service.
    private val blockerStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // When the signal is received, immediately reload the blocker state.
            if (intent?.action == AppBlockerAccessibilityService.ACTION_BLOCKER_STATE_CHANGED) {
                viewModel.loadOverallState(this@MainActivity)
            }
        }
    }

    private val accessibilityPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (isAccessibilityServiceEnabled()) {
            if (!hasRegisteredTags(this)) {
                val intent = Intent(this, OnboardingTagActivity::class.java)
                startActivity(intent)
            } else {
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

        // Register the receiver to start listening.
        registerReceiver(blockerStateReceiver, IntentFilter(AppBlockerAccessibilityService.ACTION_BLOCKER_STATE_CHANGED), RECEIVER_EXPORTED)

        viewModel.loadOverallState(this)
        viewModel.loadBlockingModeType(this)
        viewModel.refreshCountdown(this)

        if (!isAccessibilityServiceEnabled()) {
            val intent = Intent(this, OnboardingPermsActivity::class.java)
            accessibilityPermissionLauncher.launch(intent)
            return
        }

        if (!hasRegisteredTags(this)) {
            startActivity(Intent(this, OnboardingTagActivity::class.java))
            return
        }

        viewModel.loadAndSortApps(packageManager, this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver to prevent memory leaks.
        unregisterReceiver(blockerStateReceiver)
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