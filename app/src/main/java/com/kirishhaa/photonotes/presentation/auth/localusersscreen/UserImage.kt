package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.EditButton

@Composable
fun UserImage(
    model: Any?,
    modifier: Modifier = Modifier,
    edit: Boolean = false,
    onEdit: () -> Unit = {},
) {

    val mod = remember {
        if (edit) {
            Modifier
                .pulsateClick(true, onEdit)
                .then(modifier)
        } else modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = mod
    ) {
        AsyncImage(
            model = model,
            contentDescription = null,
            error = painterResource(R.drawable.empty_user_mock),
            placeholder = painterResource(R.drawable.empty_user_mock),
            modifier = Modifier.fillMaxSize()
        )
        if (edit) {
            EditButton(modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(20.dp))
        }
    }
}