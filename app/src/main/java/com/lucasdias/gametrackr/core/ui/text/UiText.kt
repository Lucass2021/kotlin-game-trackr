package com.lucasdias.gametrackr.core.ui.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Resource(
        @param:StringRes val id: Int,
        val args: List<Any> = emptyList(),
    ) : UiText

    data class Raw(
        val text: String,
    ) : UiText
}

@Composable
fun UiText.asString(): String =
    when (this) {
        is UiText.Resource -> stringResource(id, *args.toTypedArray())
        is UiText.Raw -> text
    }

fun UiText.asString(context: Context): String =
    when (this) {
        is UiText.Resource -> context.getString(id, *args.toTypedArray())
        is UiText.Raw -> text
    }
