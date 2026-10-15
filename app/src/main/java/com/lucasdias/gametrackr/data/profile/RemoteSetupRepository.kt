package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.network.ProfileApi
import com.lucasdias.gametrackr.core.network.apiCall
import com.lucasdias.gametrackr.core.network.dto.toDomain
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RemoteSetupRepository(
    private val api: ProfileApi,
    private val json: Json,
) : SetupRepository {
    override suspend fun fetchSetups(): Result<List<SetupItem>> = apiCall(json) { api.getSetups().data.map { it.toDomain() } }

    override suspend fun createSetup(
        title: String,
        description: String,
        images: List<SetupImageUpload>,
    ): Result<SetupItem> = apiCall(json) { api.createSetup(setupParts(title, description, images)).data.toDomain() }

    override suspend fun updateSetup(
        id: Int,
        title: String,
        description: String,
        newImages: List<SetupImageUpload>,
        removedImageIds: List<Int>,
    ): Result<SetupItem> =
        apiCall(json) {
            val parts =
                setupParts(title, description, newImages) +
                    removedImageIds.map { MultipartBody.Part.createFormData(REMOVE_IMAGE_IDS_FIELD, it.toString()) }
            api.updateSetup(id, parts).data.toDomain()
        }

    override suspend fun deleteSetup(id: Int): Result<Unit> = apiCall(json) { api.deleteSetup(id) }

    private fun setupParts(
        title: String,
        description: String,
        images: List<SetupImageUpload>,
    ): List<MultipartBody.Part> =
        listOf(
            MultipartBody.Part.createFormData(TITLE_FIELD, title),
            MultipartBody.Part.createFormData(DESCRIPTION_FIELD, description),
        ) +
            images.map {
                MultipartBody.Part.createFormData(IMAGES_FIELD, it.fileName, it.bytes.toRequestBody(it.mimeType.toMediaType()))
            }

    private companion object {
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val IMAGES_FIELD = "images[]"
        const val REMOVE_IMAGE_IDS_FIELD = "remove_image_ids[]"
    }
}
