package com.zentap.android.util

import androidx.navigation.NavController
import com.zentap.android.ui.screens.Screen

/**
 * A safe way to pop the back stack.
 *
 * This extension function checks if the current screen is the start destination.
 * If it is, the command is ignored, preventing a pop that would lead to a blank screen.
 * This effectively handles rapid double-taps on back buttons.
 */
fun NavController.safePopBackStack() {
    // Get the route of the current screen
    val currentRoute = this.currentBackStackEntry?.destination?.route

    // Only pop the back stack if the current screen is NOT the main home screen
    if (currentRoute != Screen.Home.route) {
        this.popBackStack()
    }
}