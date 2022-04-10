package dev.marshi.jetalarm.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

class Nav(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    companion object {
        val navs = listOf(
            Nav(route = "home", label = "Home", icon = Icons.Default.Home),
            Nav(route = "log", label = "Log", icon = Icons.Default.List),
        )
    }
}