package com.lucasdias.gametrackr.core.pagination

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TEXT_SIZE = 14.sp

@Composable
fun LoadMoreErrorRow(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val retryLabel = stringResource(R.string.load_more_retry)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClickLabel = retryLabel, role = Role.Button, onClick = onRetry)
                .padding(vertical = Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Text(text = stringResource(R.string.load_more_error), color = AppTextSecondary, fontSize = TEXT_SIZE)
        Text(text = retryLabel, color = AppPrimary, fontSize = TEXT_SIZE, fontWeight = FontWeight.SemiBold)
    }
}

@Preview
@Composable
private fun LoadMoreErrorRowPreview() {
    AppPreview {
        LoadMoreErrorRow(onRetry = {})
    }
}
