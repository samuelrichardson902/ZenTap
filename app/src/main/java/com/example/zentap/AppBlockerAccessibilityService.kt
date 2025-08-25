package com.example.zentap

import android.accessibilityservice.AccessibilityService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

import android.content.pm.PackageManager

class AppBlockerAccessibilityService : AccessibilityService() {

    private val TAG = "AppBlockerService"
    private var launcherPackageName: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            if (packageName == this.packageName || packageName == launcherPackageName) {
                return
            }

            if (isAppBlocked(packageName)) {
                showBlockingScreen(packageName)
            }
        }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
        launcherPackageName = getLauncherPackageName()
        Log.d(TAG, "Accessibility Service is connected and running.")
    }

    private fun getLauncherPackageName(): String? {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName
    }

    private fun isAppBlocked(packageName: String): Boolean {
        val prefs = getSharedPreferences("blocked_apps", Context.MODE_PRIVATE)
        return prefs.getBoolean(packageName, false)
    }

    private fun showBlockingScreen(packageName: String) {
        // --- START OF FIX ---
        // Create the regular Intent as before
        val intent = Intent(this, BlockingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("blocked_package", packageName)
        }

        // Wrap it in a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, // requestCode, can be 0 for this use case
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            // Send the PendingIntent to have the system launch the activity
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            Log.e(TAG, "PendingIntent was canceled", e)
        }
        // --- END OF FIX ---
    }
}