package com.lucasdias.gametrackr.support

import com.lucasdias.gametrackr.core.model.User
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

    fun user(
        id: Int = 1,
        name: String = "Lucas",
    ) = User(
        id = id,
        name = name,
        email = "lucas@example.com",
        username = "lucas",
        profileColor = "#7C5CFF",
    )
}
