package com.zentap.android.ui.screens.blocked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- CHANGED: BlockedMessage removed, now we just pass plain strings ---
@Composable
fun BlockedScreen(
    appName: String,
    emoji: String,        // CHANGED
    message: String,      // CHANGED
    onClose: () -> Unit,
    onRequestAccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = emoji,   // CHANGED
            fontSize = 100.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = message.format(appName),  // CHANGED
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onClose,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
            ) {
                Text("Close")
            }
            Button(
                onClick = onRequestAccess,
                modifier = Modifier.weight(1f)
            ) {
                Text("Request 1 Minute")
            }
        }
    }
}
