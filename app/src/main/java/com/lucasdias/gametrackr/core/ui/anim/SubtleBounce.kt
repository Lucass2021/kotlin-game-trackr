package com.lucasdias.gametrackr.core.ui.anim

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.subtleBounce(
    distance: Dp = 7.dp,
    durationMillis: Int = 1300,
): Modifier =
    composed {
        val transition = rememberInfiniteTransition(label = "subtleBounce")
        val distancePx = with(LocalDensity.current) { distance.toPx() }
        val offsetY by transition.animateFloat(
            initialValue = 0f,
            targetValue = -distancePx,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "subtleBounceOffset",
        )
        graphicsLayer { translationY = offsetY }
    }
