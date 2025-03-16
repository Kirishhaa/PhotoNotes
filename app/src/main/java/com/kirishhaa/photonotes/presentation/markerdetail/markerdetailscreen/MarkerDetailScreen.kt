package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.domain.Marker

@Composable
fun MarkerDetailScreen(markerId: Int) {
    val viewmodel: MarkerDetailViewModel = viewModel(factory = MarkerDetailViewModel.Factory(markerId))
    val state by viewmodel.state.collectAsState()

    when {
        state.preloading -> LoadingScreen()
        else -> MarkerDetailScreen(state.requireMarker())
    }
}

@Composable
private fun MarkerDetailScreen(marker: Marker) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = marker.filePath,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text("lon = ${marker.location?.longitude}")
        Spacer(Modifier.height(20.dp))
        Text("lat = ${marker.location?.latitude}")
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}