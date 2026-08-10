package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.lucasdias.gametrackr.feature.app.profile.SetupItem
import com.lucasdias.gametrackr.feature.app.profile.setup.SetupCover

@Composable
fun ProfileSetupRow(
    setups: List<SetupItem>,
    onSelect: (SetupItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (setups.isEmpty()) {
        EmptyCard(onAdd = onAdd, modifier = modifier.padding(horizontal = 20.dp))
        return
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
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
                .width(160.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = setup.title,
                    role = Role.Button,
                    onClick = onClick,
                ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SetupCover(setup = setup, modifier = Modifier.width(160.dp).height(120.dp))

        Text(
            text = setup.title,
            color = AppTextPrimary,
            style = AppType.label(14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = setup.description,
            color = AppTextSecondary,
            style = AppType.body(12.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AddCard(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val label = stringResource(R.string.setup_add)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .width(160.dp)
                .height(120.dp)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(24.dp),
        )
        Text(text = label, color = AppTextSecondary, style = AppType.body(13.sp))
    }
}

@Composable
private fun EmptyCard(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier =
            modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = stringResource(R.string.setup_add),
                    role = Role.Button,
                    onClick = onAdd,
                ).padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.DEVICES.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(26.dp),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.setup_empty_title),
                color = AppTextPrimary,
                style = AppType.label(15.sp),
            )
            Text(
                text = stringResource(R.string.setup_profile_empty_message),
                color = AppTextSecondary,
                style = AppType.body(13.sp),
            )
        }

        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(18.dp),
        )
    }
}
