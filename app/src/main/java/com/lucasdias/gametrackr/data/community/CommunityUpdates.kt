package com.lucasdias.gametrackr.data.community

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class CommunityJoin(
    val communityId: Long,
    val isJoined: Boolean,
)

class CommunityUpdates {
    private val _joins = MutableSharedFlow<CommunityJoin>(extraBufferCapacity = BUFFER_SIZE)
    val joins: SharedFlow<CommunityJoin> = _joins.asSharedFlow()

    fun publish(
        communityId: Long,
        isJoined: Boolean,
    ) {
        _joins.tryEmit(CommunityJoin(communityId, isJoined))
    }

    private companion object {
        const val BUFFER_SIZE = 16
    }
}
