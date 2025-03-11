package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.GoogleMap

@Composable
fun GoogleMapScreen() {
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    )
}