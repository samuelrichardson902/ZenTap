package com.zentap.android.util

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.zentap.android.data.BlockedSettings
import com.zentap.android.data.NfcSettings
import com.zentap.android.ui.screens.blocked.AppBlockerAccessibilityService
import com.zentap.android.ui.screens.nfc.ChangeTagActivity

class NfcActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.data?.let { uri ->
            val scannedId = uri.lastPathSegment
            if (scannedId != null) {
                handleScannedTag(scannedId)
            }
        }
        finish()
    }

    private fun handleScannedTag(scannedId: String) {
        val registeredTags = NfcSettings.getRegisteredTags(this)

        if (registeredTags.isEmpty()) {
            NfcSettings.registerNewTag(this, scannedId)
            ToastManager.showToast(this, "Tag registered successfully!")
        } else if (NfcSettings.isTagRegistered(this, scannedId)) {
            val isMasterSwitchOn = BlockedSettings.getBlockedMode(this)

            if (isMasterSwitchOn) {
                // --- Trying to turn blocking OFF ---
                val mode = BlockedSettings.getBlockingModeType(this)
                BlockedSettings.setBlockedMode(false, this)

                if (mode == "Strict") {
                    val durationMs = BlockedSettings.getStrictUnlockDuration(this)
                    val expiration = System.currentTimeMillis() + durationMs

                    // --- THIS IS THE NEW LINE ---
                    // Save the expiration time so the countdown can see it.
                    BlockedSettings.setStrictUnlockExpiration(expiration, this)

                    val intent = Intent(AppBlockerAccessibilityService.ACTION_SCHEDULE_REBLOCK).apply {
                        putExtra(AppBlockerAccessibilityService.EXTRA_DURATION_MS, durationMs)
                    }
                    sendBroadcast(intent)
                    val durationMinutes = durationMs / 60000
                    ToastManager.showToast(this, "Strict Mode: Blocker off for $durationMinutes minutes.")
                } else {
                    ToastManager.showToast(this, "Blocked mode is now OFF")
                }
            } else {
                // --- Trying to turn blocking ON ---
                BlockedSettings.setBlockedMode(true, this)

                // --- THIS IS THE NEW LINE ---
                // Clear the expiration time so the countdown stops.
                BlockedSettings.setStrictUnlockExpiration(0L, this)

                sendBroadcast(Intent(AppBlockerAccessibilityService.ACTION_CANCEL_REBLOCK))
                ToastManager.showToast(this, "Blocked mode is now ON")
            }
        } else {
            val intent = Intent(this, ChangeTagActivity::class.java).apply {
                putExtra(ChangeTagActivity.PRESENTED_TAG_ID, scannedId)
            }
            startActivity(intent)
        }
    }
}