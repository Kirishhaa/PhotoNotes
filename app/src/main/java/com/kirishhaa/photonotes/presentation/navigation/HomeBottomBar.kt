package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kirishhaa.photonotes.R

@Composable
fun HomeBottomBar(navController: NavHostController) {
    val screens = BottomNavigationScreen.getAll()
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (isBottomBarDestination) {
        NavigationBar(
            containerColor = colorResource(R.color.secondary_container),
            modifier = Modifier.clip(RoundedCornerShape(20.dp, 20.dp ,0.dp, 0.dp))
        ) {
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
private fun RowScope.AddScreen(
    screen: BottomNavigationScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val context = LocalContext.current
    NavigationBarItem(
        colors = NavigationBarItemColors(
            selectedIconColor = colorResource(R.color.primary_color),
            selectedTextColor = colorResource(R.color.on_surface),
            disabledTextColor = colorResource(R.color.on_surface),
            unselectedIconColor = colorResource(R.color.secondary_color),
            unselectedTextColor = colorResource(R.color.on_surface),
            disabledIconColor = colorResource(R.color.secondary_color),
            selectedIndicatorColor = colorResource(R.color.primary_container)
        ),
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        alwaysShowLabel = false,
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.getTitle(context)
            )
        },
        label = {
            Text(
                text = screen.getTitle(context),
                fontFamily = FontFamily(Font(R.font.comic)),

            )
        }
    )
}