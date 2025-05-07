package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.auth.localusersscreen.UserImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title: String, userImagePath: String?, navController: NavHostController) {
    val screens = BottomNavigationScreen.getAll()
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (isBottomBarDestination) {
        TopAppBar(
            title = { Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.on_surface)
            ) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.surface_color)
            ),
            actions = {
                UserImage(
                    model = userImagePath,
                    modifier = Modifier
                        .size(48.dp)
                        .pulsateClick(
                            clickable = true,
                            onClick = {
                                navController.navigate(GraphRoute.PROFILE)
                            }
                        )
                )
            }
        )
    }
}