package com.lucasdias.gametrackr.feature.app.profile.setup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import com.lucasdias.gametrackr.core.model.SetupImageUpload
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.max

fun interface SetupPhotoEncoder {
    suspend fun encode(photo: String): SetupImageUpload?
}

class ContentSetupPhotoEncoder(
    private val context: Context,
) : SetupPhotoEncoder {
    override suspend fun encode(photo: String): SetupImageUpload? =
        withContext(Dispatchers.IO) {
            try {
                decodeDownsampled(photo)?.let { bitmap ->
                    val output = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
                    SetupImageUpload(bytes = output.toByteArray(), fileName = "$FILE_PREFIX${photo.hashCode()}.jpg", mimeType = JPEG_MIME_TYPE)
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                null
            }
        }

    private fun decodeDownsampled(photo: String): Bitmap? {
        val uri = photo.toUri()
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

        var sampleSize = 1
        while (max(bounds.outWidth, bounds.outHeight) / sampleSize > MAX_PHOTO_DIMENSION) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
    }

    private companion object {
        const val MAX_PHOTO_DIMENSION = 1200
        const val JPEG_QUALITY = 85
        const val JPEG_MIME_TYPE = "image/jpeg"
        const val FILE_PREFIX = "setup-"
    }
}
