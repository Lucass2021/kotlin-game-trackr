package com.lucasdias.gametrackr.feature.app.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community

@Composable
fun CommunityStatsBar(
    community: Community,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Stat(value = community.members, label = "MEMBERS", modifier = Modifier.weight(1f))
        Divider()
        Stat(value = community.posts, label = "POSTS", modifier = Modifier.weight(1f))
        Divider()
        Stat(value = community.online, label = "ONLINE", isLive = true, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.width(1.dp).height(34.dp).background(AppOutline))
}

@Composable
private fun Stat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = value,
                color = AppTextPrimary,
                style = AppType.headline(19.sp),
            )
            if (isLive) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(AppPrimary))
            }
        }
        Text(
            text = label,
            color = AppTextSecondary,
            style = AppType.label(11.sp),
            letterSpacing = 0.8.sp,
        )
    }
}
