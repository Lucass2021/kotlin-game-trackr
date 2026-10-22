package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.pagination.Page

interface CommunityFeedRepository {
    suspend fun fetchFeed(
        filter: CommunityFeedFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<CommunityPost>>

    suspend fun fetchSuggestedCommunity(): Result<Community?>

    suspend fun setBookmark(
        postId: Long,
        isBookmarked: Boolean,
    ): Result<Unit>
}
