package com.example.zentap.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zentap.MainViewModel
import com.example.zentap.util.ToastManager


@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val isBlockedMode by viewModel.isOverallToggleOn.collectAsState()
    val blockingModeType by viewModel.blockingModeType.collectAsState()
    val strictUnlockDuration by viewModel.strictUnlockDurationMinutes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOverallState(context)
        viewModel.loadBlockingModeType(context)
        viewModel.loadStrictUnlockDuration(context)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(elevation = CardDefaults.cardElevation(2.dp)) {
            Text(
                text = "To change your registered NFC tag, simply scan a new tag with your phone while the app is open.",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        var showBreakGlassDialog by remember { mutableStateOf(false) }
        BreakGlassCard(onClick = { showBreakGlassDialog = true })
        if (showBreakGlassDialog) {
            BreakGlassDialog(viewModel = viewModel, onDismiss = { showBreakGlassDialog = false })
        }

        BlockingModeCard(
            currentMode = blockingModeType,
            isBlockedMode = isBlockedMode,
            onClick = { viewModel.toggleBlockingModeType(context) }
        )

        // --- NEW: Conditionally shown card for strict mode duration ---
        if (blockingModeType == "Strict") {
            StrictDurationCard(
                durationMinutes = strictUnlockDuration,
                isBlockedMode = isBlockedMode,
                onDurationChange = { newDuration ->
                    val duration = newDuration.toIntOrNull() ?: 0
                    viewModel.setStrictUnlockDuration(duration, context)
                }
            )
        }

        TagManagementCard(
            navController = navController,
            isBlockedMode = isBlockedMode
        )
    }
}

// --- NEW CARD COMPOSABLE ---
@Composable
fun StrictDurationCard(
    durationMinutes: Int,
    isBlockedMode: Boolean,
    onDurationChange: (String) -> Unit
) {
    val alpha = if (isBlockedMode) 0.5f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Unlock Duration:", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = if (durationMinutes > 0) durationMinutes.toString() else "",
                onValueChange = onDurationChange,
                modifier = Modifier.width(120.dp),
                label = { Text("Minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                enabled = !isBlockedMode
            )
        }
    }
}

@Composable
fun BlockingModeCard(
    currentMode: String,
    isBlockedMode: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var showInfoDialog by remember { mutableStateOf(false) }

    val alpha = if (isBlockedMode) 0.5f else 1f
    val icon = if (currentMode == "Strict") Icons.Default.Shield else Icons.Default.GppGood
    val text = if (currentMode == "Strict") "Strict Mode" else "Normal Mode"
    val description = if (currentMode == "Strict") "Temporary, timed unlocks." else "Standard on/off toggle."

    if (showInfoDialog) {
        BlockingModeInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(enabled = !isBlockedMode) {
                if (isBlockedMode) {
                    ToastManager.showToast(context, "Cannot change mode while blocking is active")
                } else {
                    onClick()
                }
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Blocking Mode Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { showInfoDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "More info about blocking modes"
                )
            }
        }
    }
}

// ... (The rest of your SettingsScreen.kt file remains the same)
// BlockingModeInfoDialog, TagManagementCard, BreakGlassCard, etc.

// --- NEW DIALOG COMPOSABLE ---
@Composable
fun BlockingModeInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Blocking Modes Explained") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Normal Mode: ")
                        }
                        append("The blocking mode is a simple toggle that you can turn on or off manually.")
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Strict Mode: ")
                        }
                        append("Disabling the blocker is temporary. It will automatically re-enable after a set period.")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it")
            }
        }
    )
}

@Composable
fun TagManagementCard(
    navController: NavController,
    isBlockedMode: Boolean
) {
    val context = LocalContext.current
    val alpha = if (isBlockedMode) 0.5f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(enabled = !isBlockedMode) {
                if (isBlockedMode) {
                    ToastManager.showToast(context, "Cannot manage tags while blocked mode is active")
                } else {
                    navController.navigate("tag_management")
                }
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Manage NFC Tags",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "View, rename, or remove your registered tags.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// --- Break Glass Card Composable ---

@Composable
fun BreakGlassCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Break Glass",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "Use this to disable the blocker if you've lost your tag.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
            )
        }
    }
}

// --- Break Glass Dialog Composable ---

@Composable
fun BreakGlassDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val challengeString by remember { mutableStateOf(generateRandomString(10)) }
    var userInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Break Glass") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("To disable blocking, please type the following text exactly:")
                Text(
                    text = challengeString,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { newValue ->
                        if (newValue.length <= userInput.length + 1) {
                            userInput = newValue
                        }
                    },
                    label = { Text("Enter Unlock Code") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (userInput == challengeString) {
                        viewModel.toggleOverallState(false, context)
                        ToastManager.showToast(context, "Blocked mode disabled successfully.")
                        onDismiss()
                    } else {
                        ToastManager.showToast(context, "Incorrect entry. Try again.")
                    }
                }
            ) {
                Text("Confirm Disable")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// --- Helper Function for Random String ---

private fun generateRandomString(length: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + "!@#$%?".toList()
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}