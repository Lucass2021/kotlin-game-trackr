package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupItem
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
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupCover

private val CARD_WIDTH = 160.dp
private val CARD_HEIGHT = 120.dp
private val BORDER_WIDTH = 1.dp
private val ADD_ICON_SIZE = 24.dp
private val TITLE_SIZE = 14.sp
private val DESCRIPTION_SIZE = 12.sp
private val ADD_LABEL_SIZE = 13.sp

@Composable
fun ProfileSetupSection(
    setups: List<SetupItem>,
    onSelect: (SetupItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (setups.isEmpty()) {
        ProfileSetupEmptyCard(onAdd = onAdd, modifier = modifier.padding(horizontal = Spacing.lg))
        return
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        items(setups, key = { it.id }) { setup ->
            SetupCard(setup = setup, onClick = { onSelect(setup) })
        }

        item { AddCard(onClick = onAdd) }
    }
}

@Composable
private fun SetupCard(
    setup: SetupItem,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .width(CARD_WIDTH)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = setup.title,
                    role = Role.Button,
                    onClick = onClick,
                ),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        SetupCover(setup = setup, modifier = Modifier.width(CARD_WIDTH).height(CARD_HEIGHT))

        Text(
            text = setup.title,
            color = AppTextPrimary,
            style = AppType.label(TITLE_SIZE),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = setup.description,
            color = AppTextSecondary,
            style = AppType.body(DESCRIPTION_SIZE),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AddCard(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Radius.md)
    val label = stringResource(R.string.setup_add)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .width(CARD_WIDTH)
                .height(CARD_HEIGHT)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(ADD_ICON_SIZE),
        )
        Text(text = label, color = AppTextSecondary, style = AppType.body(ADD_LABEL_SIZE))
    }
}

@Preview
@Composable
private fun ProfileSetupSectionPreview() {
    AppPreview {
        ProfileSetupSection(setups = ProfileMockData.setups, onSelect = {}, onAdd = {})
    }
}

@Preview
@Composable
private fun ProfileSetupSectionEmptyPreview() {
    AppPreview {
        ProfileSetupSection(setups = emptyList(), onSelect = {}, onAdd = {})
    }
}
