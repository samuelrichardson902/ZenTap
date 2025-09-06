package com.example.zentap.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zentap.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavController,
) {
    val isOverallToggleOn by viewModel.isOverallToggleOn.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BlockerControlCard(
            isBlockerEnabled = isOverallToggleOn,
            onToggle = {
                val newState = !isOverallToggleOn
                if (newState && !isAccessibilityServiceEnabled()) {
                    showDialog = true
                } else {
                    viewModel.toggleOverallState(newState, context)
                }
            }
        )

        AppSelectionNavCard(
            isBlockerEnabled = isOverallToggleOn,
            onClick = {
                if (isOverallToggleOn) {
                    Toast.makeText(context, "Cannot change apps while blocker is active", Toast.LENGTH_SHORT).show()
                } else {
                    navController.navigate("app_selection")
                }
            }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Accessibility Permission Required") },
            text = { Text("To enable the blocker, this app needs accessibility permission. Please enable it in the settings.") },
            confirmButton = {
                TextButton(onClick = {
                    openAccessibilitySettings()
                    showDialog = false
                }) { Text("Go to Settings") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockerControlCard(
    isBlockerEnabled: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Blocker Control", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    text = if (isBlockerEnabled) "Status: Active" else "Status: Inactive",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = if (isBlockerEnabled) Icons.Default.Lock else Icons.Default.LockOpen,
                contentDescription = "Toggle Blocker",
                modifier = Modifier
                    .size(40.dp),
                tint = if (isBlockerEnabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionNavCard(
    isBlockerEnabled: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (isBlockerEnabled) 0.5f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(enabled = !isBlockerEnabled, onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "App Selection", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    text = "Choose which apps to block",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.PhonelinkSetup,
                contentDescription = "Navigate to App Selection",
                modifier = Modifier.size(40.dp),
                tint = if (!isBlockerEnabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}
