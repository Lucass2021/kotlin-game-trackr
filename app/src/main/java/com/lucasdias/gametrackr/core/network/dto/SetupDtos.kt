package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.SetupImage
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.network.resolveMediaUrl
import kotlinx.serialization.Serializable

@Serializable
data class SetupImageDto(
    val id: Int,
    val url: String,
)

@Serializable
data class SetupDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    val images: List<SetupImageDto> = emptyList(),
)

@Serializable
data class SetupResponse(
    val message: String? = null,
    val data: SetupDto,
)

@Serializable
data class SetupsResponse(
    val message: String? = null,
    val data: List<SetupDto>,
)

fun SetupDto.toDomain(apiBaseUrl: String) =
    SetupItem(
        id = id,
        title = title,
        description = description.orEmpty(),
        images = images.map { SetupImage(id = it.id, url = resolveMediaUrl(it.url, apiBaseUrl)) },
    )
