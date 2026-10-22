package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.pagination.Page

interface DiscoverCommunitiesRepository {
    suspend fun fetchCommunities(
        category: String?,
        search: String?,
        page: Int,
        perPage: Int,
    ): Result<Page<Community>>

    suspend fun setJoined(
        communityId: Long,
        isJoined: Boolean,
    ): Result<Unit>
}
