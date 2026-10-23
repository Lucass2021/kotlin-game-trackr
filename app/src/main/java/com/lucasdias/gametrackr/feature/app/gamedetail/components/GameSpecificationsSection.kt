package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameSpec
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository

private val LABEL_SIZE = 11.sp
private val LABEL_TRACKING = 0.8.sp
private val VALUE_SIZE = 15.sp
private val BORDER_WIDTH = 1.dp
private const val COLUMNS = 2

@Composable
fun GameSpecificationsSection(
    specs: List<GameSpec>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameSectionHeader(title = stringResource(R.string.game_detail_specifications))

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            specs.chunked(COLUMNS).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    row.forEach { spec ->
                        SpecCard(spec = spec, modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                    repeat(COLUMNS - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun SpecCard(
    spec: GameSpec,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.md)
    Column(
        modifier =
            modifier
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Text(
            text = spec.label.uppercase(),
            color = AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            letterSpacing = LABEL_TRACKING,
        )
        Text(
            text = spec.value,
            color = AppTextPrimary,
            style = AppType.label(VALUE_SIZE),
        )
    }
}

@Preview
@Composable
private fun GameSpecificationsSectionPreview() {
    AppPreview {
        GameSpecificationsSection(specs = FakeGameRepository.gameDetail.specs)
    }
}
