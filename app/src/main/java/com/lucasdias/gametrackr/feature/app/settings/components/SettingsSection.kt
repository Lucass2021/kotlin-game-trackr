package com.lucasdias.gametrackr.feature.app.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 13.sp
private val DIVIDER_INSET = Spacing.md + SETTINGS_ICON_BOX_WIDTH + Spacing.sm

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Text(
            text = title.uppercase(),
            color = AppTextSecondary,
            style = AppType.label(TITLE_SIZE),
            modifier = Modifier.padding(start = Spacing.xxs),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.lg))
                    .background(AppSurfaceCard),
            content = content,
        )
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(color = AppOutline, modifier = Modifier.padding(start = DIVIDER_INSET))
}

@Preview
@Composable
private fun SettingsSectionPreview() {
    AppPreview {
        SettingsSection(title = stringResource(R.string.settings_section_account)) {
            SettingsNavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.settings_change_password), onClick = {})
            SettingsDivider()
            SettingsDestructiveRow(
                icon = AppIcon.CLOSE,
                title = stringResource(R.string.settings_delete_account),
                isLoading = false,
                onClick = {},
            )
        }
    }
}
