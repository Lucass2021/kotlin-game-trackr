package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BACK_BUTTON_SIZE = 40.dp
private val BACK_ICON_SIZE = 22.dp
private val TITLE_SIZE = 18.sp

@Composable
fun UserProfileTopBar(
    username: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.user_profile_back)

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(BACK_BUTTON_SIZE)
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null,
                        onClickLabel = backLabel,
                        role = Role.Button,
                        onClick = onBack,
                    ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = backLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(BACK_ICON_SIZE),
            )
        }

        Text(
            text = username,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun UserProfileTopBarPreview() {
    AppPreview {
        UserProfileTopBar(username = "@avareyes", onBack = {})
    }
}
