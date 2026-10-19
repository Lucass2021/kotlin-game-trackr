package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val LABEL_SIZE = 13.sp
private val CLOSE_ICON_SIZE = 14.dp
private val DIVIDER_THICKNESS = 1.dp

@Composable
fun ReplyIndicator(
    author: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cancelLabel = stringResource(R.string.post_detail_cancel_reply)
    Column(modifier = modifier.fillMaxWidth().background(AppBackground)) {
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = AppOutline)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.post_detail_replying_to, author),
                color = AppTextSecondary,
                style = AppType.label(LABEL_SIZE),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = AppIcon.CLOSE.image(),
                contentDescription = cancelLabel,
                tint = AppTextSecondary,
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = cancelLabel,
                            role = Role.Button,
                            onClick = onCancel,
                        ).size(CLOSE_ICON_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun ReplyIndicatorPreview() {
    AppPreview {
        ReplyIndicator(author = "@QuestMaster", onCancel = {})
    }
}
