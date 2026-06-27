package com.lucasdias.gametrackr.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TRACK_WIDTH_DP = 220

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val iconScale = remember { Animatable(0.5f) }
    val iconAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val subtitleOffset = remember { Animatable(15f) }
    val progressAlpha = remember { Animatable(0f) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            iconScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
            )
        }
        launch { iconAlpha.animateTo(1f, tween(durationMillis = 400)) }
        launch {
            delay(400)
            titleAlpha.animateTo(1f, tween(durationMillis = 600))
        }
        launch {
            delay(600)
            launch { subtitleAlpha.animateTo(1f, tween(durationMillis = 600)) }
            subtitleOffset.animateTo(0f, tween(durationMillis = 600))
        }
        launch {
            delay(900)
            progressAlpha.animateTo(1f, tween(durationMillis = 400))
        }
        launch {
            delay(1100)
            progress.animateTo(1f, tween(durationMillis = 1800, easing = FastOutSlowInEasing))
            onAnimationFinished()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(iconScale.value)
                    .alpha(iconAlpha.value)
                    .clip(RoundedCornerShape(22.dp))
                    .background(AppSurfaceCard)
                    .border(0.6.dp, AppOutline, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                color = AppTextPrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .alpha(titleAlpha.value)
            )

            Text(
                text = stringResource(R.string.splash_tagline),
                color = AppSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .alpha(subtitleAlpha.value)
                    .graphicsLayer { translationY = subtitleOffset.value }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .alpha(progressAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(TRACK_WIDTH_DP.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(AppSurfaceCard)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.value)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(AppPrimary)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                color = AppTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    GameTrackrTheme {
        SplashScreen(onAnimationFinished = {})
    }
}
