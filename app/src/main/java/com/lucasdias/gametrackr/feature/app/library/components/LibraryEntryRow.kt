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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt
import com.lucasdias.gametrackr.feature.app.library.LibraryEntry

@Composable
fun LibraryEntryRow(
    entry: LibraryEntry,
    modifier: Modifier = Modifier,
    onFavorite: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        GameCoverArt(start = entry.coverStart, end = entry.coverEnd, width = 62.dp, height = 82.dp)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = entry.title,
                color = AppTextPrimary,
                style = AppType.label(18.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                LibraryStatusBadge(status = entry.status)
                StarRating(rating = entry.rating)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (onFavorite != null) {
                val interaction = remember { MutableInteractionSource() }
                val label = if (entry.isFavorite) "Remove from favorites" else "Add to favorites"
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
                            ).size(18.dp),
                )
            }

            Text(
                text = stringResource(R.string.library_hours, entry.hours),
                color = AppTextSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
