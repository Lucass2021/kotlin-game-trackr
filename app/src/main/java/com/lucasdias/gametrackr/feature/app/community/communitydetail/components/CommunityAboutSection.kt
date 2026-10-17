package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val BORDER_WIDTH = 1.dp
private val ICON_SIZE = 18.dp
private val BODY_SIZE = 15.sp
private val NOTE_SIZE = 13.sp

@Composable
fun CommunityAboutSection(
    community: Community,
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
    onDelete: () -> Unit = {},
) {
    val shape = RoundedCornerShape(Radius.lg)
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(
            text = community.description,
            color = AppTextSecondary,
            style = AppType.body(BODY_SIZE),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(AppSurfaceCard)
                    .border(BORDER_WIDTH, AppOutline, shape),
        ) {
            InfoRow(icon = AppIcon.COMMUNITY, label = stringResource(R.string.community_about_category), value = "#${community.category}")
            community.createdMonth?.let { createdMonth ->
                HorizontalDivider(thickness = BORDER_WIDTH, color = AppOutline)
                InfoRow(icon = AppIcon.CALENDAR, label = stringResource(R.string.community_about_created), value = createdMonth)
            }
        }

        if (isOwner) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(AppSurfaceCard)
                        .border(BORDER_WIDTH, AppOutline, shape)
                        .clickable(onClick = onDelete),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = AppIcon.TRASH.image(),
                        contentDescription = null,
                        tint = AppTertiary,
                        modifier = Modifier.size(ICON_SIZE),
                    )
                    Text(
                        text = stringResource(R.string.community_delete_action),
                        color = AppTertiary,
                        style = AppType.label(BODY_SIZE),
                        modifier = Modifier.padding(start = Spacing.sm),
                    )
                }
            }

            Text(
                text = stringResource(R.string.community_delete_owner_note),
                color = AppTextSecondary,
                style = AppType.body(NOTE_SIZE),
            )
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(ICON_SIZE),
        )
        Text(
            text = label,
            color = AppTextSecondary,
            style = AppType.body(BODY_SIZE),
            modifier = Modifier.padding(start = Spacing.sm).weight(1f),
        )
        Text(
            text = value,
            color = AppTextPrimary,
            style = AppType.label(BODY_SIZE),
        )
    }
}

@Preview
@Composable
private fun CommunityAboutSectionPreview() {
    AppPreview {
        CommunityAboutSection(community = CommunityMockData.detailCommunity, isOwner = true)
    }
}
