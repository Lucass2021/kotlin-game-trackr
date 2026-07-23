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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt
import com.lucasdias.gametrackr.feature.app.library.LibraryEntry

@Composable
fun ProfileFavoritesRow(
    favorites: List<LibraryEntry>,
    onSelect: (LibraryEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
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
                .width(104.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = entry.title,
                    role = Role.Button,
                    onClick = onSelect,
                ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        GameCoverArt(
            start = entry.coverStart,
            end = entry.coverEnd,
            width = 104.dp,
            height = 138.dp,
        )

        Text(
            text = entry.title,
            color = AppTextSecondary,
            style = AppType.body(13.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
