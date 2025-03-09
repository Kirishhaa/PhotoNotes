package com.kirishhaa.photonotes.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.ui.theme.AppTheme

@Preview
@Composable
fun EditButton(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(48.dp)
        .clip(CircleShape)
        .background(brush = Brush.horizontalGradient(colors = listOf(Color.Red, Color.Blue)))
        .then(modifier)) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.edit_vector),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        )
    }
}