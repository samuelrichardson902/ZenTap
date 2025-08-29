package com.example.zentap.ui.screens.blocked

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BlockedFeatureScreen(
    appName: String,
    viewModel: BlockedViewModel,
    onUnlockApp: () -> Unit,
    onClose: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    val shouldUnlock by viewModel.unlockEvent.collectAsState()
    // Get the random message from the ViewModel
    val blockedMessage by viewModel.blockedMessage.collectAsState()

    LaunchedEffect(shouldUnlock) {
        if (shouldUnlock) {
            onUnlockApp()
        }
    }

    when (val state = screenState) {
        is BlockedScreenState.Idle -> {
            BlockedScreen(
                appName = appName,
                blockedMessage = blockedMessage,
                onClose = onClose,
                onRequestAccess = { viewModel.startCountdown() }
            )
        }
        is BlockedScreenState.CountingDown -> {
            CountdownScreen(timeLeft = state.timeLeftFormatted)
        }
    }
}
