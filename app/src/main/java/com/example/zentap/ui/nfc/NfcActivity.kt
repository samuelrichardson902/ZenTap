package com.example.zentap.ui.nfc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import com.example.zentap.MainActivity
import com.example.zentap.MainViewModel
import com.example.zentap.data.BlockedSettings.getBlockedMode
import com.example.zentap.data.BlockedSettings.setBlockedMode
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
        val registeredTag = NfcSettings.getRegisteredTag(this)

        if (registeredTag == null) {
            // No tag registered yet, so register this one and proceed to main activity.
            NfcSettings.setRegisteredTag(this, scannedId)
            Toast.makeText(this, "Tag registered successfully!", Toast.LENGTH_SHORT).show()
        } else if (registeredTag == scannedId) {
            // Match → toggle blocked state
            val blocked = getBlockedMode(this)
            if (setBlockedMode(!blocked, this)){
                Toast.makeText(
                    this,
                    "Blocked mode is now ${if (!blocked) "ON" else "OFF"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Mismatch → launch change tag flow
            val intent = Intent(this, ChangeTagActivity::class.java).apply {
                putExtra(ChangeTagActivity.PRESENTED_TAG_ID, scannedId)
            }
            startActivity(intent)
        }
    }
}