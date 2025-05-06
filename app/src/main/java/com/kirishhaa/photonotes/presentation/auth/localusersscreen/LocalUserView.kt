package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun LocalUserView(localUser: LocalUserUI, clickable: Boolean, onClick: (LocalUserUI) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .width(275.dp)
            .height(80.dp)
            .padding(10.dp)
            .pulsateClick(clickable = clickable, onClick = { onClick(localUser) })
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(colors = listOf(Color.Blue, Color.Cyan)))
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
    ) {
        Spacer(Modifier.width(20.dp))
        UserImage(
            model = null,
            modifier = Modifier.size(40.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(localUser.username, fontSize = 20.sp)
        }
    }
}