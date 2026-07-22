package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R

@OptIn(ExperimentalTextApi::class)
private fun sora(
    weight: FontWeight,
    axis: Int,
) = Font(R.font.sora, weight, variationSettings = FontVariation.Settings(FontVariation.weight(axis)))

@OptIn(ExperimentalTextApi::class)
private fun inter(
    weight: FontWeight,
    axis: Int,
) = Font(R.font.inter, weight, variationSettings = FontVariation.Settings(FontVariation.weight(axis)))

val Sora =
    FontFamily(
        sora(FontWeight.Normal, 400),
        sora(FontWeight.Medium, 500),
        sora(FontWeight.SemiBold, 600),
        sora(FontWeight.Bold, 700),
        sora(FontWeight.ExtraBold, 800),
    )

val Inter =
    FontFamily(
        inter(FontWeight.Normal, 400),
        inter(FontWeight.Medium, 500),
        inter(FontWeight.SemiBold, 600),
        inter(FontWeight.Bold, 700),
    )

object AppType {
    fun headline(
        size: TextUnit,
        weight: FontWeight = FontWeight.Bold,
    ) = TextStyle(fontFamily = Sora, fontSize = size, fontWeight = weight)

    fun body(
        size: TextUnit,
        weight: FontWeight = FontWeight.Normal,
    ) = TextStyle(fontFamily = Inter, fontSize = size, fontWeight = weight)

    fun label(
        size: TextUnit = 16.sp,
        weight: FontWeight = FontWeight.SemiBold,
    ) = TextStyle(fontFamily = Inter, fontSize = size, fontWeight = weight)
}

val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
    )
