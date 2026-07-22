package com.lucasdias.gametrackr.feature.app.gamedetail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibrarySheet
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameAboutSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameCommunitySection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHero
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameGenreChip
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameInfoChip
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameScreenshotsSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameSpecificationsSection

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailScreen(
    onBack: () -> Unit,
    onExploreCommunity: () -> Unit,
    game: GameDetail = GameDetailMockData.game,
) {
    var showAddToLibrary by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        GameDetailHero(
            game = game,
            onBack = onBack,
            onShare = {
                val sendIntent =
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out ${game.title} on GameTrackr!")
                    }
                context.startActivity(Intent.createChooser(sendIntent, null))
            },
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = game.title,
                color = AppTextPrimary,
                style = AppType.headline(30.sp),
                lineHeight = 36.sp,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GameInfoChip(text = game.year)
                GameInfoChip(text = String.format("%.1f", game.rating), isRating = true)
                game.platforms.forEach { GameInfoChip(text = it) }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                game.genres.forEach { GameGenreChip(text = it) }
            }

            PrimaryButton(
                text = stringResource(R.string.game_detail_add_to_library),
                onClick = { showAddToLibrary = true },
                icon = AppIcon.ADD_TO_LIBRARY,
            )
        }

        GameScreenshotsSection(screenshots = game.screenshots)

        GameAboutSection(about = game.about)

        GameCommunitySection(discussions = game.discussions, onSeeAll = onExploreCommunity)

        GameSpecificationsSection(
            specs = game.specs,
            systemRequirements = game.systemRequirements,
            showSystemRequirements = game.supportsPC,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }

    if (showAddToLibrary) {
        AddToLibrarySheet(
            gameTitle = game.title,
            coverStart = game.coverStart,
            coverEnd = game.coverEnd,
            onDismiss = { showAddToLibrary = false },
        )
    }
}
