package com.kirishhaa.photonotes.presentation.languagescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun LanguageDialog(
    state: LanguageStateUI,
    onPreviousLanguage: () -> Unit,
    onNextLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .height(250.dp).then(modifier)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(colors = listOf(Color.Gray, Color.Green))),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.loading -> CircularProgressIndicator()
            else -> LanguageDialogLoaded(state.requireChosenLanguage(), onPreviousLanguage, onNextLanguage)
        }
    }
}

@Composable
private fun LanguageDialogLoaded(language: LanguageUI, onPreviousLanguage: () -> Unit, onNextLanguage: () -> Unit) {
    Text(language.getAsStringResource(), fontSize = 16.sp)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.prev_arrow),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
                .pulsateClick(clickable = true, onClick = onPreviousLanguage)
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.next_arrow),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
                .pulsateClick(clickable = true, onClick = onNextLanguage)
        )
    }
}