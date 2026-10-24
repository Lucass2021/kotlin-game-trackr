package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.CommunityPost
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PostUpdates {
    private val _posts = MutableSharedFlow<CommunityPost>(extraBufferCapacity = BUFFER_SIZE)
    val posts: SharedFlow<CommunityPost> = _posts.asSharedFlow()

    private val _createdPosts = MutableSharedFlow<CommunityPost>(extraBufferCapacity = BUFFER_SIZE)
    val createdPosts: SharedFlow<CommunityPost> = _createdPosts.asSharedFlow()

    fun publish(post: CommunityPost) {
        _posts.tryEmit(post)
    }

    fun publishCreated(post: CommunityPost) {
        _createdPosts.tryEmit(post)
    }

    private companion object {
        const val BUFFER_SIZE = 16
    }
}
