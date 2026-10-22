package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 22.sp
private val VIEW_ALL_SIZE = 15.sp

@Composable
fun HomeSectionHeader(
    title: String,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
        )
        Text(
            text = stringResource(R.string.home_view_all),
            color = AppPrimary,
            style = AppType.label(VIEW_ALL_SIZE),
            modifier =
                Modifier
                    .pressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = onViewAll,
                    ),
        )
    }
}

@Preview
@Composable
private fun HomeSectionHeaderPreview() {
    AppPreview {
        HomeSectionHeader(title = "New Releases", onViewAll = {})
    }
}
