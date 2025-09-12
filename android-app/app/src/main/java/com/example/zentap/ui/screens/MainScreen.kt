package com.example.zentap.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zentap.MainActivity
import com.example.zentap.ui.screens.analytics.AnalyticsScreen
import com.example.zentap.ui.screens.home.HomeScreen
import com.example.zentap.ui.screens.home.AppSelectionScreen
import com.example.zentap.ui.screens.settings.SettingsScreen
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.zentap.ui.screens.schedule.ScheduleScreen
import com.example.zentap.ui.screens.settings.TagManagementScreen
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
}

val items = listOf(
    Screen.Home,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val activity = LocalActivity.current as MainActivity
    val viewModel = activity.viewModel

    val isBlockerEnabled by viewModel.isOverallToggleOn.collectAsState()
    LaunchedEffect(isBlockerEnabled) {
        if (isBlockerEnabled) {
            if (navController.currentDestination?.route == "app_selection") {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            }
        }
    }

    Scaffold(
        // The entire bottomBar section is now commented out.
        // To add it back later, just remove the /* and */ comment markers.
        /*
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == screen.route || (screen.route == "home" && it.route == "app_selection")
                    } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = isSelected,
                        onClick = {
                            if (screen.route == "home") {
                                if (currentDestination?.route != "home") {
                                    navController.popBackStack(
                                        Screen.Home.route,
                                        inclusive = false
                                    )
                                }
                            } else {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
        */
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            // This padding will now adjust to the absence of the bottom bar.
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = activity.viewModel,
                    navController = navController
                )
            }
            composable("app_selection") {
                AppSelectionScreen(
                    viewModel = activity.viewModel,
                    navController = navController
                )
            }

            composable("analytics_screen") { AnalyticsScreen(viewModel = activity.viewModel) }
            composable("settings_screen") {
                SettingsScreen(
                    viewModel = activity.viewModel,
                    navController = navController
                )
            }
            composable("tag_management") {
                TagManagementScreen(
                    viewModel = activity.viewModel,
                    navController = navController
                )
            }
            composable("schedule_screen") {
                ScheduleScreen(viewModel = viewModel,
                    navController = navController)
            }
        }
    }
}