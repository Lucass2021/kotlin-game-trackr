package com.lucasdias.gametrackr.support

import com.lucasdias.gametrackr.core.network.dto.GameCoverDto
import com.lucasdias.gametrackr.core.network.dto.GameDto

object TestData {
    fun game(
        id: Int,
        name: String,
        cover: String? = null,
        releaseDate: Long? = null,
    ) = GameDto(
        id = id,
        name = name,
        slug = name.lowercase().replace(" ", "-"),
        firstReleaseDate = releaseDate,
        cover = cover?.let { GameCoverDto(id = 1, url = it) },
    )
}
