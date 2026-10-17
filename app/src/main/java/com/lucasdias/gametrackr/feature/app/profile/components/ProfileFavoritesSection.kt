package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val COVER_WIDTH = 104.dp
private val COVER_HEIGHT = 138.dp
private val TITLE_SIZE = 13.sp

@Composable
fun ProfileFavoritesSection(
    favorites: List<LibraryEntry>,
    onSelect: (LibraryEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        items(favorites, key = { it.title }) { entry ->
            FavoriteCard(entry = entry, onSelect = { onSelect(entry) })
        }
    }
}

@Composable
private fun FavoriteCard(
    entry: LibraryEntry,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .width(COVER_WIDTH)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = entry.title,
                    role = Role.Button,
                    onClick = onSelect,
                ),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        GameCoverArt(
            start = entry.coverStart,
            end = entry.coverEnd,
            width = COVER_WIDTH,
            height = COVER_HEIGHT,
        )

        Text(
            text = entry.title,
            color = AppTextSecondary,
            style = AppType.body(TITLE_SIZE),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun ProfileFavoritesSectionPreview() {
    AppPreview {
        ProfileFavoritesSection(favorites = ProfileMockData.favorites, onSelect = {})
    }
}
