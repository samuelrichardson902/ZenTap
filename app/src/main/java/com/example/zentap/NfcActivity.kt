package com.example.zentap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

class NfcActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { uri ->
            val scannedId = uri.lastPathSegment
            if (scannedId != null) {
                handleScannedTag(scannedId)
            }
        }

        // Close quickly so it feels "headless"
        finish()
    }

    private fun handleScannedTag(scannedId: String) {
        val prefs = getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, MODE_PRIVATE)
        val registeredId = prefs.getString(MainViewModel.REGISTERED_TAG_PREFS, null)

        when {
            registeredId == null -> {
                // No tag registered → launch registration
                //todo
            }

            registeredId == scannedId -> {
                // Match → toggle blocked state
                val blocked = prefs.getBoolean("is_on", false)
                prefs.edit().putBoolean("is_on", !blocked).apply()

                Toast.makeText(
                    this,
                    "Blocked mode is now ${if (!blocked) "ON" else "OFF"}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                // Different tag → send to register flow
                //todo
            }
        }
    }
}