package com.lucasdias.gametrackr.feature.app.profile.setup

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

const val SETUP_PHOTO_LIMIT = 6

private const val MAX_PHOTO_DIMENSION = 1200

@Composable
fun rememberSetupPhoto(uri: Uri): ImageBitmap? {
    val context = LocalContext.current
    return produceState<ImageBitmap?>(initialValue = null, key1 = uri) {
        value = withContext(Dispatchers.IO) { decodeDownsampled(context, uri) }
    }.value
}

private fun decodeDownsampled(
    context: Context,
    uri: Uri,
): ImageBitmap? =
    runCatching {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }

        var sampleSize = 1
        while (max(bounds.outWidth, bounds.outHeight) / sampleSize > MAX_PHOTO_DIMENSION) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        context.contentResolver
            .openInputStream(uri)
            ?.use { BitmapFactory.decodeStream(it, null, options) }
            ?.asImageBitmap()
    }.getOrNull()
