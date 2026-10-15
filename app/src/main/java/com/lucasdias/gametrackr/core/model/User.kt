package com.lucasdias.gametrackr.core.model

import java.time.LocalDate

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val username: String? = null,
    val profileColor: String? = null,
    val bio: String? = null,
    val visibility: ProfileVisibility = ProfileVisibility.PUBLIC,
    val createdAt: LocalDate? = null,
)
