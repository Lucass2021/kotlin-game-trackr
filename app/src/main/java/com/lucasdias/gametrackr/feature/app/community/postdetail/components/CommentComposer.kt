package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.Inter
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.community.CommunityOptions
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

private val REACTION_SIZE = 22.sp
private val AVATAR_SIZE = 34.dp
private val FIELD_TEXT_SIZE = 15.sp
private val SEND_BUTTON_SIZE = 44.dp
private val SEND_ICON_SIZE = 20.dp
private const val DISABLED_ALPHA = 0.4f
private val BORDER_WIDTH = 1.dp

@Composable
fun CommentComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(AppSurfaceCard)) {
        HorizontalDivider(thickness = BORDER_WIDTH, color = AppOutline)
        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            items(CommunityOptions.quickReactions) { reaction ->
                Text(
                    text = reaction,
                    fontSize = REACTION_SIZE,
                    modifier =
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = { onDraftChange(draft + reaction) },
                        ),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = Spacing.lg, end = Spacing.lg, bottom = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            CommunityAvatar(start = CoverCyanStart, end = CoverCyanEnd, size = AVATAR_SIZE)
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .border(BORDER_WIDTH, AppOutline, CircleShape)
                        .padding(horizontal = Spacing.md, vertical = Spacing.xs),
            ) {
                if (draft.isEmpty()) {
                    Text(
                        text = stringResource(R.string.post_detail_comment_placeholder),
                        color = AppTextSecondary,
                        style = AppType.body(FIELD_TEXT_SIZE),
                    )
                }
                BasicTextField(
                    value = draft,
                    onValueChange = onDraftChange,
                    textStyle = TextStyle(color = AppTextPrimary, fontFamily = Inter, fontSize = FIELD_TEXT_SIZE),
                    cursorBrush = SolidColor(AppPrimary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            SendButton(enabled = draft.isNotBlank(), onSend = onSend)
        }
    }
}

@Composable
private fun SendButton(
    enabled: Boolean,
    onSend: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val label = stringResource(R.string.community_action_send)
    Box(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(SEND_BUTTON_SIZE)
                .clip(CircleShape)
                .background(if (enabled) AppPrimary else AppPrimary.copy(alpha = DISABLED_ALPHA))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onSend,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.SEND.image(),
            contentDescription = label,
            tint = AppOnPrimary,
            modifier = Modifier.size(SEND_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun CommentComposerPreview() {
    AppPreview {
        CommentComposer(draft = "Great post!", onDraftChange = {}, onSend = {})
    }
}
