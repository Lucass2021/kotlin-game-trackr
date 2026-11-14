package com.lucasdias.gametrackr.di

import com.lucasdias.gametrackr.BuildConfig
import com.lucasdias.gametrackr.core.auth.DataStoreTokenStore
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import com.lucasdias.gametrackr.core.network.AuthApi
import com.lucasdias.gametrackr.core.network.AuthInterceptor
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.ProfileApi
import com.lucasdias.gametrackr.core.network.RefreshApi
import com.lucasdias.gametrackr.core.network.TokenAuthenticator
import com.lucasdias.gametrackr.core.network.TokenRefresher
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val AUTH_CLIENT = "authClient"
private const val REFRESH_CLIENT = "refreshClient"

internal val baseUrl: String
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

val networkModule =
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

        single<TokenStore> { DataStoreTokenStore(androidContext()) }
        single { SessionManager() }

        single { TokenRefresher(get(), get(), get()) }
        single { AuthInterceptor(get(), get()) }
        single { TokenAuthenticator(get()) }

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
        single<CommunityApi> {
            retrofit(get(named(AUTH_CLIENT)), get()).create(CommunityApi::class.java)
        }
        single<ProfileApi> {
            retrofit(get(named(AUTH_CLIENT)), get()).create(ProfileApi::class.java)
        }
        single<GameApi> {
            retrofit(get(named(AUTH_CLIENT)), get()).create(GameApi::class.java)
        }
    }
