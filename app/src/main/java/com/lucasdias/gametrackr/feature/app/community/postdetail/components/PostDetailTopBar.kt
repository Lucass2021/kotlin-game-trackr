package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BUTTON_SIZE = 40.dp
private val ICON_SIZE = 22.dp
private val TITLE_SIZE = 20.sp
private val MENU_LABEL_SIZE = 15.sp
private val DIVIDER_THICKNESS = 1.dp

@Composable
fun PostDetailTopBar(
    isOwnPost: Boolean,
    onBack: () -> Unit,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TopBarButton(icon = AppIcon.BACK, label = stringResource(R.string.community_action_back), onClick = onBack)
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.app_name),
                color = AppPrimary,
                style = AppType.headline(TITLE_SIZE, FontWeight.ExtraBold),
            )
            Spacer(Modifier.weight(1f))
            if (isOwnPost) {
                Box {
                    TopBarButton(
                        icon = AppIcon.OVERFLOW,
                        label = stringResource(R.string.community_action_more),
                        onClick = { showMenu = true },
                    )
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        containerColor = AppSurfaceCard,
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(R.string.post_detail_delete_action),
                                    color = AppTertiary,
                                    style = AppType.label(MENU_LABEL_SIZE),
                                )
                            },
                            onClick = {
                                showMenu = false
                                onDeleteRequest()
                            },
                        )
                    }
                }
            } else {
                Spacer(Modifier.size(BUTTON_SIZE))
            }
        }
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = AppOutline)
    }
}

@Composable
private fun TopBarButton(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(BUTTON_SIZE)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = label,
            tint = AppTextPrimary,
            modifier = Modifier.size(ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun PostDetailTopBarPreview() {
    AppPreview {
        PostDetailTopBar(isOwnPost = true, onBack = {}, onDeleteRequest = {})
    }
}
