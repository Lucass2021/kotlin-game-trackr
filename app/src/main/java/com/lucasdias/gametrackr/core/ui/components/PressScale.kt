package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.pressScale(
    interactionSource: InteractionSource,
    pressedScale: Float = 0.97f,
): Modifier =
    composed {
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed) pressedScale else 1f,
            animationSpec =
                spring(
                    dampingRatio = 0.6f,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            label = "pressScale",
        )
        graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    }
