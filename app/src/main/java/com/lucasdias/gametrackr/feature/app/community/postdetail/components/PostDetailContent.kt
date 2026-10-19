package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val TITLE_SIZE = 28.sp
private val BODY_SIZE = 16.sp
private val BODY_LINE_HEIGHT = 24.sp
private const val BODY_ALPHA = 0.9f

@Composable
fun PostDetailContent(
    post: CommunityPost,
    commentCount: Int,
    isGuest: Boolean,
    onAuthorClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.lg, bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        PostDetailAuthorRow(post = post, onAuthorClick = onAuthorClick)
        PostDetailCommunityChip(name = post.communityName, onClick = onCommunityClick)
        Text(text = post.title, color = AppTextPrimary, style = AppType.headline(TITLE_SIZE, FontWeight.ExtraBold))
        Text(
            text = post.preview,
            color = AppTextPrimary.copy(alpha = BODY_ALPHA),
            style = AppType.body(BODY_SIZE),
            lineHeight = BODY_LINE_HEIGHT,
        )
        if (post.hasMedia) {
            PostDetailMedia(post = post)
        }
        PostDetailEngagementBar(
            post = post,
            commentCount = commentCount,
            isGuest = isGuest,
            onLike = onLike,
            onComment = onComment,
            onShare = onShare,
            onBookmark = onBookmark,
        )
        if (isGuest) {
            PostDetailGuestCard(onCreateAccount = onCreateAccount)
        }
    }
}

@Preview
@Composable
private fun PostDetailContentPreview() {
    AppPreview {
        PostDetailContent(
            post = CommunityMockData.detailPost,
            commentCount = CommunityMockData.detailPost.comments,
            isGuest = false,
            onAuthorClick = {},
            onCommunityClick = {},
            onLike = {},
            onComment = {},
            onShare = {},
            onBookmark = {},
            onCreateAccount = {},
        )
    }
}

@Preview
@Composable
private fun PostDetailContentGuestPreview() {
    AppPreview {
        PostDetailContent(
            post = CommunityMockData.detailPost,
            commentCount = CommunityMockData.detailPost.comments,
            isGuest = true,
            onAuthorClick = {},
            onCommunityClick = {},
            onLike = {},
            onComment = {},
            onShare = {},
            onBookmark = {},
            onCreateAccount = {},
        )
    }
}
