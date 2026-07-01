package com.lucasdias.gametrackr.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import kotlinx.coroutines.delay

private const val REDIRECT_SECONDS = 5

@Composable
fun SuccessScreen(
    title: String,
    subtitle: String,
    buttonText: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    statusTitle: String? = null,
    statusValue: String? = null,
) {
    var remaining by remember { mutableIntStateOf(REDIRECT_SECONDS) }

    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000)
            remaining--
        }
        onPrimary()
    }

    BackHandler { onPrimary() }

    AuthScreenScaffold(
        modifier = modifier,
        contentArrangement = Arrangement.Center,
        bottomBar = {
            PrimaryButton(
                text = buttonText,
                onClick = onPrimary,
                modifier = Modifier.staggeredAppear(4),
            )
            Text(
                text = stringResource(R.string.success_redirecting, remaining),
                color = AppTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .staggeredAppear(5),
            )
        },
    ) {
        SuccessBadge(modifier = Modifier.staggeredAppear(0))

        Text(
            text = title,
            color = AppPrimary,
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .staggeredAppear(1),
        )

        Text(
            text = subtitle,
            color = AppTextSecondary,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 8.dp, end = 8.dp)
                    .staggeredAppear(2),
        )

        if (statusTitle != null && statusValue != null) {
            StatusCard(
                title = statusTitle,
                value = statusValue,
                modifier =
                    Modifier
                        .padding(top = 28.dp)
                        .staggeredAppear(3),
            )
        }
    }
}

@Composable
private fun SuccessBadge(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(AppSecondary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(AppSecondary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = AppOnPrimary,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppSurfaceCard)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppSecondary.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.VerifiedUser,
                contentDescription = null,
                tint = AppSecondary,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(
            modifier =
                Modifier
                    .padding(start = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                color = AppTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
            )
            Text(
                text = value,
                color = AppTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
