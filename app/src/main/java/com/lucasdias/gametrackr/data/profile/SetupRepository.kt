package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupItem

interface SetupRepository {
    suspend fun fetchSetups(): Result<List<SetupItem>>

    suspend fun createSetup(
        title: String,
        description: String,
        images: List<SetupImageUpload>,
    ): Result<SetupItem>

    suspend fun updateSetup(
        id: Int,
        title: String,
        description: String,
        newImages: List<SetupImageUpload>,
        removedImageIds: List<Int>,
    ): Result<SetupItem>

    suspend fun deleteSetup(id: Int): Result<Unit>
}
