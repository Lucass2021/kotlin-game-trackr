package com.lucasdias.gametrackr.feature.app.addtolibrary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.core.ui.theme.Sora
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.DateField
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.InteractiveStarRating
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.NumberField
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.PlatformPicker
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.ReviewField
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.StatusSelectGrid
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

private val platforms = listOf("PS5", "PS4", "Xbox Series X|S", "PC", "Nintendo Switch")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLibrarySheet(
    gameTitle: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    coverStart: Color = CoverVioletStart,
    coverEnd: Color = CoverVioletEnd,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var status by rememberSaveable { mutableStateOf(LibraryStatus.PLAYING) }
    var rating by rememberSaveable { mutableIntStateOf(0) }
    var hours by rememberSaveable { mutableStateOf("") }
    var platform by rememberSaveable { mutableStateOf("PS5") }
    var started by rememberSaveable { mutableStateOf<Long?>(null) }
    var finished by rememberSaveable { mutableStateOf<Long?>(null) }
    var review by rememberSaveable { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppBackground,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxHeight(0.92f)) {
            Header(
                gameTitle = gameTitle,
                coverStart = coverStart,
                coverEnd = coverEnd,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                Field(stringResource(R.string.add_library_status)) {
                    StatusSelectGrid(selection = status, onSelect = { status = it })
                }

                Field(stringResource(R.string.add_library_rating)) {
                    InteractiveStarRating(rating = rating, onRatingChange = { rating = it })
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Field(stringResource(R.string.add_library_hours), modifier = Modifier.weight(1f)) {
                        NumberField(
                            value = hours,
                            onValueChange = { hours = it },
                            placeholder = stringResource(R.string.add_library_hours_placeholder),
                        )
                    }
                    Field(stringResource(R.string.add_library_platform), modifier = Modifier.weight(1f)) {
                        PlatformPicker(selection = platform, options = platforms, onSelect = { platform = it })
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Field(stringResource(R.string.add_library_started), modifier = Modifier.weight(1f)) {
                        DateField(
                            dateMillis = started,
                            onDateChange = { started = it },
                            placeholder = stringResource(R.string.add_library_date_placeholder),
                        )
                    }
                    Field(stringResource(R.string.add_library_finished), modifier = Modifier.weight(1f)) {
                        DateField(
                            dateMillis = finished,
                            onDateChange = { finished = it },
                            placeholder = stringResource(R.string.add_library_date_placeholder),
                        )
                    }
                }

                Field(stringResource(R.string.add_library_review)) {
                    ReviewField(
                        value = review,
                        onValueChange = { review = it },
                        placeholder = stringResource(R.string.add_library_review_placeholder),
                    )
                }
            }

            PrimaryButton(
                text = stringResource(R.string.add_library_save),
                onClick = onDismiss,
                icon = AppIcon.ADD_TO_LIBRARY,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
private fun Header(
    gameTitle: String,
    coverStart: Color,
    coverEnd: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        GameCoverArt(start = coverStart, end = coverEnd, width = 56.dp, height = 56.dp)
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = stringResource(R.string.add_library_title),
                color = AppTextPrimary,
                fontFamily = Sora,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = gameTitle,
                color = AppTextSecondary,
                fontSize = 14.sp,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun Field(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionLabel(text = label)
        content()
    }
}
