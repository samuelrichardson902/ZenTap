package com.example.zentap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.zentap.data.NfcSettings

class NfcActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        intent?.data?.let { uri ->
            Log.d("NfcActivity", "Launched with intent: ${intent?.data}")

            val scannedId = uri.lastPathSegment
            if (scannedId != null) {
                handleScannedTag(scannedId)
            }
        }

        // Close quickly so it feels "headless"
        finish()
    }

    private fun handleScannedTag(scannedId: String) {
        val registeredTag = NfcSettings.getRegisteredTag(this)

        if (registeredTag == scannedId) {
            // Match → toggle blocked state
            val prefs = getSharedPreferences(MainViewModel.OVERALL_TOGGLE_PREFS, MODE_PRIVATE)
            val blocked = prefs.getBoolean("is_on", false)
            prefs.edit().putBoolean("is_on", !blocked).apply()

            Toast.makeText(
                this,
                "Blocked mode is now ${if (!blocked) "ON" else "OFF"}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Mismatch → launch change tag flow
            val intent = Intent(this, ChangeTagActivity::class.java).apply {
                putExtra(ChangeTagActivity.EXTRA_TAG_ID, scannedId)
            }
            startActivity(intent)
        }
    }
}