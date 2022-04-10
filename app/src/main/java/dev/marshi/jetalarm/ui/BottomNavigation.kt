package dev.marshi.jetalarm.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun JetAlarmBottomBar(
    navs: List<Nav>,
) {
    val navController = LocalNavigator.current
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    BottomNavigation {
        navs.forEach { nav ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = nav.icon, contentDescription = null)
                },
                selected = currentDestination?.hierarchy?.any { it.route == nav.route } == true,
                label = { Text(nav.label) },
                onClick = { navController.navigate(nav.route) },
            )
        }
    }
}

@Preview
@Composable
fun JetAlarmAppPreview() {
    JetAlarmBottomBar(navs = Nav.navs)
}
