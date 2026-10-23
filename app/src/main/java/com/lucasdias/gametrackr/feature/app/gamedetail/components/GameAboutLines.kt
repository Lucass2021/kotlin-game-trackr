package com.lucasdias.gametrackr.feature.app.gamedetail.components

object GameAboutLines {
    const val COLLAPSED = 3
    const val FULLY_SHOWN_MAX = 5

    fun maxLines(
        expanded: Boolean,
        collapsible: Boolean?,
    ): Int =
        when {
            collapsible == null -> FULLY_SHOWN_MAX
            expanded || !collapsible -> Int.MAX_VALUE
            else -> COLLAPSED
        }

    fun showsToggle(collapsible: Boolean?): Boolean = collapsible == true
}
