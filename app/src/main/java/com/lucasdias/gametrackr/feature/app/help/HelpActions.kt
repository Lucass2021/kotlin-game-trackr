package com.lucasdias.gametrackr.feature.app.help

data class HelpActions(
    val onBack: () -> Unit = {},
    val onSendFeedback: () -> Unit = {},
    val onReportBug: () -> Unit = {},
)
