package com.lucasdias.gametrackr.feature.auth.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppBackground

@Composable
fun WelcomeBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Image(
            painter = painterResource(R.drawable.welcome_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops =
                                arrayOf(
                                    0.0f to AppBackground.copy(alpha = 0.55f),
                                    0.35f to AppBackground.copy(alpha = 0.15f),
                                    0.75f to AppBackground.copy(alpha = 0.85f),
                                    1.0f to AppBackground,
                                ),
                        ),
                    ),
        )
    }
}
