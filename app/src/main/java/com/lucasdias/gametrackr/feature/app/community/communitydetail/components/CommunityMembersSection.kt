package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

private val AVATAR_SIZE = 44.dp
private val FORWARD_ICON_SIZE = 16.dp
private val DIVIDER_THICKNESS = 1.dp
private val NAME_SIZE = 15.sp
private val ROLE_SIZE = 13.sp

@Composable
fun CommunityMembersSection(
    members: List<CommunityMember>,
    modifier: Modifier = Modifier,
    onMemberClick: (CommunityMember) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        members.forEachIndexed { index, member ->
            val interactionSource = remember { MutableInteractionSource() }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .pressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = member.author,
                            role = Role.Button,
                            onClick = { onMemberClick(member) },
                        ).padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CommunityAvatar(start = member.avatarStart, end = member.avatarEnd, size = AVATAR_SIZE)
                Column(modifier = Modifier.padding(start = Spacing.sm).weight(1f)) {
                    Text(
                        text = member.author,
                        color = AppPrimary,
                        style = AppType.label(NAME_SIZE),
                    )
                    Text(
                        text = member.role,
                        color = AppTextSecondary,
                        style = AppType.body(ROLE_SIZE),
                    )
                }
                Icon(
                    imageVector = AppIcon.FORWARD.image(),
                    contentDescription = null,
                    tint = AppTextSecondary,
                    modifier = Modifier.size(FORWARD_ICON_SIZE),
                )
            }
            if (index != members.lastIndex) {
                HorizontalDivider(
                    thickness = DIVIDER_THICKNESS,
                    color = AppOutline,
                    modifier = Modifier.padding(horizontal = Spacing.lg),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityMembersSectionPreview() {
    AppPreview {
        CommunityMembersSection(members = CommunityMockData.members)
    }
}
