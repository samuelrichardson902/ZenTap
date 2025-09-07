package com.example.zentap.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner // Import added
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle // Import added
import androidx.lifecycle.LifecycleEventObserver // Import added
import androidx.navigation.NavController
import com.example.zentap.MainViewModel
import com.example.zentap.util.ToastManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagManagementScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current // Get lifecycle owner

    // Collect tags from ViewModel
    val tags by viewModel.registeredTags.collectAsState()

    var editingTagId by remember { mutableStateOf<String?>(null) }
    var newName by remember { mutableStateOf("") }
    var showTapPhoneMessage by remember { mutableStateOf(false) }

    val isBlockedMode by viewModel.isOverallToggleOn.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // --- MODIFICATION START ---
    // This effect handles both initial loading and refreshing when returning to the screen.
    // It replaces the previous separate LaunchedEffects for loading.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadRegisteredTags(context)
                // Optionally reset transient state like the snackbar message when returning
                // showTapPhoneMessage = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    // --- MODIFICATION END ---

    // Navigate back if blocked mode is enabled
    LaunchedEffect(isBlockedMode) {
        if (isBlockedMode) {
            ToastManager.showToast(context, "Cannot manage tags while blocked mode is active")
            navController.popBackStack()
        }
    }

    // Show snackbar when requested
    LaunchedEffect(showTapPhoneMessage) {
        if (showTapPhoneMessage) {
            snackbarHostState.showSnackbar("Tap phone on a new tag to register")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Manage Tags") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showTapPhoneMessage = true // show snackbar
                        // External NFC scan will trigger ChangeTagActivity
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tag")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (tags.isEmpty()) {
                Text("No tags registered.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tags.entries.toList()) { (tagId, displayName) ->
                        val cardAlpha = if (isBlockedMode) 0.5f else 1f
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(cardAlpha),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Name: $displayName", style = MaterialTheme.typography.bodyLarge)
                                Text("ID: $tagId", style = MaterialTheme.typography.bodySmall)

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            if (!isBlockedMode) {
                                                editingTagId = tagId
                                                newName = displayName
                                            } else {
                                                ToastManager.showToast(context, "Cannot rename while blocked mode is active")
                                            }
                                        },
                                        enabled = !isBlockedMode
                                    ) {
                                        Text("Rename")
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            if (!isBlockedMode) {
                                                viewModel.removeTag(context, tagId)
                                                ToastManager.showToast(context, "Removed tag")
                                            } else {
                                                ToastManager.showToast(context, "Cannot remove tag while blocked mode is active")
                                            }
                                        },
                                        enabled = !isBlockedMode
                                    ) {
                                        Text("Remove")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit dialog
    if (editingTagId != null) {
        AlertDialog(
            onDismissRequest = { editingTagId = null },
            title = { Text("Rename Tag") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    editingTagId?.let {
                        viewModel.renameTag(context, it, newName)
                        ToastManager.showToast(context, "Tag renamed")
                    }
                    editingTagId = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingTagId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}