package com.lucasdias.gametrackr.feature.app.community.detail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunityMember
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

@Composable
fun CommunityMembersSection(
    modifier: Modifier = Modifier,
    onMemberClick: (CommunityMember) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CommunityMockData.members.forEachIndexed { index, member ->
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
                        ).padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CommunityAvatar(start = member.avatarStart, end = member.avatarEnd, size = 44.dp)
                Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                    Text(
                        text = member.author,
                        color = AppPrimary,
                        style = AppType.label(15.sp),
                    )
                    Text(
                        text = member.role,
                        color = AppTextSecondary,
                        style = AppType.body(13.sp),
                    )
                }
                Icon(
                    imageVector = AppIcon.FORWARD.image(),
                    contentDescription = null,
                    tint = AppTextSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
            if (index != CommunityMockData.members.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = AppOutline,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}
