package com.lucasdias.gametrackr.feature.app.community.postdetail

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.model.totalCount

data class PostDetailUiState(
    val post: CommunityPost,
    val comments: List<PostComment> = emptyList(),
    val hasLoadedComments: Boolean = false,
    val isLoadingComments: Boolean = false,
    val commentsFailed: Boolean = false,
    val showComments: Boolean = false,
    val draft: String = "",
    val replyingTo: PostComment? = null,
    val isSubmitting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isDeleted: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
) {
    val commentCount: Int
        get() = if (hasLoadedComments) comments.totalCount() else post.comments
}
