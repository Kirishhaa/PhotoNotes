package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.CommitButton
import com.kirishhaa.photonotes.presentation.EditButton
import com.kirishhaa.photonotes.presentation.home.camerascreen.ComposeFileProvider

@Composable
fun MarkerDetailScreen(markerId: Int, goBack: () -> Unit) {
    val viewmodel: MarkerDetailViewModel =
        viewModel(factory = MarkerDetailViewModel.Factory(markerId))
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (event) {
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
            onShowRemoveTagDialog = { show, removeTag ->
                viewmodel.showRemoveTagDialog(
                    show,
                    removeTag
                )
            },
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
    onSelectFolder: (Int) -> Unit,
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

    val descriptionString = stringResource(R.string.description)

    var markerDescriptionValue by remember {
        mutableStateOf(state.marker?.description ?: descriptionString)
    }

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AsyncImage(
                model = state.marker?.filePath,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.error_vector),
                placeholder = painterResource(R.drawable.error_vector),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.markername),
                    fontSize = 28.sp,
                    color = colorResource(R.color.on_surface),
                    fontFamily = FontFamily(Font(R.font.comic))
                )
                if (state.editing) {
                    TextField(
                        value = markerNameValue ?: "",
                        onValueChange = { markerNameValue = it },
                        modifier = Modifier.width(200.dp),
                        colors = TextFieldDefaults.colors(
                            cursorColor = colorResource(R.color.primary_color),
                            focusedIndicatorColor = colorResource(R.color.primary_color)
                        )
                    )
                } else {
                    Text(
                        text = markerNameValue ?: "",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.comic)),
                        color = colorResource(R.color.on_surface)
                    )
                }
                Text(
                    text = stringResource(R.string.country),
                    fontSize = 28.sp,
                    color = colorResource(R.color.on_surface),
                    fontFamily = FontFamily(Font(R.font.comic))
                )
                if (state.editing) {
                    TextField(
                        value = markerCountryValue ?: "",
                        onValueChange = { markerCountryValue = it },
                        modifier = Modifier.width(200.dp),
                        colors = TextFieldDefaults.colors(
                            cursorColor = colorResource(R.color.primary_color),
                            focusedIndicatorColor = colorResource(R.color.primary_color)
                        )
                    )
                } else {
                    Text(
                        text = markerCountryValue ?: "",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.comic)),
                        color = colorResource(R.color.on_surface)
                    )
                }
                Text(
                    text = stringResource(R.string.town),
                    fontSize = 28.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.comic))
                )
                if (state.editing) {
                    TextField(
                        value = markerTownValue ?: "",
                        onValueChange = { markerTownValue = it },
                        modifier = Modifier.width(200.dp),
                        colors = TextFieldDefaults.colors(
                            cursorColor = colorResource(R.color.primary_color),
                            focusedIndicatorColor = colorResource(R.color.primary_color)
                        )
                    )
                } else {
                    Text(
                        text = markerTownValue ?: "",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.comic)),
                        color = colorResource(R.color.on_surface)
                    )
                }
            }
        }
        if(state.marker?.tags?.isNotEmpty() == true || state.editing) {
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(Modifier.fillMaxWidth(0.9f), color = Color.Black, thickness = 2.dp)
            Spacer(Modifier.height(20.dp))
        }

        if(state.marker?.tags?.isNotEmpty() == true || state.editing) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().height(300.dp),
            ) {
                items(items = state.marker?.tags ?: emptyList(), key = { it.toString() }) { tag ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(130.dp)
                            .height(46.dp)
                            .pulsateClick(state.editing, { onShowRemoveTagDialog(true, tag.name) })
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = colorResource(R.color.secondary_container))
                    ) {
                        Text(
                            text = tag.name,
                            fontSize = 24.sp,
                            color = colorResource(R.color.on_surface),
                            fontFamily = FontFamily(Font(R.font.comic))
                        )
                    }
                }
                if (state.editing) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(46.dp)
                                .pulsateClick(true, { onShowAddTagDialog(true) })
                                .background(
                                    color = colorResource(R.color.secondary_container),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            Text(
                                text = stringResource(R.string.add_tag),
                                fontSize = 24.sp,
                                color = colorResource(R.color.on_surface),
                                fontFamily = FontFamily(Font(R.font.comic))
                            )
                        }
                    }
                }
            }
        }
        if(state.marker?.tags?.isNotEmpty() == true || state.editing) {
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(Modifier.fillMaxWidth(0.9f), color = Color.Black, thickness = 2.dp)
            Spacer(Modifier.height(20.dp))
        }

        Text(
            text = stringResource(R.string.description),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface),
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(Modifier.height(4.dp))
        if (state.editing) {
            TextField(
                value = markerDescriptionValue,
                onValueChange = { markerDescriptionValue = it },
                colors = TextFieldDefaults.colors(
                    cursorColor = colorResource(R.color.primary_color),
                    focusedIndicatorColor = colorResource(R.color.primary_color)
                )
            )
        } else {
            Text(
                text = markerDescriptionValue,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(0.9f),
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.on_surface),
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.folder),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f),
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface),
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(24.dp))
                .pulsateClick(
                    clickable = state.folders.isNotEmpty() && state.editing,
                    onClick = { showDropDownMenu = true }
                )
                .background(color = colorResource(R.color.secondary_container)),
            contentAlignment = Alignment.Center
        ) {
            val text = state.marker?.folderName ?: stringResource(R.string.none)
            Text(
                text = text,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.on_surface),
            )

            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = { showDropDownMenu = false }
            ) {
                state.folders.forEach { folder ->
                    DropdownMenuItem(
                        text = { Text(
                            text = folder.name,
                            fontFamily = FontFamily(Font(R.font.comic)),
                            color = colorResource(R.color.on_surface),
                        ) },
                        onClick = {
                            onSelectFolder(folder.id)
                            showDropDownMenu = false
                        }
                    )
                }
            }

        }

        Spacer(Modifier.height(16.dp))
        if (state.marker?.folderName != null && state.editing) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp)
                    .pulsateClick(clickable = true, onClick = {
                        onRemoveFromFolder()
                    })
                    .background(
                        color = colorResource(R.color.on_secondary_container),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.remove_from_folder),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        if (state.editing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(130.dp)
                        .height(50.dp)
                        .pulsateClick(clickable = true, onClick = {
                            onSave(
                                markerNameValue ?: "",
                                markerCountryValue ?: "",
                                markerTownValue ?: "",
                                markerDescriptionValue
                            )
                        })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .pulsateClick(clickable = true, onClick = {
                            onDelete()
                        })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        if (state.editing.not()) {
            EditButton(
                modifier = Modifier.pulsateClick(
                    clickable = state.editing.not(),
                    onClick = { onEditModeChanged(true) })
            )
        } else {
            CommitButton(
                modifier = Modifier.pulsateClick(
                    clickable = state.editing,
                    onClick = { onEditModeChanged(false) })
            )
        }
    }
    if (state.showAddTagDialog) {
        AddTagDialog(
            onAdd = onAddTag,
            onDismiss = { onShowAddTagDialog(false) }
        )
    } else if (state.showRemoveTagDialog) {
        RemoveTagDialog(
            tag = state.requireRemoveTag(),
            onRemove = { removeTag -> onRemoveTag(removeTag) },
            onDismiss = { onShowRemoveTagDialog(false, null) }
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = colorResource(R.color.primary_color))
    }
}