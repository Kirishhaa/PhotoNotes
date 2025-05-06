package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun GoogleMapScreen(
    onMarkerClicked: (Int) -> Unit
) {
    val viewmodel: GoogleMapViewModel = viewModel(factory = GoogleMapViewModel.Factory)
    val state by viewmodel.state.collectAsState()
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    ) {
        state.markers.forEach { marker ->
            val markerState = rememberUpdatedMarkerState(
                position = LatLng(marker.latitude, marker.longitude)
            )
            Marker(
                state = markerState,
                icon = marker.image,
                onClick = {
                    onMarkerClicked(marker.id)
                    true
                }
            )
        }
    }
}