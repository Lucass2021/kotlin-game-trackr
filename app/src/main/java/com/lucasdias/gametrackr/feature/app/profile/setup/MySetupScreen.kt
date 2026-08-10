package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.profile.SetupItem

@Composable
fun MySetupScreen(
    setups: List<SetupItem>,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (SetupItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Header(showAdd = setups.isNotEmpty(), onBack = onBack, onAdd = onAdd)

        if (setups.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CommunityEmptyState(
                    icon = AppIcon.DEVICES,
                    title = stringResource(R.string.setup_empty_title),
                    message = stringResource(R.string.setup_empty_message),
                    actionTitle = stringResource(R.string.setup_empty_action),
                    onAction = onAdd,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                items(setups, key = { it.id }) { setup ->
                    SetupCard(setup = setup, onClick = { onEdit(setup) })
                }
            }
        }
    }
}

@Composable
private fun Header(
    showAdd: Boolean,
    onBack: () -> Unit,
    onAdd: () -> Unit,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val addInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.setup_back)
    val addLabel = stringResource(R.string.setup_add)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null,
                        onClickLabel = backLabel,
                        role = Role.Button,
                        onClick = onBack,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = backLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(22.dp),
            )
        }

        Text(
            text = stringResource(R.string.setup_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        if (showAdd) {
            Box(
                modifier =
                    Modifier
                        .pressScale(addInteraction)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AppPrimary)
                        .clickable(
                            interactionSource = addInteraction,
                            indication = null,
                            onClickLabel = addLabel,
                            role = Role.Button,
                            onClick = onAdd,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.PLUS.image(),
                    contentDescription = addLabel,
                    tint = AppOnPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun SetupCard(
    setup: SetupItem,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = setup.title,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SetupCover(
            setup = setup,
            cornerRadius = 16,
            placeholderSize = 48,
            modifier = Modifier.fillMaxWidth().height(180.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = setup.title,
                color = AppTextPrimary,
                style = AppType.headline(18.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (setup.description.isNotEmpty()) {
                Text(
                    text = setup.description,
                    color = AppTextSecondary,
                    style = AppType.body(14.sp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
