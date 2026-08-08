package com.lucasdias.gametrackr.feature.app.community.createtopic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon

@Composable
fun CommunityPickerField(
    selection: Community?,
    options: List<Community>,
    onSelect: (Community) -> Unit,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.zIndex(if (expanded) 20f else 0f)) {
        Column {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fieldBox()
                        .height(50.dp)
                        .clickable(enabled = !isLocked) { expanded = !expanded }
                        .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (selection != null) {
                    CommunityIcon(
                        start = selection.iconStart,
                        end = selection.iconEnd,
                        size = 28.dp,
                        cornerRadius = 8.dp,
                    )
                    Text(
                        text = selection.name,
                        color = AppTextPrimary,
                        style = AppType.body(16.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.create_topic_community_placeholder),
                        color = AppTextSecondary,
                        style = AppType.body(16.sp),
                        modifier = Modifier.weight(1f),
                    )
                }

                if (!isLocked) {
                    Icon(
                        imageVector = AppIcon.CARET_DOWN.image(),
                        contentDescription = null,
                        tint = AppTextSecondary,
                        modifier = Modifier.size(16.dp).rotate(if (expanded) 180f else 0f),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                val shape = RoundedCornerShape(12.dp)
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, shape)
                            .clip(shape)
                            .background(AppSurfaceCard)
                            .border(1.dp, AppOutline, shape),
                ) {
                    if (options.isEmpty()) {
                        Text(
                            text = "You haven't joined any communities yet.",
                            color = AppTextSecondary,
                            style = AppType.body(14.sp),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 18.dp).fillMaxWidth(),
                        )
                    } else {
                        options.forEachIndexed { index, option ->
                            val interactionSource = remember { MutableInteractionSource() }
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                        ) {
                                            onSelect(option)
                                            expanded = false
                                        }.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                CommunityIcon(
                                    start = option.iconStart,
                                    end = option.iconEnd,
                                    size = 26.dp,
                                    cornerRadius = 8.dp,
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = option.name,
                                        color = if (option.id == selection?.id) AppPrimary else AppTextPrimary,
                                        style = AppType.body(16.sp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = option.subtitle,
                                        color = AppTextSecondary,
                                        style = AppType.body(12.sp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                            if (index < options.lastIndex) {
                                HorizontalDivider(color = AppOutline)
                            }
                        }
                    }
                }
            }
        }
    }
}
