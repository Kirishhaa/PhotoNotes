package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun MarkerView(marker: MarkerUI, onClick: (MarkerUI) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp)
            .pulsateClick(clickable = true, onClick = { onClick(marker) })
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.Gray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            AsyncImage(
                model = marker.imagePath,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.error_vector),
                placeholder = painterResource(R.drawable.error_vector),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(marker.name, fontSize = 18.sp)
        }
        Spacer(Modifier.height(8.dp))
    }
}