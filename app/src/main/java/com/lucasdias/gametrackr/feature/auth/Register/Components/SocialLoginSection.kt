package com.lucasdias.gametrackr.feature.auth.register.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun SocialLoginSection(onGoogle: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(AppOutline))
            Text(
                text = stringResource(R.string.register_or_continue),
                color = AppTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(AppOutline))
        }
        GoogleButton(onGoogle = onGoogle)
    }
}

@Composable
private fun GoogleButton(onGoogle: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        label = "googlePressAlpha"
    )
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .pressScale(interactionSource, pressedScale = 0.96f)
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .alpha(alpha)
            .background(AppSurfaceCard)
            .border(1.dp, AppOutline, shape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onGoogle),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape).background(AppTextPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "G", color = AppOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.register_continue_google),
            color = AppTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
