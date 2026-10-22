package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeCommunityFeedRepository(
    private val remote: CommunityRepository,
    private val followingPosts: List<CommunityPost> = CommunityMockData.feed,
    var failure: Throwable? = null,
) : CommunityFeedRepository {
    private val bookmarkedIds = mutableSetOf<Long>()

    var fetchFeedCount = 0
        private set
    var setBookmarkCount = 0
        private set

    override suspend fun fetchFeed(
        filter: CommunityFeedFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<CommunityPost>> {
        fetchFeedCount++
        failure?.let { return Result.failure(it) }
        val result =
            when (filter) {
                CommunityFeedFilter.LATEST -> {
                    remote.fetchPosts(page = page, perPage = perPage, communityId = null)
                }

                CommunityFeedFilter.TOP_RATED -> {
                    remote
                        .fetchPosts(page = page, perPage = perPage, communityId = null)
                        .map { it.copy(items = it.items.sortedByDescending { post -> post.likes }) }
                }

                CommunityFeedFilter.FOLLOWING -> {
                    delay(FakeLatency.RESPONSE_DELAY_MS)
                    Result.success(fakePage(followingPosts, page, perPage))
                }
            }
        return result.map { it.copy(items = it.items.map(::withBookmark)) }
    }

    override suspend fun fetchSuggestedCommunity(): Result<Community?> {
        failure?.let { return Result.failure(it) }
        return remote
            .fetchCommunities(page = 1, perPage = SUGGESTION_POOL_SIZE, search = null)
            .map { page -> page.items.firstOrNull { !it.isJoined } }
    }

    override suspend fun setBookmark(
        postId: Long,
        isBookmarked: Boolean,
    ): Result<Unit> {
        setBookmarkCount++
        delay(FakeLatency.RESPONSE_DELAY_MS)
        failure?.let { return Result.failure(it) }
        if (isBookmarked) bookmarkedIds.add(postId) else bookmarkedIds.remove(postId)
        return Result.success(Unit)
    }

    fun isBookmarked(postId: Long): Boolean = postId in bookmarkedIds

    private fun withBookmark(post: CommunityPost): CommunityPost = post.copy(isBookmarked = post.id in bookmarkedIds)

    private companion object {
        const val SUGGESTION_POOL_SIZE = 30
    }
}
