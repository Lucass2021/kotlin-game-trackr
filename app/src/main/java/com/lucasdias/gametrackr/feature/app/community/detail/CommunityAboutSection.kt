package com.lucasdias.gametrackr.feature.app.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community

@Composable
fun CommunityAboutSection(
    community: Community,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = community.description,
            color = AppTextSecondary,
            style = AppType.body(15.sp),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(AppSurfaceCard)
                    .border(1.dp, AppOutline, shape),
        ) {
            InfoRow(icon = AppIcon.COMMUNITY, label = "Category", value = "#${community.category}")
            HorizontalDivider(thickness = 1.dp, color = AppOutline)
            InfoRow(icon = AppIcon.CALENDAR, label = "Created", value = "March 2024")
            HorizontalDivider(thickness = 1.dp, color = AppOutline)
            InfoRow(icon = AppIcon.MEDAL, label = "Moderators", value = "3")
        }
    }
}

@Composable
private fun InfoRow(
    icon: AppIcon,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = label,
            color = AppTextSecondary,
            style = AppType.body(15.sp),
            modifier = Modifier.padding(start = 12.dp).weight(1f),
        )
        Text(
            text = value,
            color = AppTextPrimary,
            style = AppType.label(15.sp),
        )
    }
}
