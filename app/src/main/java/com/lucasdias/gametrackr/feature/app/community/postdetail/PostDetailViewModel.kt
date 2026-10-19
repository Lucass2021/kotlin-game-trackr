package com.lucasdias.gametrackr.feature.app.community.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.model.addingReply
import com.lucasdias.gametrackr.core.model.findComment
import com.lucasdias.gametrackr.core.model.replacingComment
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.data.community.CommunityRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val repository: CommunityRepository,
    post: CommunityPost,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostDetailUiState(post = post))
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val postId = post.id
    private var commentsJob: Job? = null
    private var isPostLikePending = false
    private val pendingCommentLikeIds = mutableSetOf<Long>()

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.hasLoadedComments || state.isLoadingComments) return
        loadComments()
    }

    fun loadComments() {
        commentsJob?.cancel()
        _uiState.update { it.copy(isLoadingComments = true, commentsFailed = false) }
        commentsJob =
            viewModelScope.launch {
                repository
                    .fetchPostComments(postId)
                    .onSuccess { comments ->
                        _uiState.update { it.copy(comments = comments, hasLoadedComments = true, isLoadingComments = false) }
                    }.onFailure { _uiState.update { it.copy(commentsFailed = true, isLoadingComments = false) } }
            }
    }

    fun onLike() {
        if (isPostLikePending) return
        isPostLikePending = true
        val previous = _uiState.value.post
        val liked = !previous.isLiked
        _uiState.update { it.copy(post = it.post.copy(isLiked = liked, likes = previous.likes + if (liked) 1 else -1)) }
        viewModelScope.launch {
            repository
                .togglePostLike(postId)
                .onSuccess { status ->
                    _uiState.update { it.copy(post = it.post.copy(isLiked = status.isLiked, likes = status.likes)) }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            post = it.post.copy(isLiked = previous.isLiked, likes = previous.likes),
                            toastMessage = R.string.community_like_error,
                        )
                    }
                }
            isPostLikePending = false
        }
    }

    fun onBookmark() {
        _uiState.update { it.copy(post = it.post.copy(isBookmarked = !it.post.isBookmarked)) }
    }

    fun onCommentsOpen() {
        _uiState.update { it.copy(showComments = true) }
    }

    fun onCommentsDismiss() {
        _uiState.update { it.copy(showComments = false) }
    }

    fun onDraftChange(draft: String) {
        _uiState.update { it.copy(draft = draft) }
    }

    fun onReplyTap(comment: PostComment) {
        _uiState.update { it.copy(replyingTo = comment) }
    }

    fun onReplyCancel() {
        _uiState.update { it.copy(replyingTo = null) }
    }

    fun onSend() {
        val state = _uiState.value
        val text = state.draft.trim()
        if (text.isEmpty() || state.isSubmitting) return
        val target = state.replyingTo
        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val result = if (target == null) repository.addComment(postId, text) else repository.replyToComment(postId, target.id, text)
            result
                .onSuccess { created ->
                    _uiState.update {
                        it.copy(
                            comments = if (target == null) it.comments + created else it.comments.addingReply(target.id, created),
                            draft = "",
                            replyingTo = null,
                            isSubmitting = false,
                        )
                    }
                }.onFailure { _uiState.update { it.copy(isSubmitting = false, toastMessage = R.string.post_detail_comment_error) } }
        }
    }

    fun onCommentLike(commentId: Long) {
        val previous = _uiState.value.comments.findComment(commentId) ?: return
        if (!pendingCommentLikeIds.add(commentId)) return
        val liked = !previous.isLiked
        replaceComment(commentId) { it.copy(isLiked = liked, likes = previous.likes + if (liked) 1 else -1) }
        viewModelScope.launch {
            repository
                .toggleCommentLike(postId, commentId)
                .onSuccess { status -> replaceComment(commentId) { it.copy(isLiked = status.isLiked, likes = status.likes) } }
                .onFailure {
                    replaceComment(commentId) { it.copy(isLiked = previous.isLiked, likes = previous.likes) }
                    _uiState.update { it.copy(toastMessage = R.string.community_like_error) }
                }
            pendingCommentLikeIds.remove(commentId)
        }
    }

    fun onDeleteRequest() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }

    fun onDeleteDismissed() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun onDeleteConfirmed() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
        viewModelScope.launch {
            val result = repository.deletePost(postId)
            if (result.isSuccess || result.exceptionOrNull() == ApiError.NotFound) {
                _uiState.update { it.copy(isDeleted = true) }
            } else {
                _uiState.update { it.copy(toastMessage = R.string.post_detail_delete_error) }
            }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun replaceComment(
        commentId: Long,
        transform: (PostComment) -> PostComment,
    ) {
        _uiState.update { it.copy(comments = it.comments.replacingComment(commentId, transform)) }
    }
}
