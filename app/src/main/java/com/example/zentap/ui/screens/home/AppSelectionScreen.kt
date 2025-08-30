package com.example.zentap.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zentap.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    viewModel: MainViewModel,
    isAccessibilityServiceEnabled: () -> Boolean,
    openAccessibilitySettings: () -> Unit
) {
    val categorizedApps by viewModel.categorizedApps.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // State to track which categories are expanded
    var expandedCategories by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apps to Block") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                // Section for currently blocked apps
                item {
                    if (categorizedApps.blockedApps.isNotEmpty()) {
                        Text(
                            text = "Blocked Apps",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
                items(items = categorizedApps.blockedApps, key = { it.packageName }) { app ->
                    AppItem(
                        app = AppUiModel(app.name, app.packageName, app.isBlocked),
                        onToggle = { isBlocked ->
                            val appInfo = categorizedApps.blockedApps.find { it.packageName == app.packageName }
                                ?: categorizedApps.categories.values.flatten().find { it.packageName == app.packageName }
                            appInfo?.let {
                                if (isBlocked && !isAccessibilityServiceEnabled()) {
                                    showDialog = true
                                } else {
                                    viewModel.toggleAppBlocking(it, isBlocked, context)
                                    val message =
                                        if (isBlocked) "${it.name} is now blocked"
                                        else "${it.name} blocking disabled"
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                // Separator
                item { Spacer(modifier = Modifier.padding(8.dp)) }

                // Sections for other apps, grouped by category
                categorizedApps.categories.forEach { (category, categoryApps) ->
                    item(key = category) {
                        ExpandableCategoryHeader(
                            categoryName = category,
                            isExpanded = category in expandedCategories,
                            onToggleExpand = {
                                expandedCategories = if (category in expandedCategories) {
                                    expandedCategories - category
                                } else {
                                    expandedCategories + category
                                }
                            }
                        )
                    }
                    if (category in expandedCategories) {
                        items(items = categoryApps, key = { it.packageName }) { app ->
                            AppItem(
                                app = AppUiModel(app.name, app.packageName, app.isBlocked),
                                onToggle = { isBlocked ->
                                    val appInfo = categorizedApps.blockedApps.find { it.packageName == app.packageName }
                                        ?: categorizedApps.categories.values.flatten().find { it.packageName == app.packageName }
                                    appInfo?.let {
                                        if (isBlocked && !isAccessibilityServiceEnabled()) {
                                            showDialog = true
                                        } else {
                                            viewModel.toggleAppBlocking(it, isBlocked, context)
                                            val message =
                                                if (isBlocked) "${it.name} is now blocked"
                                                else "${it.name} blocking disabled"
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Accessibility Permission Required") },
            text = {
                Text("To block apps, this app needs accessibility permission. Please enable it in the settings.")
            },
            confirmButton = {
                TextButton(onClick = {
                    openAccessibilitySettings()
                    showDialog = false
                }) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ExpandableCategoryHeader(
    categoryName: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = categoryName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand"
        )
    }
}
