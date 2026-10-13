package com.lucasdias.gametrackr.core.pagination

data class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val lastPage: Int,
    val total: Int,
)
