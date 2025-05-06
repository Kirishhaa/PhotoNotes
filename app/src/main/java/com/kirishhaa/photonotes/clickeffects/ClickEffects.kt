package com.kirishhaa.photonotes.clickeffects

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.flow.collectLatest

fun Modifier.pulsateClick(clickable: Boolean, onClick: () -> Unit) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    var pressed by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(0) {
        interactionSource.interactions.collectLatest { intection ->
            when (intection) {
                is PressInteraction.Press -> pressed = true
                else -> pressed = false
            }
        }
    }
    val scale by animateFloatAsState(if (pressed) 0.7f else 1f)
    if (clickable) {
        this
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    } else this.alpha(0.5f)
}