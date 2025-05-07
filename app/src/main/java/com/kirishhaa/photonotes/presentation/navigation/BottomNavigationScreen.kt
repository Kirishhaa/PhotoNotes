package com.kirishhaa.photonotes.presentation.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.kirishhaa.photonotes.R

//like bottom navigation screen
sealed class BottomNavigationScreen(
    val route: String,
    val icon: ImageVector
) {

    fun getTitle(context: Context): String {
        return when(this) {
            Camera -> context.getString(R.string.camera)
            Feedback -> context.getString(R.string.feedback)
            Folders -> context.getString(R.string.folders)
            GoogleMap -> context.getString(R.string.google_map)
        }
    }

    data object Folders : BottomNavigationScreen(
        route = "FOLDERS_SCREEN",
        icon = Icons.Default.Menu
    )

    data object Camera : BottomNavigationScreen(
        route = "CAMERA_SCREEN",
        icon = Icons.Default.Add
    )

    data object GoogleMap : BottomNavigationScreen(
        route = "GOOGLE_MAP_SCREEN",
        icon = Icons.Default.LocationOn
    )

    data object Feedback : BottomNavigationScreen(
        route = "FEEDBACK_SCREEN",
        icon = Icons.Default.Email
    )

    companion object {
        fun getAll() = listOf(Folders, Camera, GoogleMap, Feedback)
    }

}