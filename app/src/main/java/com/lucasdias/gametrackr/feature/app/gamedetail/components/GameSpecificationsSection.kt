package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Sora
import com.lucasdias.gametrackr.feature.app.gamedetail.GameSpec
import com.lucasdias.gametrackr.feature.app.gamedetail.SystemRequirementTier

@Composable
fun GameSpecificationsSection(
    specs: List<GameSpec>,
    systemRequirements: List<SystemRequirementTier>,
    showSystemRequirements: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        GameSectionHeader(title = stringResource(R.string.game_detail_specifications))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            specs.chunked(2).forEach { rowSpecs ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowSpecs.forEach { spec ->
                        SpecCard(spec = spec, modifier = Modifier.weight(1f))
                    }
                    if (rowSpecs.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        if (showSystemRequirements) {
            Text(
                text = stringResource(R.string.game_detail_system_requirements),
                color = AppTextPrimary,
                fontFamily = Sora,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp),
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                systemRequirements.forEach { RequirementTierCard(it) }
            }
        }
    }
}

@Composable
private fun SpecCard(
    spec: GameSpec,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier =
            modifier
                .heightIn(min = 64.dp)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = spec.label.uppercase(),
            color = AppTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp,
        )
        Text(
            text = spec.value,
            color = AppTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun RequirementTierCard(tier: SystemRequirementTier) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(AppPrimary))
            Text(
                text = tier.name,
                color = AppTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tier.items.forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = item.label,
                        color = AppTextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.width(64.dp),
                    )
                    Text(
                        text = item.value,
                        color = AppTextPrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
