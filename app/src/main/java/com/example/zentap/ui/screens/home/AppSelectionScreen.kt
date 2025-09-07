package com.example.zentap.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zentap.MainViewModel
import com.example.zentap.data.categoryOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val categorizedApps by viewModel.categorizedApps.collectAsState()
    val context = LocalContext.current
    var expandedCategories by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apps to Block") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background) // Main background color
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                // First show blocked apps
                if (categorizedApps.blockedApps.isNotEmpty()) {
                    item {
                        Text(
                            text = "Blocked Apps",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                            // .background(MaterialTheme.colorScheme.surface) // MODIFICATION: Removed background
                        )
                    }
                    items(items = categorizedApps.blockedApps, key = { it.packageName }) { app ->
                        AppItem(
                            app = AppUiModel(app.name, app.packageName, app.isBlocked),
                            onToggle = { isBlocked ->
                                val appInfo = categorizedApps.blockedApps.find { it.packageName == app.packageName }
                                    ?: categorizedApps.categories.values.flatten().find { it.packageName == app.packageName }
                                appInfo?.let { viewModel.toggleAppBlocking(it, isBlocked, context) }
                            }
                        )
                    }
                }

                // Then show other categories in the defined order
                categoryOrder.filter { it != "Blocked Apps" }.forEach { category ->
                    val apps = categorizedApps.categories[category] ?: emptyList()
                    if (apps.isEmpty()) return@forEach

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
                        items(items = apps, key = { it.packageName }) { app ->
                            AppItem(
                                app = AppUiModel(app.name, app.packageName, app.isBlocked),
                                onToggle = { isBlocked ->
                                    val appInfo = categorizedApps.blockedApps.find { it.packageName == app.packageName }
                                        ?: categorizedApps.categories.values.flatten().find { it.packageName == app.packageName }
                                    appInfo?.let { viewModel.toggleAppBlocking(it, isBlocked, context) }
                                }
                            )
                        }
                    }
                }
            }
        }
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
            .padding(16.dp),
        // .background(MaterialTheme.colorScheme.surface), // MODIFICATION: Removed background
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