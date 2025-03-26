package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.domain.Marker
import com.kirishhaa.photonotes.presentation.CommitButton
import com.kirishhaa.photonotes.presentation.EditButton

@Composable
fun MarkerDetailScreen(markerId: Int, goBack: () -> Unit) {
    val viewmodel: MarkerDetailViewModel = viewModel(factory = MarkerDetailViewModel.Factory(markerId))
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when(event) {
                is MarkerDetailEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                MarkerDetailEvent.GoBack -> goBack()
            }
        }
    }

    when {
        state.preloading -> LoadingScreen()
        else -> MarkerDetailScreen(
            state = state,
            onShowAddTagDialog = { show -> viewmodel.showAddTagDialog(show) },
            onAddTag = viewmodel::tryAddTag,
            onShowRemoveTagDialog = { show, removeTag -> viewmodel.showRemoveTagDialog(show, removeTag) },
            onRemoveTag = { removeTag -> viewmodel.tryRemoveTag(removeTag) },
            onEditModeChanged = { edit -> viewmodel.setEditMode(edit) },
            onSave = viewmodel::onSave,
            onDelete = viewmodel::onDelete,
            onSelectFolder = viewmodel::onSelectFolder,
            onRemoveFromFolder = viewmodel::onRemoveFromFolder
        )
    }
}

@Composable
private fun MarkerDetailScreen(
    state: MarkerDetailState,
    onShowAddTagDialog: (Boolean) -> Unit,
    onAddTag: (String) -> Unit,
    onShowRemoveTagDialog: (Boolean, String?) -> Unit,
    onRemoveTag: (String) -> Unit,
    onEditModeChanged: (Boolean) -> Unit,
    onSave: (String, String, String, String) -> Unit,
    onDelete: () -> Unit,
    onSelectFolder: (String) -> Unit,
    onRemoveFromFolder: () -> Unit
) {

    var markerNameValue by remember {
        mutableStateOf(state.marker?.name)
    }
    var markerCountryValue by remember {
        mutableStateOf(state.marker?.location?.country)
    }
    var markerTownValue by remember {
        mutableStateOf(state.marker?.location?.town)
    }

    var markerDescriptionValue by remember {
        mutableStateOf(state.marker?.description ?: "Description")
    }

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AsyncImage(
                model = state.marker?.filePath,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.error_vector),
                placeholder = painterResource(R.drawable.error_vector),
                modifier = Modifier.size(120.dp).clip(CircleShape)
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("MarkerName", fontSize = 24.sp, color = Color.Black)
                if (state.editing) {
                    TextField(
                        value = markerNameValue ?: "",
                        onValueChange = { markerNameValue = it },
                        modifier = Modifier.width(200.dp)
                    )
                } else {
                    Text(markerNameValue ?: "", fontSize = 24.sp)
                }
                Text("Country", fontSize = 24.sp, color = Color.Black)
                if (state.editing) {
                    TextField(
                        value = markerCountryValue ?: "",
                        onValueChange = { markerCountryValue = it },
                        modifier = Modifier.width(200.dp)
                    )
                } else {
                    Text(markerCountryValue ?: "", fontSize = 20.sp)
                }
                Text("Town", fontSize = 24.sp, color = Color.Black)
                if (state.editing) {
                    TextField(
                        value = markerTownValue ?: "",
                        onValueChange = { markerTownValue = it },
                        modifier = Modifier.width(200.dp)
                    )
                } else {
                    Text(markerTownValue ?: "", fontSize = 20.sp)
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        HorizontalDivider(Modifier.fillMaxWidth(0.9f), color = Color.Black, thickness = 2.dp)
        Spacer(Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp)
        ) {
            items(items = state.marker?.tags ?: emptyList(), key = { it }) { tag ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .pulsateClick(state.editing, { onShowRemoveTagDialog(true, tag) })
                        .padding(5.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                ) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = tag,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
            if(state.editing) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .pulsateClick(state.editing, { onShowAddTagDialog(true) })
                            .padding(5.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Yellow)
                    ) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "add Tag",
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        HorizontalDivider(Modifier.fillMaxWidth(0.9f), color = Color.Black, thickness = 2.dp)
        Spacer(Modifier.height(4.dp))

        Text(
            text = "Description",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(Modifier.height(4.dp))
        if(state.editing) {
            TextField(
                value = markerDescriptionValue,
                onValueChange = { markerDescriptionValue = it }
            )
        } else {
            Text(
                text = markerDescriptionValue,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Folder",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(140.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Cyan)
                .pulsateClick(
                    clickable = state.folders.isNotEmpty() && state.editing,
                    onClick = { showDropDownMenu = true }
                ),
            contentAlignment = Alignment.Center
        ) {
            val text = state.marker?.folderName ?: "None"
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.Black,
            )

            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = { showDropDownMenu = false }
            ) {
                state.folders.forEach { folder ->
                    DropdownMenuItem(
                        text = { Text(folder.name) },
                        onClick = {
                            onSelectFolder(folder.name)
                            showDropDownMenu = false
                        }
                    )
                }
            }

        }

        Spacer(Modifier.height(12.dp))
        if(state.marker?.folderName != null) {
            Button(
                onClick = { onRemoveFromFolder() }
            ) {
                Text("Remove From Folder")
            }
        }

        Spacer(Modifier.weight(1f))
        if(state.editing.not()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { onSave(markerNameValue ?: "", markerCountryValue ?: "", markerTownValue ?: "", markerDescriptionValue) }
                ) {
                    Text("Save")
                }
                Button(
                    onClick = { onDelete() }
                ) {
                    Text("Delete")
                }
            }
        }
    }
    if(state.showAddTagDialog) {
        AddTagDialog(
            onAdd = onAddTag,
            onDismiss = { onShowAddTagDialog(false) }
        )
    } else if(state.showRemoveTagDialog) {
        RemoveTagDialog(
            tag = state.requireRemoveTag(),
            onRemove = { removeTag -> onRemoveTag(removeTag) },
            onDismiss = { onShowRemoveTagDialog(false, null) }
        )
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
            if(state.editing.not()) {
                EditButton(
                    modifier = Modifier.pulsateClick(clickable = state.editing.not(), onClick = { onEditModeChanged(true) })
                )
            } else {
                CommitButton(
                    modifier = Modifier.pulsateClick(clickable = state.editing, onClick = { onEditModeChanged(false) })
                )
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
        CircularProgressIndicator()
    }
}