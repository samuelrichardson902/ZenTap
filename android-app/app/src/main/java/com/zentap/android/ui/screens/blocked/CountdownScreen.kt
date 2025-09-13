package com.zentap.android.ui.screens.blocked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountdownScreen(
    timeLeft: String,      // same as before
    appName: String? = null   // OPTIONAL: could display which app is being unlocked
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
            text = "Unlocking${if (appName != null) " $appName" else ""} in...", // CHANGED
            fontSize = 24.sp,
            color = Color(0xFFCCCCCC),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = timeLeft,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "You must stay on this screen for the countdown to continue.",
            fontSize = 14.sp,
            color = Color(0xFF888888),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Preview
@Composable
fun CountdownScreenPreview() {
    CountdownScreen(timeLeft = "00:42", appName = "Instagram")
}
