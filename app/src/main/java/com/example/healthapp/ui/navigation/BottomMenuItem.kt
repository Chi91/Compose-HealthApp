package com.example.healthapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.ui.graphics.vector.ImageVector

// Für die BottomNavigation zuständig
sealed class BottomMenuItem(
    val route: String,
    val iconUnselected: ImageVector,
    val iconSelected: ImageVector,
    val title: String
) {
    object Home : BottomMenuItem(
        title = "Home",
        iconUnselected = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home,
        route = "Home"
    )

    object Info : BottomMenuItem(
        title = "Info",
        iconUnselected = Icons.Outlined.Info,
        iconSelected = Icons.Filled.Info,
        route = "Info"
    )

    object Share : BottomMenuItem(
        title = "Share",
        iconUnselected = Icons.Outlined.Share,
        iconSelected = Icons.Filled.Share,
        route = "Share"
    )
}


