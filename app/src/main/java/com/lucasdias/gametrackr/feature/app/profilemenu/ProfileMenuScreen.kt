package com.lucasdias.gametrackr.feature.app.profilemenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SecondaryButton
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.appshell.components.DetailTopBar
import com.lucasdias.gametrackr.feature.app.profile.Profile
import com.lucasdias.gametrackr.feature.app.profile.ProfileMockData

private data class MenuItem(
    val icon: AppIcon,
    val title: String,
    val onClick: () -> Unit = {},
)

@Composable
fun ProfileMenuScreen(
    isGuest: Boolean,
    userName: String?,
    email: String?,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onStats: () -> Unit,
    profile: Profile = ProfileMockData.profile,
) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        DetailTopBar(title = stringResource(R.string.menu_title), onBack = onBack)
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AccountHeader(isGuest = isGuest, userName = userName, email = email, profile = profile)

            MenuSection(
                listOf(
                    MenuItem(AppIcon.EDIT_PROFILE, stringResource(R.string.menu_edit_profile)),
                    MenuItem(AppIcon.CHART, stringResource(R.string.menu_my_stats), onStats),
                    MenuItem(AppIcon.MEDAL, stringResource(R.string.menu_achievements)),
                ),
            )

            MenuSection(
                listOf(
                    MenuItem(AppIcon.SETTINGS, stringResource(R.string.menu_settings)),
                    MenuItem(AppIcon.HELP, stringResource(R.string.menu_help)),
                    MenuItem(AppIcon.INFO, stringResource(R.string.menu_about)),
                ),
            )

            SessionSection(isGuest = isGuest, onLogout = onLogout)
        }
    }
}

@Composable
private fun AccountHeader(
    isGuest: Boolean,
    userName: String?,
    email: String?,
    profile: Profile,
) {
    val name =
        when {
            isGuest -> stringResource(R.string.menu_guest_name)
            !userName.isNullOrBlank() -> userName
            else -> profile.name
        }
    val subtitle =
        when {
            isGuest -> stringResource(R.string.menu_not_signed_in)
            !email.isNullOrBlank() -> email
            else -> profile.username
        }

    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = AppIcon.AVATAR.image(filled = true),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.size(44.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = name,
                    color = AppTextPrimary,
                    style = AppType.headline(18.sp),
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        color = AppTextSecondary,
                        style = AppType.body(14.sp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuSection(items: List<MenuItem>) {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                MenuRow(item)
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AppOutline,
                        modifier = Modifier.padding(start = 54.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuRow(item: MenuItem) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = item.title,
                    role = Role.Button,
                    onClick = item.onClick,
                ).padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = item.icon.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = item.title,
            color = AppTextPrimary,
            style = AppType.label(16.sp),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = AppIcon.FORWARD.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun SessionSection(
    isGuest: Boolean,
    onLogout: () -> Unit,
) {
    if (isGuest) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PrimaryButton(text = stringResource(R.string.menu_create_account), onClick = onLogout)
            SecondaryButton(text = stringResource(R.string.menu_exit_guest), onClick = onLogout)
        }
    } else {
        SecondaryButton(text = stringResource(R.string.sign_out), onClick = onLogout)
    }
    Spacer(modifier = Modifier.size(8.dp))
}
