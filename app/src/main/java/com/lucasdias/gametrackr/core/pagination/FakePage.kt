package com.lucasdias.gametrackr.core.pagination

fun <T> fakePage(
    items: List<T>,
    page: Int,
    perPage: Int,
): Page<T> {
    val lastPage = maxOf(1, (items.size + perPage - 1) / perPage)
    val start = minOf((page - 1) * perPage, items.size)
    val end = minOf(start + perPage, items.size)
    return Page(
        items = items.subList(start, end),
        currentPage = page,
        lastPage = lastPage,
        total = items.size,
    )
}
