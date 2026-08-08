package com.lucasdias.gametrackr.core.ui

import android.content.Context
import android.content.Intent

private var lastShareTimestamp = 0L

fun Context.shareText(text: String) {
    val now = System.currentTimeMillis()
    if (now - lastShareTimestamp < 800) return
    lastShareTimestamp = now

    val sendIntent =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
    startActivity(Intent.createChooser(sendIntent, null))
}
