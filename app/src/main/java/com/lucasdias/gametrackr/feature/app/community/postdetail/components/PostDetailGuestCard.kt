package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 18.sp
private val MESSAGE_SIZE = 14.sp
private val ACTION_SIZE = 14.sp
private val BORDER_WIDTH = 1.dp

@Composable
fun PostDetailGuestCard(
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Radius.lg)
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.post_detail_guest_title),
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
        )
        Text(
            text = stringResource(R.string.post_detail_guest_message),
            color = AppTextSecondary,
            style = AppType.body(MESSAGE_SIZE),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.sm),
        )
        Text(
            text = stringResource(R.string.post_detail_guest_action),
            color = AppOnPrimary,
            style = AppType.label(ACTION_SIZE),
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .pressScale(interactionSource)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(interactionSource = interactionSource, indication = null, role = Role.Button, onClick = onCreateAccount)
                    .padding(horizontal = Spacing.xl, vertical = Spacing.sm),
        )
    }
}

@Preview
@Composable
private fun PostDetailGuestCardPreview() {
    AppPreview {
        PostDetailGuestCard(onCreateAccount = {})
    }
}
