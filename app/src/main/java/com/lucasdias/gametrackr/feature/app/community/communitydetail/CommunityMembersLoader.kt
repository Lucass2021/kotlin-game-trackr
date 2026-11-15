package com.lucasdias.gametrackr.feature.app.community.communitydetail

import com.lucasdias.gametrackr.data.community.CommunityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CommunityMembersLoader(
    private val scope: CoroutineScope,
    private val repository: CommunityRepository,
    private val communityId: Long,
    private val updateState: ((CommunityDetailUiState) -> CommunityDetailUiState) -> Unit,
) {
    private var job: Job? = null

    fun load() {
        job?.cancel()
        updateState { it.copy(membersFailed = false) }
        job =
            scope.launch {
                repository
                    .fetchCommunityMembers(communityId)
                    .onSuccess { members -> updateState { it.copy(members = members) } }
                    .onFailure { updateState { it.copy(membersFailed = true) } }
            }
    }
}
