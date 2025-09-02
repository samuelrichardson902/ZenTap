package com.example.zentap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.theme.ZenTapTheme

class OnboardingTagActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        addOnNewIntentListener { intent -> handleIntent(intent) }

        setContent {
            ZenTapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OnboardingTagScreen()
                }
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null || intent.action != "android.nfc.action.NDEF_DISCOVERED") return
        intent.data?.let { uri ->
            val scannedId = uri.lastPathSegment
            if (scannedId != null) {
                NfcSettings.setRegisteredTag(this, scannedId)
                Toast.makeText(this, "Tag registered successfully!", Toast.LENGTH_SHORT).show()
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(mainActivityIntent)
                finish()
            }
        }
    }
}

@Composable
fun OnboardingTagScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register Your Tag",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Scan an NFC tag to associate it with your ZenTap account.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
