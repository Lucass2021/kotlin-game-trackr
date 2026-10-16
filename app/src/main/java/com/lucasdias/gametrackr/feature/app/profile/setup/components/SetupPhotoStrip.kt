package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupImage
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val THUMBNAIL_SIZE = 108.dp

@Composable
fun SetupPhotoStrip(
    images: List<SetupImage>,
    newPhotos: List<String>,
    canAddPhotos: Boolean,
    onRemoveImage: (Int) -> Unit,
    onRemoveNewPhoto: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()).padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        images.forEach { image ->
            Thumbnail(model = image.url, onRemove = { onRemoveImage(image.id) })
        }
        newPhotos.forEach { photo ->
            Thumbnail(model = photo, onRemove = { onRemoveNewPhoto(photo) })
        }

        if (canAddPhotos) {
            AddPhotoTile(onClick = onAdd)
        }
    }
}

@Composable
private fun Thumbnail(
    model: String,
    onRemove: () -> Unit,
) {
    val removeInteraction = remember { MutableInteractionSource() }
    val removeLabel = stringResource(R.string.setup_remove_photo)

    Box(modifier = Modifier.size(THUMBNAIL_SIZE)) {
        AsyncImage(
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppSurfaceCard),
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .pressScale(removeInteraction)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                        interactionSource = removeInteraction,
                        indication = null,
                        onClickLabel = removeLabel,
                        role = Role.Button,
                        onClick = onRemove,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.CLOSE.image(),
                contentDescription = removeLabel,
                tint = Color.White,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Composable
private fun AddPhotoTile(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val label = stringResource(R.string.setup_add_photo)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(THUMBNAIL_SIZE)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(22.dp),
        )
        Text(text = label, color = AppTextSecondary, style = AppType.body(12.sp))
    }
}

@Preview
@Composable
private fun SetupPhotoStripPreview() {
    AppPreview {
        SetupPhotoStrip(
            images = emptyList(),
            newPhotos = emptyList(),
            canAddPhotos = true,
            onRemoveImage = {},
            onRemoveNewPhoto = {},
            onAdd = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
