package com.lucasdias.gametrackr.feature.auth.register

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.register.components.BackButton
import com.lucasdias.gametrackr.feature.auth.register.components.RegisterBottomSection
import com.lucasdias.gametrackr.feature.auth.register.components.RegisterFormSection
import com.lucasdias.gametrackr.feature.auth.register.components.SocialLoginSection
import com.lucasdias.gametrackr.feature.auth.register.components.TermsAcceptanceRow

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit
) {
    val form = remember { RegisterFormState() }

    AuthScreenScaffold {
        BackButton(onBack = onBack, modifier = Modifier.staggeredAppear(0))

        TitleWithSubtitle(
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle),
            modifier = Modifier
                .padding(top = 16.dp)
                .staggeredAppear(1)
        )

        RegisterFormSection(
            form = form,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(2)
        )

        TermsAcceptanceRow(
            accepted = form.acceptedTerms,
            onToggle = { form.acceptedTerms = !form.acceptedTerms },
            error = form.termsError,
            modifier = Modifier
                .padding(top = 24.dp)
                .staggeredAppear(3)
        )

        RegisterBottomSection(
            isLoading = form.isLoading,
            onCreateAccount = { form.submit() },
            onSignIn = onSignIn,
            modifier = Modifier
                .padding(top = 24.dp)
                .staggeredAppear(4)
        )

        SocialLoginSection(
            onGoogle = { form.signUpGoogle() },
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(5)
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    GameTrackrTheme {
        RegisterScreen(onBack = {}, onSignIn = {})
    }
}
