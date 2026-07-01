package com.lucasdias.gametrackr.di

import com.lucasdias.gametrackr.BuildConfig
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.auth.AuthRepositoryImpl
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import com.lucasdias.gametrackr.core.network.AuthApi
import com.lucasdias.gametrackr.core.network.AuthInterceptor
import com.lucasdias.gametrackr.core.network.RefreshApi
import com.lucasdias.gametrackr.core.network.TokenAuthenticator
import com.lucasdias.gametrackr.feature.auth.AuthViewModel
import com.lucasdias.gametrackr.feature.auth.forgotpassword.ForgotPasswordViewModel
import com.lucasdias.gametrackr.feature.auth.login.LoginViewModel
import com.lucasdias.gametrackr.feature.auth.register.RegisterViewModel
import com.lucasdias.gametrackr.feature.auth.resetpassword.ResetPasswordViewModel
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.VerifyResetCodeViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val AUTH_CLIENT = "authClient"
private const val REFRESH_CLIENT = "refreshClient"

private val baseUrl: String
    get() = BuildConfig.API_BASE_URL.let { if (it.endsWith("/")) it else "$it/" }

private fun retrofit(
    client: OkHttpClient,
    json: Json,
): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

val appModule =
    module {
        single {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        single {
            HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
            }
        }

        single { TokenStore(androidContext()) }
        single { SessionManager() }

        single { AuthInterceptor(get()) }
        single { TokenAuthenticator(get(), get(), get()) }

        single(named(REFRESH_CLIENT)) {
            OkHttpClient
                .Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
        }

        single(named(AUTH_CLIENT)) {
            OkHttpClient
                .Builder()
                .addInterceptor(get<AuthInterceptor>())
                .authenticator(get<TokenAuthenticator>())
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
        }

        single<RefreshApi> {
            retrofit(get(named(REFRESH_CLIENT)), get()).create(RefreshApi::class.java)
        }
        single<AuthApi> {
            retrofit(get(named(AUTH_CLIENT)), get()).create(AuthApi::class.java)
        }

        single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }

        viewModel { AuthViewModel(get(), get()) }
        viewModel { LoginViewModel(get(), androidContext()) }
        viewModel { RegisterViewModel(get(), androidContext()) }
        viewModel { ForgotPasswordViewModel(get(), androidContext()) }
        viewModel { (email: String) -> VerifyResetCodeViewModel(get(), androidContext(), email) }
        viewModel { (email: String, code: String) ->
            ResetPasswordViewModel(get(), androidContext(), email, code)
        }
    }
