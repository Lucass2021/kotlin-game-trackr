package com.lucasdias.gametrackr.feature.app.community.createtopic.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon

private val FIELD_HEIGHT = 50.dp
private val ICON_SIZE = 28.dp
private val CARET_SIZE = 16.dp
private val TEXT_SIZE = 16.sp
private const val OPEN_Z_INDEX = 20f
private const val CARET_OPEN_ROTATION = 180f

@Composable
fun CommunityPickerField(
    selection: Community?,
    options: List<Community>,
    onSelect: (Community) -> Unit,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    isLoading: Boolean = false,
    isFailed: Boolean = false,
    onRetry: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val placeholder = stringResource(R.string.create_topic_community_placeholder)

    Box(modifier = modifier.zIndex(if (expanded) OPEN_Z_INDEX else 0f)) {
        Column {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fieldBox()
                        .height(FIELD_HEIGHT)
                        .clickable(enabled = !isLocked, onClickLabel = placeholder, role = Role.Button) { expanded = !expanded }
                        .padding(horizontal = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                if (selection != null) {
                    CommunityIcon(start = selection.iconStart, end = selection.iconEnd, size = ICON_SIZE, cornerRadius = Radius.sm)
                    Text(
                        text = selection.name,
                        color = AppTextPrimary,
                        style = AppType.body(TEXT_SIZE),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Text(
                        text = placeholder,
                        color = AppTextSecondary,
                        style = AppType.body(TEXT_SIZE),
                        modifier = Modifier.weight(1f),
                    )
                }

                if (!isLocked) {
                    Icon(
                        imageVector = AppIcon.CARET_DOWN.image(),
                        contentDescription = null,
                        tint = AppTextSecondary,
                        modifier = Modifier.size(CARET_SIZE).rotate(if (expanded) CARET_OPEN_ROTATION else 0f),
                    )
                }
            }

            Spacer(Modifier.height(Spacing.xs))

            AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
                CommunityPickerOptions(
                    selection = selection,
                    options = options,
                    isLoading = isLoading,
                    isFailed = isFailed,
                    onSelect = { option ->
                        onSelect(option)
                        expanded = false
                    },
                    onRetry = onRetry,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityPickerFieldPreview() {
    AppPreview {
        CommunityPickerField(selection = null, options = CommunityMockData.all.filter { it.isJoined }, onSelect = {})
    }
}

@Preview
@Composable
private fun CommunityPickerFieldLockedPreview() {
    AppPreview {
        CommunityPickerField(selection = CommunityMockData.all.first(), options = emptyList(), onSelect = {}, isLocked = true)
    }
}
