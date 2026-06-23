package com.lucasdias.gametrackr.core.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glow(
    color: Color,
    cornerRadius: Dp,
    blurRadius: Dp = 26.dp,
    spread: Dp = 2.dp,
    alpha: Float = 0.6f
): Modifier = drawBehind {
    val paint = Paint().apply {
        this.color = color.copy(alpha = alpha)
    }
    paint.asFrameworkPaint().maskFilter =
        BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)

    drawIntoCanvas { canvas ->
        val spreadPx = spread.toPx()
        val radiusPx = cornerRadius.toPx()
        canvas.nativeCanvas.drawRoundRect(
            -spreadPx,
            -spreadPx,
            size.width + spreadPx,
            size.height + spreadPx,
            radiusPx,
            radiusPx,
            paint.asFrameworkPaint()
        )
    }
}
