package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.library.LibraryMockData
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val BORDER_WIDTH = 1.dp
private val COVER_WIDTH = 62.dp
private val COVER_HEIGHT = 82.dp
private val FAVORITE_ICON_SIZE = 18.dp
private val TITLE_SIZE = 18.sp
private val HOURS_SIZE = 15.sp

@Composable
fun LibraryEntryRow(
    entry: LibraryEntry,
    modifier: Modifier = Modifier,
    onFavorite: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(Radius.lg)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameCoverArt(start = entry.coverStart, end = entry.coverEnd, width = COVER_WIDTH, height = COVER_HEIGHT)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = entry.title,
                color = AppTextPrimary,
                style = AppType.label(TITLE_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                LibraryStatusBadge(status = entry.status)
                StarRating(rating = entry.rating)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            if (onFavorite != null) {
                val interaction = remember { MutableInteractionSource() }
                val label =
                    stringResource(
                        if (entry.isFavorite) R.string.library_favorite_remove else R.string.library_favorite_add,
                    )
                Icon(
                    imageVector = AppIcon.LIKE.image(filled = entry.isFavorite),
                    contentDescription = label,
                    tint = if (entry.isFavorite) AppPrimary else AppTextSecondary,
                    modifier =
                        Modifier
                            .pressScale(interaction)
                            .clickable(
                                interactionSource = interaction,
                                indication = null,
                                onClickLabel = label,
                                role = Role.Button,
                                onClick = onFavorite,
                            ).size(FAVORITE_ICON_SIZE),
                )
            }

            Text(
                text = stringResource(R.string.library_hours, entry.hours),
                color = AppTextSecondary,
                fontSize = HOURS_SIZE,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview
@Composable
private fun LibraryEntryRowPreview() {
    AppPreview {
        LibraryEntryRow(entry = LibraryMockData.entries[0], onFavorite = {})
    }
}
