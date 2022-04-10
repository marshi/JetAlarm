package dev.marshi.jetalarm.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.marshi.jetalarm.ui.log.LogScreen
import dev.marshi.jetalarm.ui.theme.JetAlarmTheme

@Composable
fun JetAlarmApp() {
    JetAlarmTheme {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavigator provides navController) {
            Scaffold(
                bottomBar = { JetAlarmBottomBar(Nav.navs) },
            ) {
                createNavHost(navController = navController)
            }
        }
    }
}

val LocalNavigator = compositionLocalOf<NavHostController> { error("navigation not found") }

@Composable
fun createNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { AlarmListScreen() }
        composable("log") { LogScreen() }
    }
}