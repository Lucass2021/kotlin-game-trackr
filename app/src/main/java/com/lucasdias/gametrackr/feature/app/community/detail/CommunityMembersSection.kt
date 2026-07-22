package com.lucasdias.gametrackr.feature.app.community.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

@Composable
fun CommunityMembersSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        CommunityMockData.members.forEachIndexed { index, member ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CommunityAvatar(start = member.avatarStart, end = member.avatarEnd, size = 44.dp)
                Column(modifier = Modifier.padding(start = 14.dp)) {
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
