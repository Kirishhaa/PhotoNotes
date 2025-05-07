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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun MarkerView(marker: MarkerUI, onClick: (MarkerUI) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .pulsateClick(clickable = true, onClick = { onClick(marker) })
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorResource(R.color.secondary_container)),
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
            Text(
                text = marker.name,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.on_secondary_container)
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}