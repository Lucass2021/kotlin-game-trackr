package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 20.sp
private val COUNT_SIZE = 15.sp
private val CLOSE_BUTTON_SIZE = 32.dp
private val CLOSE_ICON_SIZE = 16.dp
private val DIVIDER_THICKNESS = 1.dp

@Composable
fun CommentsHeader(
    total: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val closeLabel = stringResource(R.string.community_action_close)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.md, bottom = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(text = stringResource(R.string.post_detail_comments_title), color = AppTextPrimary, style = AppType.headline(TITLE_SIZE))
            Text(text = total.toString(), color = AppTextSecondary, style = AppType.body(COUNT_SIZE))
            Spacer(Modifier.weight(1f))
            Box(
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .size(CLOSE_BUTTON_SIZE)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = closeLabel,
                            role = Role.Button,
                            onClick = onClose,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.CLOSE.image(),
                    contentDescription = closeLabel,
                    tint = AppTextSecondary,
                    modifier = Modifier.size(CLOSE_ICON_SIZE),
                )
            }
        }
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = AppOutline)
    }
}

@Preview
@Composable
private fun CommentsHeaderPreview() {
    AppPreview {
        CommentsHeader(total = 5, onClose = {})
    }
}
