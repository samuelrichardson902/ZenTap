package com.example.zentap.ui.screens.blocked

import android.accessibilityservice.AccessibilityService
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.edit
import com.example.zentap.data.AppSettings
import com.example.zentap.data.BlockedSettings


class AppBlockerAccessibilityService : AccessibilityService() {

    private val TAG = "AppBlockerService"
    private val handler = Handler(Looper.getMainLooper())
    private val reblockRunnableMap = mutableMapOf<String, Runnable>()

    // Original temporary access receiver, correctly re-included
    private val temporaryAccessReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_GRANT_TEMP_ACCESS) {
                val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: return
                grantTemporaryAccess(packageName)
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Register the temporary access receiver when the service connects
        registerReceiver(temporaryAccessReceiver,
            IntentFilter(ACTION_GRANT_TEMP_ACCESS), RECEIVER_EXPORTED)
        Log.d(TAG, "Accessibility Service connected.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //skip if not changine window, blank package name, or this app
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName || packageName == getLauncherPackageName()) return


        // The core logic: check overall toggle first, then individual app settings.
        if (BlockedSettings.getBlockedMode(this) && BlockedSettings.getAppBlockedState(packageName, this)) {
            if (!tempUnlock(packageName)) {
                showBlockingScreen(packageName)
            }
        }
    }

    private fun tempUnlock(packageName: String): Boolean {
        val expirationTime = BlockedSettings.getTempUnlockExpiration(packageName, this)
        return System.currentTimeMillis() < expirationTime // still unlocked if current < expiration
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver and clear all pending callbacks
        unregisterReceiver(temporaryAccessReceiver)
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "Accessibility Service destroyed.")
    }




    private fun grantTemporaryAccess(packageName: String) {
        Log.d(TAG, "Granting temporary access to $packageName")
        val unlockTime = AppSettings.getUnlockTime(this)
        val expirationTime = System.currentTimeMillis() + unlockTime

        BlockedSettings.setTempUnlockExpiration(packageName, expirationTime, this)

        reblockRunnableMap[packageName]?.let { handler.removeCallbacks(it) }

        val reblockRunnable = Runnable {
            Log.d(TAG, "Temporary access for $packageName expired. Re-blocking.")
            showBlockingScreen(packageName)
        }
        handler.postDelayed(reblockRunnable, unlockTime)
        reblockRunnableMap[packageName] = reblockRunnable
    }

    private fun showBlockingScreen(packageName: String) {
        val intent = Intent(this, BlockingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
            putExtra("blocked_package", packageName)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        try {
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            Log.e(TAG, "Failed to show blocking screen.", e)
        }
    }

    private fun getLauncherPackageName(): String? {
        val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName
    }

    override fun onInterrupt() {}

    companion object {
        const val ACTION_GRANT_TEMP_ACCESS = "com.example.zentap.GRANT_TEMP_ACCESS"
        const val EXTRA_PACKAGE_NAME = "package_name"
        const val PREFS_TEMP_UNLOCK = "temp_unlock"
    }
}