package com.lucasdias.gametrackr.feature.app.profilemenu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData

private val AVATAR_SIZE = 44.dp
private val NAME_SIZE = 18.sp
private val SUBTITLE_SIZE = 14.sp

@Composable
fun ProfileMenuAccountHeader(
    name: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(Radius.lg), modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = AppIcon.AVATAR.image(filled = true),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.size(AVATAR_SIZE),
            )
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
                if (name.isNotEmpty()) {
                    Text(text = name, color = AppTextPrimary, style = AppType.headline(NAME_SIZE))
                }
                if (subtitle.isNotEmpty()) {
                    Text(text = subtitle, color = AppTextSecondary, style = AppType.body(SUBTITLE_SIZE))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileMenuAccountHeaderPreview() {
    AppPreview {
        ProfileMenuAccountHeader(name = ProfileMockData.user.name, subtitle = ProfileMockData.user.email)
    }
}
