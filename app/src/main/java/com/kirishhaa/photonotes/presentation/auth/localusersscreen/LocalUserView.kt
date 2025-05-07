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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import org.w3c.dom.Text

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
            .background(color = colorResource(R.color.secondary_container))
            .border(width = 2.dp, color = colorResource(R.color.primary_color), shape = RoundedCornerShape(20.dp))
    ) {
        Spacer(Modifier.width(20.dp))
        UserImage(
            model = localUser.picturePath,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = localUser.username,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontFamily(Font(R.font.comic)),
            modifier = Modifier.weight(1f))
    }
}