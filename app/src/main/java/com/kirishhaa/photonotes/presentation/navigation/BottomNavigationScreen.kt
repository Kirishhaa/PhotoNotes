package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

//like bottom navigation screen
sealed class BottomNavigationScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    data object Folders : BottomNavigationScreen(
        route = "FOLDERS_SCREEN",
        title = "Folders",
        icon = Icons.Default.Menu
    )

    data object Camera : BottomNavigationScreen(
        route = "CAMERA_SCREEN",
        title = "Camera",
        icon = Icons.Default.Add
    )

    data object GoogleMap : BottomNavigationScreen(
        route = "GOOGLE_MAP_SCREEN",
        title = "GoogleMap",
        icon = Icons.Default.LocationOn
    )

    data object Feedback : BottomNavigationScreen(
        route = "FEEDBACK_SCREEN",
        title = "Feedback",
        icon = Icons.Default.Email
    )

    companion object {
        fun getAll() = listOf(Folders, Camera, GoogleMap, Feedback)
    }

}