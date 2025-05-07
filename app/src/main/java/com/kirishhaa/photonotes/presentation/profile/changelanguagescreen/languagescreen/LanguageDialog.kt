package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun LanguageDialog(
    state: LanguageState,
    onPreviousLanguage: () -> Unit,
    onNextLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .then(modifier)
            .clip(RoundedCornerShape(20.dp))
            .background(color = colorResource(R.color.secondary_container)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.loading -> CircularProgressIndicator()
            else -> LanguageDialogLoaded(
                state.requireLanguage(),
                onPreviousLanguage,
                onNextLanguage
            )
        }
    }
}

@Composable
private fun LanguageDialogLoaded(
    language: LanguageUI,
    onPreviousLanguage: () -> Unit,
    onNextLanguage: () -> Unit
) {
    Text(
        text = language.getAsStringResource(),
        fontSize = 32.sp,
        fontFamily = FontFamily(Font(R.font.comic)),
        color = colorResource(R.color.primary_color)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.prev_arrow),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = colorResource(R.color.primary_color)),
            modifier = Modifier
                .size(48.dp)
                .pulsateClick(clickable = true, onClick = onPreviousLanguage)
                .background(
                    color = colorResource(R.color.tertiary_container),
                    shape = CircleShape
                )
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.next_arrow),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = colorResource(R.color.primary_color)),
            modifier = Modifier
                .size(48.dp)
                .pulsateClick(clickable = true, onClick = onNextLanguage)
                .background(
                    color = colorResource(R.color.tertiary_container),
                    shape = CircleShape
                )
        )
    }
}