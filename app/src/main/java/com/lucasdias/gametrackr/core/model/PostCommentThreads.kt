package com.lucasdias.gametrackr.core.model

fun List<PostComment>.findComment(id: Long): PostComment? = flatMap { listOf(it) + it.replies }.firstOrNull { it.id == id }

fun List<PostComment>.replacingComment(
    id: Long,
    transform: (PostComment) -> PostComment,
): List<PostComment> =
    map { thread ->
        val updated = if (thread.id == id) transform(thread) else thread
        updated.copy(replies = updated.replies.map { if (it.id == id) transform(it) else it })
    }

fun List<PostComment>.addingReply(
    targetId: Long,
    reply: PostComment,
): List<PostComment> =
    map { thread ->
        val isTarget = thread.id == targetId || thread.replies.any { it.id == targetId }
        if (isTarget) thread.copy(replies = thread.replies + reply) else thread
    }
