package com.lucasdias.gametrackr.core.ui.anim

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.subtlePulse(
    maxScale: Float = 1.08f,
    minAlpha: Float = 0.82f,
    durationMillis: Int = 1100,
): Modifier =
    composed {
        val transition = rememberInfiniteTransition(label = "subtlePulse")
        val spec = infiniteRepeatable<Float>(tween(durationMillis, easing = EaseInOut), RepeatMode.Reverse)
        val scale by transition.animateFloat(1f, maxScale, spec, label = "pulseScale")
        val alpha by transition.animateFloat(1f, minAlpha, spec, label = "pulseAlpha")
        graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        }
    }
