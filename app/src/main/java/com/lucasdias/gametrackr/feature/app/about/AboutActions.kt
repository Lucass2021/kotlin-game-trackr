package com.lucasdias.gametrackr.feature.app.about

data class AboutActions(
    val onBack: () -> Unit = {},
    val onOpenTerms: () -> Unit = {},
    val onOpenPrivacy: () -> Unit = {},
)
