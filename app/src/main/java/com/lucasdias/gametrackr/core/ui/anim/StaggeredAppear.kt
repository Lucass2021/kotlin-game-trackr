package com.lucasdias.gametrackr.core.ui.anim

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

fun Modifier.staggeredAppear(
    index: Int,
    stepMillis: Int = 80,
): Modifier =
    composed {
        var shown by remember { mutableStateOf(false) }
        val translateY = with(LocalDensity.current) { 14.dp.toPx() }
        val spec = tween<Float>(durationMillis = 400, delayMillis = index * stepMillis, easing = EaseOut)

        val alpha by animateFloatAsState(if (shown) 1f else 0f, spec, label = "staggerAlpha")
        val offsetY by animateFloatAsState(if (shown) 0f else translateY, spec, label = "staggerOffset")

        LaunchedEffect(Unit) { shown = true }

        graphicsLayer {
            this.alpha = alpha
            translationY = offsetY
        }
    }
