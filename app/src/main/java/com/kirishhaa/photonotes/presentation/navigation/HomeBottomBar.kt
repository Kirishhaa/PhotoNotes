package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun HomeBottomBar(navController: NavHostController) {
    val screens = BottomNavigationScreen.getAll()
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarDestination = screens.any { it.route == currentDestination?.route }
    if(isBottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddScreen(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun RowScope.AddScreen(screen: BottomNavigationScreen, currentDestination: NavDestination?, navController: NavHostController) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title
            )
        },
        label = {
            Text(screen.title)
        }
    )
}