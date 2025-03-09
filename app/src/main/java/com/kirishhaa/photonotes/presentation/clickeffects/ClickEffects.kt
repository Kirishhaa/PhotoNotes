package com.kirishhaa.photonotes.presentation.clickeffects

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.pulsateClick(clickable: Boolean, onClick: () -> Unit) = composed {
    var buttonState by remember {
        mutableStateOf(ButtonState.IDLE)
    }
    val scale by animateFloatAsState(if(buttonState == ButtonState.IDLE) 1f else 0.7f)
    if(clickable) {
        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(buttonState) {
                awaitPointerEventScope {
                    buttonState = if (buttonState == ButtonState.PRESSED) {
                        waitForUpOrCancellation()
                        ButtonState.IDLE
                    } else {
                        awaitFirstDown(false)
                        ButtonState.PRESSED
                    }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    } else this.alpha(0.5f)
}