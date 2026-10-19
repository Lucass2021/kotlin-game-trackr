package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

private val AVATAR_SIZE = 44.dp
private val AUTHOR_SIZE = 15.sp
private val TIME_SIZE = 13.sp

@Composable
fun PostDetailAuthorRow(
    post: CommunityPost,
    onAuthorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            modifier
                .pressScale(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = stringResource(R.string.post_detail_view_profile, post.author),
                    role = Role.Button,
                    onClick = onAuthorClick,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CommunityAvatar(start = post.avatarStart, end = post.avatarEnd, size = AVATAR_SIZE)
        Column(
            modifier = Modifier.padding(start = Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
        ) {
            Text(text = post.author, color = AppPrimary, style = AppType.label(AUTHOR_SIZE))
            Text(text = post.timeAgo, color = AppTextSecondary, style = AppType.body(TIME_SIZE))
        }
    }
}

@Preview
@Composable
private fun PostDetailAuthorRowPreview() {
    AppPreview {
        PostDetailAuthorRow(post = CommunityMockData.detailPost, onAuthorClick = {})
    }
}
