package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt

fun String.toAppColor(fallback: Color = CoverVioletStart): Color =
    runCatching { Color((if (startsWith("#")) this else "#$this").toColorInt()) }
        .getOrDefault(fallback)

fun Color.toHexString(): String = String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())

fun Color.darkened(amount: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(toArgb(), hsl)
    hsl[2] = (hsl[2] * (1f - amount)).coerceIn(0f, 1f)
    return Color(ColorUtils.HSLToColor(hsl))
}
