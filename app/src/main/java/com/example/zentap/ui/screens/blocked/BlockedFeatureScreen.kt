package com.example.zentap.ui.screens.blocked

import androidx.compose.runtime.Composable

@Composable
fun BlockedFeatureScreen(
    appName: String,
    timeLeftFormatted: String,
    emoji: String,
    message: String,
    onUnlockApp: () -> Unit,
    onClose: () -> Unit,
    onRequestAccess: () -> Unit,   // <--- keep this!
) {
    if (timeLeftFormatted == "00:00") {
        onUnlockApp()
    } else if (timeLeftFormatted == "") {
        BlockedScreen(
            appName = appName,
            emoji = emoji,
            message = message,
            onClose = onClose,
            onRequestAccess = onRequestAccess // <--- pass it down
        )
    } else {
        CountdownScreen(timeLeft = timeLeftFormatted, appName = appName)
    }
}


