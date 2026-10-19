package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.data.community.CommunityMockData

private const val ASPECT_RATIO = 16f / 9f
private val ICON_SIZE = 44.dp
private const val ICON_ALPHA = 0.15f
private val BORDER_WIDTH = 1.dp

@Composable
fun PostDetailMedia(
    post: CommunityPost,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.md)
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(ASPECT_RATIO)
                .clip(shape)
                .background(Brush.linearGradient(listOf(post.mediaStart, post.mediaEnd)))
                .border(BORDER_WIDTH, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = AppTextPrimary.copy(alpha = ICON_ALPHA),
            modifier = Modifier.size(ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun PostDetailMediaPreview() {
    AppPreview {
        PostDetailMedia(post = CommunityMockData.detailPost)
    }
}
