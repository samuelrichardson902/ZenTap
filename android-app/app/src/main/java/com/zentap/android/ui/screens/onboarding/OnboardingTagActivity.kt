package com.zentap.android.ui.screens.onboarding

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zentap.android.BaseActivity
import com.zentap.android.data.NfcSettings
import com.zentap.android.ui.theme.ZenTapTheme

class OnboardingTagActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenTapTheme {
                OnboardingTagScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (NfcSettings.getRegisteredTags(this).isNotEmpty()) {
            finish()
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
            modifier = Modifier.padding(bottom = 16.dp),
            // Apply primary color to the headline.
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Scan an NFC tag to associate it with your ZenTap account.",
            style = MaterialTheme.typography.bodyLarge,
            // Use onSurfaceVariant for secondary/instructional text.
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}