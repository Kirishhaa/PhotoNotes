package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.EditButton

@Composable
fun FoldersScreen(onCloseApp: () -> Unit, toMarkerDetails: (Int) -> Unit) {
    val viewmodel: FoldersViewModel = viewModel(factory = FoldersViewModel.Factory)
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(9) {
        viewmodel.events.collect{ event ->
            when(val eventValue = event.getValue()) {
                FolderEvent.CloseApp -> onCloseApp()
                null -> {}
            }
        }
    }

    BackHandler { viewmodel.handleBackPress() }

    when(state.loadingState) {
        true -> LoadingScreen()
        false -> FoldersScreen(
            state = state,
            onMarkerClicked = { marker -> toMarkerDetails(marker.id) },
            onFolderClicked = viewmodel::selectFolder
        )
    }
}

@Composable
private fun FoldersScreen(state: FoldersState, onMarkerClicked: (MarkerUI) -> Unit, onFolderClicked: (FolderUI) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = state.folders, key = { it.name }) { folder ->
            FolderView(folder, onFolderClicked)
        }
        items(items = state.markers , key = { it.id.toString()+it.name+it.imagePath }) { marker ->
            MarkerView(marker, onMarkerClicked)
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .width(48.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter)
        ) {
            EditButton(
                modifier = Modifier.pulsateClick(clickable = true, onClick = {})
            )
        }
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