package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val ICON_BOX_SIZE = 56.dp
private val ICON_SIZE = 26.dp
private val PLUS_ICON_SIZE = 18.dp
private const val ICON_BOX_ALPHA = 0.12f
private val TITLE_SIZE = 15.sp
private val MESSAGE_SIZE = 13.sp

@Composable
fun ProfileSetupEmptyCard(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Radius.lg)

    Row(
        modifier =
            modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = stringResource(R.string.setup_add),
                    role = Role.Button,
                    onClick = onAdd,
                ).padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_BOX_SIZE)
                    .clip(RoundedCornerShape(Radius.lg))
                    .background(AppPrimary.copy(alpha = ICON_BOX_ALPHA)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.DEVICES.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Text(
                text = stringResource(R.string.setup_empty_title),
                color = AppTextPrimary,
                style = AppType.label(TITLE_SIZE),
            )
            Text(
                text = stringResource(R.string.setup_profile_empty_message),
                color = AppTextSecondary,
                style = AppType.body(MESSAGE_SIZE),
            )
        }

        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(PLUS_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun ProfileSetupEmptyCardPreview() {
    AppPreview {
        ProfileSetupEmptyCard(onAdd = {})
    }
}
