package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val ICON_BOX_SIZE = 120.dp
private val ICON_SIZE = 56.dp
private val GLOW_BLUR = 34.dp
private const val GLOW_ALPHA = 0.35f
private val TITLE_SIZE = 24.sp
private val SUBTITLE_SIZE = 15.sp

@Composable
fun ProfileGuestState(
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(AppBackground).padding(horizontal = Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .size(ICON_BOX_SIZE)
                    .glow(color = AppPrimary, cornerRadius = Radius.xl, blurRadius = GLOW_BLUR, alpha = GLOW_ALPHA)
                    .clip(RoundedCornerShape(Radius.xl))
                    .background(AppSurfaceCard),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.AVATAR.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Text(
            text = stringResource(R.string.profile_guest_title),
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.padding(top = Spacing.xl),
        )

        Text(
            text = stringResource(R.string.profile_guest_subtitle),
            color = AppTextSecondary,
            style = AppType.body(SUBTITLE_SIZE),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.xs),
        )

        PrimaryButton(
            text = stringResource(R.string.profile_guest_action),
            onClick = onCreateAccount,
            modifier = Modifier.padding(top = Spacing.xl),
        )
    }
}

@Preview
@Composable
private fun ProfileGuestStatePreview() {
    AppPreview {
        ProfileGuestState(onCreateAccount = {})
    }
}
