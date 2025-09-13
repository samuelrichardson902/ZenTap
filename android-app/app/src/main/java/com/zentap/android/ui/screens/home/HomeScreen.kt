package com.zentap.android.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.zentap.android.MainViewModel
import com.zentap.android.util.ToastManager

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavController,
) {
    // --- STATE MANAGEMENT ---
    val isOverallToggleOn by viewModel.isOverallToggleOn.collectAsState()
    val timeLeft by viewModel.strictModeTimeLeft.collectAsState()
    val blockingMode by viewModel.blockingModeType.collectAsState()
    val strictUnlockDuration by viewModel.strictUnlockDurationMinutes.collectAsState()
    var showDurationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadOverallState(context)
        viewModel.loadBlockingModeType(context)
        viewModel.loadStrictUnlockDuration(context)
    }

    // State for controlling dialogs
    var showBreakGlassDialog by remember { mutableStateOf(false) }
    var showStrictInfoDialog by remember { mutableStateOf(false) }

    // --- DIALOGS ---
    if (showBreakGlassDialog) {
        BreakGlassDialog(viewModel = viewModel, onDismiss = { showBreakGlassDialog = false })
    }
    if (showStrictInfoDialog) {
        BlockingModeInfoDialog(onDismiss = { showStrictInfoDialog = false })
    }
    if (showDurationDialog) {
        StrictDurationDialog(
            viewModel = viewModel,
            initialMinutes = strictUnlockDuration,
            onDismiss = { showDurationDialog = false }
        )
    }

    // --- UI LAYOUT ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatusCard(
            isBlockerEnabled = isOverallToggleOn,
            timeLeft = timeLeft,
            blockingMode = blockingMode,
            onToggle = {
                val newState = !isOverallToggleOn
                viewModel.toggleOverallState(newState, context)
                val message = "Blocked mode is now ${if (newState) "ON" else "OFF"}"
                ToastManager.showToast(context, message)
            }
        )

        Spacer(Modifier.height(24.dp))

        // Grid of dashboard buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardButton(
                modifier = Modifier.weight(1f),
                text = "app selection",
                icon = Icons.Default.SettingsApplications,
                enabled = !isOverallToggleOn,
                onClick = {
                    if (isOverallToggleOn) {
                        ToastManager.showToast(context, "Cannot change apps while blocker is active")
                    } else {
                        navController.navigate("app_selection")
                    }
                }
            )
            DashboardButton(
                modifier = Modifier.weight(1f),
                text = "Strict Mode",
                icon = if (blockingMode == "Strict") Icons.Default.GppGood else Icons.Outlined.Shield,
                enabled = !isOverallToggleOn,
                hasInfoIcon = true,
                onClick = {
                    if (isOverallToggleOn) {
                        ToastManager.showToast(context, "Cannot change mode while blocking is active")
                    } else {
                        viewModel.toggleBlockingModeType(context)
                    }
                },
                onInfoClick = { showStrictInfoDialog = true }
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardButton(
                modifier = Modifier.weight(1f),
                text = "Manage NFC Tags",
                icon = Icons.Default.Nfc,
                enabled = !isOverallToggleOn,
                onClick = {
                    if (isOverallToggleOn) {
                        ToastManager.showToast(context, "Cannot manage tags while blocked mode is active")
                    } else {
                        navController.navigate("tag_management")
                    }
                }
            )
            DashboardButton(
                modifier = Modifier.weight(1f),
                text = "Break Glass",
                icon = Icons.Default.Warning,
                isError = true,
                onClick = { showBreakGlassDialog = true }
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardButton(
                modifier = Modifier.weight(1f),
                text = "Schedule Auto Lock",
                icon = Icons.Default.Schedule,
                enabled = !isOverallToggleOn,
                onClick = {
                    navController.navigate("schedule_screen")
                }
            )
            // This button will only appear if Strict Mode is on.
            // Otherwise, a Spacer will keep the layout balanced.
            if (blockingMode == "Strict") {
                DashboardButton(
                    modifier = Modifier.weight(1f),
                    text = "Unlock Duration",
                    icon = Icons.Default.Timer,
                    enabled = !isOverallToggleOn,
                    onClick = { showDurationDialog = true }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

// --- UI COMPONENTS ---

@Composable
fun StatusCard(
    isBlockerEnabled: Boolean,
    timeLeft: Long,
    blockingMode: String,
    onToggle: () -> Unit
) {
    val context = LocalContext.current
    val statusText = if (isBlockerEnabled) "Status Active" else "Status Inactive"
    val icon = if (isBlockerEnabled) Icons.Default.Lock else Icons.Default.LockOpen
    val iconColor = if (isBlockerEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    var menuExpanded by remember { mutableStateOf(false) }

    val showTimer = !isBlockerEnabled && blockingMode == "Strict" && timeLeft > 0

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        if (isBlockerEnabled) {
                            ToastManager.showToast(context, "Blocked mode must be disabled using the NFC tag.")
                        } else {
                            onToggle()
                        }
                    })
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = statusText,
                    modifier = Modifier.size(36.dp),
                    tint = iconColor
                )
                Text(
                    text = statusText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (showTimer) {
                    Text(
                        text = "Unblocked for ${formatTime(timeLeft)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Privacy Policy") },
                        onClick = {
                            context.startActivity(android.content.Intent(context, com.zentap.android.ui.screens.privacy.PrivacyPolicyActivity::class.java))
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    isError: Boolean = false,
    hasInfoIcon: Boolean = false,
    onClick: () -> Unit,
    onInfoClick: () -> Unit = {}
) {
    val alpha = if (enabled) 1f else 0.5f

    // The icon color is still red for the error state.
    val iconColor = when {
        isError -> MaterialTheme.colorScheme.error
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.primary
    }

    val cardColors = CardDefaults.cardColors()

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .alpha(alpha)
            .clickable(enabled = enabled, onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(40.dp),
                tint = iconColor // Use the icon-specific color
            )
            Spacer(Modifier.height(12.dp))

            // This Box allows the Text and Icon group to be centered together.
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        // Text color is now always the default onSurface color.
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (hasInfoIcon) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onInfoClick,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "More Info",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


// --- DIALOGS AND HELPER FUNCTIONS (Previously in SettingsScreen.kt) ---

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

private fun generateRandomString(length: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

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
                        append("A simple on/off toggle for blocking apps.")
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Strict Mode: ")
                        }
                        append("Disabling the blocker is temporary. It will automatically re-enable after a set time.")
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
fun BreakGlassDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val challengeString by remember { mutableStateOf(generateRandomString(8)) }
    var userInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Emergency Disable") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("To disable blocking, please type the following text exactly:")
                Text(
                    text = challengeString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Enter text here") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        autoCorrect = false
                    ),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (userInput == challengeString) {
                        viewModel.toggleOverallState(false, context)
                        ToastManager.showToast(context, "Blocked mode disabled.")
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

@Composable
fun StrictDurationDialog(
    viewModel: MainViewModel,
    initialMinutes: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var durationInput by remember { mutableStateOf(initialMinutes.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Strict Mode Duration") },
        text = {
            Column {
                Text("Set how many minutes the blocker stays off when temporarily unlocked.")
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = durationInput,
                    onValueChange = { newValue ->
                        // Allow only digits
                        if (newValue.all { it.isDigit() }) {
                            durationInput = newValue
                        }
                    },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newDuration = durationInput.toIntOrNull() ?: 0
                    viewModel.setStrictUnlockDuration(newDuration, context)
                    ToastManager.showToast(context, "Unlock duration set to $newDuration minutes.")
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}