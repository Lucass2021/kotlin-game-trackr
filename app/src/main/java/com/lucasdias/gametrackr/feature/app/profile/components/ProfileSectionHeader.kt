package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 20.sp
private val ACTION_SIZE = 15.sp

@Composable
fun ProfileSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionTitle: String? = null,
    onAction: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.weight(1f),
        )

        if (actionTitle != null) {
            Text(
                text = actionTitle,
                color = AppPrimary,
                style = AppType.label(ACTION_SIZE),
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            onClick = onAction,
                        ),
            )
        }
    }
}

@Preview
@Composable
private fun ProfileSectionHeaderPreview() {
    AppPreview {
        ProfileSectionHeader(title = "Favorites", actionTitle = "View all")
    }
}
