package com.lucasdias.gametrackr.feature.app.about

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText

data class AboutUiState(
    val versionName: String,
) {
    val versionText: UiText get() = UiText.Resource(R.string.about_version, listOf(versionName))
}
