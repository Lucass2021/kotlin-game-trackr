package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeDiscoverCommunitiesRepository(
    private val remote: CommunityRepository,
    private val categoryCommunities: List<Community> = DiscoverMockData.categoryCommunities,
    var failure: Throwable? = null,
) : DiscoverCommunitiesRepository {
    private val mockIds = categoryCommunities.map { it.id }.toSet()
    private val joinedMockIds = categoryCommunities.filter { it.isJoined }.map { it.id }.toMutableSet()

    var fetchCount = 0
        private set
    var lastSearch: String? = null
        private set
    var setJoinedCount = 0
        private set

    override suspend fun fetchCommunities(
        category: String?,
        search: String?,
        page: Int,
        perPage: Int,
    ): Result<Page<Community>> {
        fetchCount++
        lastSearch = search
        failure?.let { return Result.failure(it) }
        if (category == null) return remote.fetchCommunities(page = page, perPage = perPage, search = search)
        delay(FakeLatency.RESPONSE_DELAY_MS)
        val matches =
            categoryCommunities
                .filter { it.category == category && (search.isNullOrBlank() || it.name.contains(search, ignoreCase = true)) }
                .map { it.copy(isJoined = it.id in joinedMockIds) }
        return Result.success(fakePage(matches, page, perPage))
    }

    override suspend fun setJoined(
        communityId: Long,
        isJoined: Boolean,
    ): Result<Unit> {
        setJoinedCount++
        failure?.let { return Result.failure(it) }
        if (communityId !in mockIds) {
            return if (isJoined) remote.joinCommunity(communityId) else remote.leaveCommunity(communityId)
        }
        delay(FakeLatency.RESPONSE_DELAY_MS)
        if (isJoined) joinedMockIds.add(communityId) else joinedMockIds.remove(communityId)
        return Result.success(Unit)
    }
}
