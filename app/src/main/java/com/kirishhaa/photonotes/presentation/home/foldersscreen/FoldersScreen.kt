package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.EditButton

@Composable
fun FoldersScreen(onCloseApp: () -> Unit, toMarkerDetails: (Int) -> Unit) {
    val viewmodel: FoldersViewModel = viewModel(factory = FoldersViewModel.Factory)
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(9) {
        viewmodel.events.collect { event ->
            when (val eventValue = event.getValue()) {
                FolderEvent.CloseApp -> onCloseApp()
                null -> {}
            }
        }
    }

    BackHandler { viewmodel.handleBackPress() }

    when {
        state.loadingState -> LoadingScreen()
        state.showAddFolderDialog.not() -> FoldersScreen(
            state = state,
            onMarkerClicked = { marker -> toMarkerDetails(marker.id) },
            onFolderClicked = viewmodel::selectFolder,
            onEdit = viewmodel::showAddFolderDialog,
            onRemoveFolder = viewmodel::onRemoveFolder
        )
    }

    AnimatedVisibility(
        visible = state.showAddFolderDialog
    ) {
        AddFolderDialog(
            onDismiss = viewmodel::hideAddFolderDialog,
            onAdd = viewmodel::addNewFolder
        )
    }

}

@Composable
private fun FoldersScreen(
    state: FoldersState,
    onMarkerClicked: (MarkerUI) -> Unit,
    onFolderClicked: (FolderUI) -> Unit,
    onEdit: () -> Unit,
    onRemoveFolder: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            state.selectedFolder?.let { folder ->
                item {
                    Text(
                        text = "Folder ${folder.name}",
                        color = colorResource(R.color.primary_color),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            items(items = state.folders, key = { it.id }) { folder ->
                FolderView(folder, onFolderClicked)
            }
            items(
                items = state.markers,
                key = { it.id.toString() + it.name + it.imagePath }) { marker ->
                MarkerView(marker, onMarkerClicked)
            }
        }
        state.selectedFolder?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                    .offset(y = -5.dp)
                    .pulsateClick(clickable = true, onClick = { onRemoveFolder() })
                    .background(
                        color = colorResource(R.color.on_secondary_container),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.remove_folder),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }

    AnimatedVisibility(visible = state.showEditButton) {
        Box(Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -5.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(160.dp)
                        .height(45.dp)
                        .align(Alignment.CenterHorizontally)
                        .pulsateClick(clickable = true, onClick = { onEdit() })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = "New Folder",
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.primary_color)
        )
    }
}