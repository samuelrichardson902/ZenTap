package com.example.zentap.util

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.zentap.data.BlockedSettings
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.screens.nfc.ChangeTagActivity

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
            val blocked = BlockedSettings.getBlockedMode(this)
            if (BlockedSettings.setBlockedMode(!blocked, this)) {
                val message = "Blocked mode is now ${if (!blocked) "ON" else "OFF"}"
                ToastManager.showToast(this, message)
            }
        } else {
            val intent = Intent(this, ChangeTagActivity::class.java).apply {
                putExtra(ChangeTagActivity.PRESENTED_TAG_ID, scannedId)
            }
            startActivity(intent)
        }
    }
}