package com.lucasdias.gametrackr.data.community

object DiscoverMockData {
    const val MOCK_ID_OFFSET = 1_000_000L

    val categoryCommunities = CommunityMockData.all.map { it.copy(id = it.id + MOCK_ID_OFFSET) }
}
