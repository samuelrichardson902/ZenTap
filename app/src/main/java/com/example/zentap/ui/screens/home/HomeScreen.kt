package com.example.zentap.ui.screens.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zentap.AppInfo
import com.example.zentap.CategorizedApps
import com.example.zentap.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    apps: CategorizedApps,
    isOverallToggleOn: Boolean,
    onOverallToggle: (Boolean) -> Unit,
    onToggleBlock: (AppUiModel, Boolean) -> Unit,
    viewModel: MainViewModel
) {
    // State to track which categories are expanded
    var expandedCategories by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Blocker") },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "Enabled",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(Modifier.padding(horizontal = 8.dp))
                        Switch(
                            checked = isOverallToggleOn,
                            onCheckedChange = onOverallToggle
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                // Section for currently blocked apps
                item {
                    if (apps.blockedApps.isNotEmpty()) {
                        Text(
                            text = "Blocked Apps",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.White)
                        )
                    }
                }
                items(items = apps.blockedApps, key = { it.packageName }) { app ->
                    AppItem(
                        app = AppUiModel(app.name, app.packageName, app.isBlocked),
                        onToggle = { isBlocked -> onToggleBlock(AppUiModel(app.name, app.packageName, app.isBlocked), isBlocked) }
                    )
                }

                // Separator
                item { Spacer(modifier = Modifier.padding(8.dp)) }

                // Sections for other apps, grouped by category
                apps.categories.forEach { (category, categoryApps) ->
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
                                onToggle = { isBlocked -> onToggleBlock(AppUiModel(app.name, app.packageName, app.isBlocked), isBlocked) }
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
            .padding(16.dp)
            .background(Color.White),
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
