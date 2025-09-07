package com.example.zentap.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info Card
        Card(elevation = CardDefaults.cardElevation(2.dp)) {
            Text(
                text = "To change your registered NFC tag, simply scan a new tag with your phone while the app is open.",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Break Glass
        var showBreakGlassDialog by remember { mutableStateOf(false) }
        BreakGlassCard(onClick = { showBreakGlassDialog = true })
        if (showBreakGlassDialog) {
            BreakGlassDialog(viewModel = viewModel, onDismiss = { showBreakGlassDialog = false })
        }


        // Tag Management Card
        TagManagementCard(
            navController = navController,
            isBlockedMode = isBlockedMode
        )
    }
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

    // Generate a 6-character random string for the challenge.
    // 'remember' ensures it doesn't change on recomposition unless the dialog is fully dismissed and recreated.
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
                        // Anti-copy/paste logic: only allow changes that add/remove one character at a time.
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
    // Combine lowercase letters, uppercase letters, numbers, and special characters
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + "!@#$%?".toList()
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
