package com.zentap.android.ui.screens.blocked

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
import com.zentap.android.data.AppSettings
import com.zentap.android.data.BlockedSettings
import com.zentap.android.util.ToastManager


class AppBlockerAccessibilityService : AccessibilityService() {

    private val TAG = "AppBlockerService"
    private val handler = Handler(Looper.getMainLooper())
    private val reblockRunnableMap = mutableMapOf<String, Runnable>()
    private var globalReblockRunnable: Runnable? = null
    private var lastKnownForegroundApp: String? = null
    private var strictActivationRunnable: Runnable? = null

    private val commandReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                ACTION_SCHEDULE_REBLOCK -> {
                    val duration = intent.getLongExtra(EXTRA_DURATION_MS, 0L)
                    if (duration > 0) scheduleGlobalReblock(duration)
                }
                ACTION_CANCEL_REBLOCK -> cancelGlobalReblock()
                ACTION_START_STRICT_ACTIVATION_TIMER -> scheduleStrictActivation()
                ACTION_CANCEL_STRICT_ACTIVATION_TIMER -> cancelStrictActivation()
                ACTION_GRANT_TEMP_ACCESS -> {
                    val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: return
                    grantTemporaryAccess(packageName)
                }
                ACTION_TRIGGER_IMMEDIATE_BLOCK_CHECK -> performImmediateBlockCheck()
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val intentFilter = IntentFilter().apply {
            addAction(ACTION_GRANT_TEMP_ACCESS)
            addAction(ACTION_SCHEDULE_REBLOCK)
            addAction(ACTION_CANCEL_REBLOCK)
            addAction(ACTION_START_STRICT_ACTIVATION_TIMER)
            addAction(ACTION_CANCEL_STRICT_ACTIVATION_TIMER)
            addAction(ACTION_TRIGGER_IMMEDIATE_BLOCK_CHECK)
        }
        registerReceiver(commandReceiver, intentFilter, RECEIVER_EXPORTED)
        Log.d(TAG, "Accessibility Service connected.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return

        lastKnownForegroundApp = packageName

        if (packageName == this.packageName || packageName == getLauncherPackageName()) {
            return
        }

        if (BlockedSettings.getBlockedMode(this) && BlockedSettings.getAppBlockedState(packageName, this)) {
            if (!tempUnlock(packageName)) {
                showBlockingScreen(packageName)
            }
        }
    }

    private fun tempUnlock(packageName: String): Boolean {
        val expirationTime = BlockedSettings.getTempUnlockExpiration(packageName, this)
        return System.currentTimeMillis() < expirationTime
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(commandReceiver)
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "Accessibility Service destroyed.")
    }

    private fun scheduleStrictActivation() {
        cancelStrictActivation()
        strictActivationRunnable = Runnable {
            Log.d(TAG, "Strict mode activation timer finished. Enabling blocker.")
            BlockedSettings.setBlockedMode(true, this@AppBlockerAccessibilityService)
            ToastManager.showToast(this@AppBlockerAccessibilityService, "Blocker is now active.")
            sendBroadcast(Intent(ACTION_BLOCKER_STATE_CHANGED))
        }.also {
            handler.postDelayed(it, 15000)
        }
    }

    private fun cancelStrictActivation() {
        strictActivationRunnable?.let { handler.removeCallbacks(it) }
        strictActivationRunnable = null
    }

    private fun scheduleGlobalReblock(durationMs: Long) {
        cancelGlobalReblock()
        globalReblockRunnable = Runnable {
            Log.d(TAG, "Strict mode temporary access expired. Re-blocking globally.")
            BlockedSettings.setBlockedMode(true, this@AppBlockerAccessibilityService)

            // --- THIS IS THE FIX ---
            // Send a broadcast to tell the UI to refresh itself immediately.
            sendBroadcast(Intent(ACTION_BLOCKER_STATE_CHANGED))

            val currentApp = lastKnownForegroundApp
            if (currentApp != null && BlockedSettings.getAppBlockedState(currentApp, this)) {
                Log.d(TAG, "User was in a blocked app ($currentApp). Showing BlockedActivity.")
                showBlockingScreen(currentApp)
            } else {
                Log.d(TAG, "User was not in a blocked app ($currentApp). Re-blocking silently.")
                ToastManager.showToast(this@AppBlockerAccessibilityService, "Blocker has been re-enabled.")
            }

            globalReblockRunnable = null
        }.also {
            handler.postDelayed(it, durationMs)
            Log.d(TAG, "Scheduled global re-block in ${durationMs / 1000} seconds.")
        }
    }

    private fun cancelGlobalReblock() {
        globalReblockRunnable?.let { handler.removeCallbacks(it) }
        globalReblockRunnable = null
        Log.d(TAG, "Cancelled pending global re-block.")
    }

    private fun grantTemporaryAccess(packageName: String) {
        val unlockTime = AppSettings.getUnlockTime(this)
        val expirationTime = System.currentTimeMillis() + unlockTime
        BlockedSettings.setTempUnlockExpiration(packageName, expirationTime, this)
        reblockRunnableMap[packageName]?.let { handler.removeCallbacks(it) }
        val reblockRunnable = Runnable {
            if (lastKnownForegroundApp == packageName) {
                Log.d(TAG, "Temporary access for $packageName expired while user was in the app. Re-blocking.")
                showBlockingScreen(packageName)
            } else {
                Log.d(TAG, "Temporary access for $packageName expired, but user was in a different app ($lastKnownForegroundApp). No action taken.")
            }
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
            this, System.currentTimeMillis().toInt(), intent,
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

    private fun performImmediateBlockCheck() {
        val currentApp = lastKnownForegroundApp
        Log.d(TAG, "Immediate block check triggered. Current app: $currentApp")

        // If the blocker is on and the user is in a blocked app, show the screen
        if (BlockedSettings.getBlockedMode(this) &&
            currentApp != null &&
            BlockedSettings.getAppBlockedState(currentApp, this)) {

            Log.d(TAG, "User is in a blocked app ($currentApp). Showing BlockedActivity.")
            showBlockingScreen(currentApp)
        }
    }



    override fun onInterrupt() {}

    companion object {
        const val ACTION_GRANT_TEMP_ACCESS = "com.example.zentap.GRANT_TEMP_ACCESS"
        const val EXTRA_PACKAGE_NAME = "package_name"
        const val ACTION_SCHEDULE_REBLOCK = "com.example.zentap.SCHEDULE_REBLOCK"
        const val ACTION_CANCEL_REBLOCK = "com.example.zentap.CANCEL_REBLOCK"
        const val EXTRA_DURATION_MS = "duration_ms"
        const val ACTION_START_STRICT_ACTIVATION_TIMER = "com.example.zentap.START_STRICT_ACTIVATION_TIMER"
        const val ACTION_CANCEL_STRICT_ACTIVATION_TIMER = "com.example.zentap.CANCEL_STRICT_ACTIVATION_TIMER"

        // New constant for the state change signal
        const val ACTION_BLOCKER_STATE_CHANGED = "com.example.zentap.BLOCKER_STATE_CHANGED"

        const val ACTION_TRIGGER_IMMEDIATE_BLOCK_CHECK = "com.example.zentap.TRIGGER_IMMEDIATE_BLOCK_CHECK"
    }
}