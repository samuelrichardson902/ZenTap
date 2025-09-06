package com.example.zentap.ui.screens.blocked

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import com.example.zentap.data.AppSettings
import com.example.zentap.data.BlockedMessages

class BlockingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val blockedPackage = intent.getStringExtra("blocked_package") ?: ""
        val appName = getAppName(blockedPackage)

        setContent {
            var timeLeft by remember { mutableStateOf(AppSettings.getWaitTime(this)) }
            val blockedMessage = remember { BlockedMessages.getRandomMessage() }
            var unlocked by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft -= 1000
                }
                unlocked = true
            }

            BlockedFeatureScreen(
                appName = appName,
                timeLeftFormatted = if (unlocked) "00:00" else formatTime(timeLeft),
                emoji = blockedMessage.emoji,
                message = blockedMessage.text,
                onUnlockApp = {
                    sendGrantAccessBroadcast(blockedPackage)
                    finish()
                },
                onClose = { goToHomeScreen() },
                onRequestAccess = {
                    // CHANGED: trigger countdown start
                    timeLeft = AppSettings.getWaitTime(this)
                    unlocked = false
                }
            )

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToHomeScreen()
            }
        })
    }

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
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(packageName, 0)
            ).toString()
        } catch (e: Exception) {
            "This app"
        }
    }

    // CHANGED: Local helper for formatting time
    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}