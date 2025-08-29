package com.example.zentap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.zentap.ui.screens.blocked.BlockedFeatureScreen
import com.example.zentap.ui.screens.blocked.BlockedViewModel

class BlockingActivity : ComponentActivity() {

    private lateinit var viewModel: BlockedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[BlockedViewModel::class.java]

        val blockedPackage = intent.getStringExtra("blocked_package") ?: ""
        val appName = getAppName(blockedPackage)

        setContent {
            BlockedFeatureScreen(
                appName = appName,
                viewModel = viewModel,
                onUnlockApp = {
                    // When the initial countdown finishes, tell the service to start
                    // the 1-minute access timer.
                    sendGrantAccessBroadcast(blockedPackage)
                    finish() // Close the blocking screen
                },
                onClose = { goToHomeScreen() }
            )
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToHomeScreen()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Resume the countdown if the user comes back to the screen
        viewModel.resumeCountdown()
    }

    override fun onPause() {
        super.onPause()
        // Pause the countdown if the user navigates away
        viewModel.pauseCountdown()
    }

    /**
     * Sends a broadcast to the Accessibility Service to grant temporary access.
     */
    private fun sendGrantAccessBroadcast(packageName: String) {
        if (packageName.isBlank()) return
        val intent = Intent(AppBlockerAccessibilityService.ACTION_GRANT_TEMP_ACCESS).apply {
            putExtra(AppBlockerAccessibilityService.EXTRA_PACKAGE_NAME, packageName)
        }
        sendBroadcast(intent)
    }

    private fun goToHomeScreen() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        finishAndRemoveTask()
    }

    private fun getAppName(packageName: String): String {
        return try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)).toString()
        } catch (e: Exception) {
            "This app"
        }
    }
}
