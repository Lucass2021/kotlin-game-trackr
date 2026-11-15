package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.LikeStatus
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.model.addingReply
import com.lucasdias.gametrackr.core.model.findComment
import com.lucasdias.gametrackr.core.model.replacingComment

class FakeCommentStore(
    comments: List<PostComment>,
) {
    private val comments = comments.toMutableList()

    val all: List<PostComment> get() = comments.toList()

    fun find(id: Long): PostComment? = comments.findComment(id)

    fun add(content: String): PostComment = newComment(content).also { comments.add(it) }

    fun reply(
        parentId: Long,
        content: String,
    ): PostComment? {
        if (find(parentId) == null) return null
        return newComment(content).also { replace(comments.addingReply(parentId, it)) }
    }

    fun toggleLike(id: Long): LikeStatus? {
        val current = find(id) ?: return null
        val liked = !current.isLiked
        val status = LikeStatus(isLiked = liked, likes = current.likes + if (liked) 1 else -1)
        replace(comments.replacingComment(id) { it.copy(isLiked = status.isLiked, likes = status.likes) })
        return status
    }

    private fun newComment(content: String): PostComment {
        val nextId = (comments.flatMap { listOf(it) + it.replies }.maxOfOrNull { it.id } ?: 0) + 1
        return CommunityPostMockData.comments.first().copy(
            id = nextId,
            author = "@you",
            timeAgo = "now",
            content = content,
            likes = 0,
            isLiked = false,
            replies = emptyList(),
        )
    }

    private fun replace(updated: List<PostComment>) {
        comments.clear()
        comments.addAll(updated)
    }
}
