package com.lucasdias.gametrackr.feature.app.community.postdetail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
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
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

@Composable
fun CommentComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(AppSurfaceCard)) {
        HorizontalDivider(thickness = 1.dp, color = AppOutline)

        LazyRow(
            contentPadding =
                androidx.compose.foundation.layout
                    .PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            items(CommunityMockData.quickReactions) { reaction ->
                Text(
                    text = reaction,
                    fontSize = 22.sp,
                    modifier =
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onDraftChange(draft + reaction) },
                        ),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CommunityAvatar(start = CoverCyanStart, end = CoverCyanEnd, size = 34.dp)

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .border(1.dp, AppOutline, CircleShape)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                if (draft.isEmpty()) {
                    Text(
                        text = "Add a comment…",
                        color = AppTextSecondary,
                        style = AppType.body(15.sp),
                    )
                }
                BasicTextField(
                    value = draft,
                    onValueChange = onDraftChange,
                    textStyle = TextStyle(color = AppTextPrimary, fontFamily = Inter, fontSize = 15.sp),
                    cursorBrush = SolidColor(AppPrimary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            val sendInteraction = remember { MutableInteractionSource() }
            val enabled = draft.isNotBlank()
            Box(
                modifier =
                    Modifier
                        .pressScale(sendInteraction)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (enabled) AppPrimary else AppPrimary.copy(alpha = 0.4f))
                        .clickable(
                            interactionSource = sendInteraction,
                            indication = null,
                            enabled = enabled,
                            onClickLabel = stringResource(R.string.community_action_send),
                            role = Role.Button,
                            onClick = onSend,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.SEND.image(),
                    contentDescription = stringResource(R.string.community_action_send),
                    tint = AppOnPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
