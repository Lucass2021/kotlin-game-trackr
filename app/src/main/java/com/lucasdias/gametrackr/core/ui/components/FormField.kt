package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel

private val COUNTER_SIZE = 12.sp
private val MESSAGE_SIZE = 13.sp

@Composable
fun FormField(
    label: String,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    hint: String? = null,
    counter: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(text = label, modifier = Modifier.weight(1f))
            if (counter != null) {
                Text(
                    text = counter.toString(),
                    color = if (counter < 0) AppTertiary else AppTextSecondary,
                    style = AppType.body(COUNTER_SIZE),
                )
            }
        }

        content()

        when {
            error != null -> Text(text = error.asString(), color = AppTertiary, style = AppType.body(MESSAGE_SIZE))
            hint != null -> Text(text = hint, color = AppTextSecondary, style = AppType.body(MESSAGE_SIZE))
        }
    }
}

@Preview
@Composable
private fun FormFieldPreview() {
    AppPreview {
        FormField(
            label = "Bio",
            error = UiText.Resource(R.string.validation_bio_too_long, listOf(1000)),
            counter = -12,
        ) {
            Text(text = "Field content", color = AppTextSecondary)
        }
    }
}

@Preview
@Composable
private fun FormFieldHintPreview() {
    AppPreview {
        FormField(label = "Name", hint = "No spaces — this is how the community is listed.") {
            Text(text = "Field content", color = AppTextSecondary)
        }
    }
}
