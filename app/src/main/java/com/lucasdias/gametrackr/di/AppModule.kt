package com.lucasdias.gametrackr.di

import com.lucasdias.gametrackr.BuildConfig
import com.lucasdias.gametrackr.core.auth.DataStoreTokenStore
import com.lucasdias.gametrackr.core.auth.GoogleAuth
import com.lucasdias.gametrackr.core.auth.GoogleAuthService
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.UserProfile
import com.lucasdias.gametrackr.core.network.AuthApi
import com.lucasdias.gametrackr.core.network.AuthInterceptor
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.ProfileApi
import com.lucasdias.gametrackr.core.network.RefreshApi
import com.lucasdias.gametrackr.core.network.TokenAuthenticator
import com.lucasdias.gametrackr.core.network.TokenRefresher
import com.lucasdias.gametrackr.data.achievements.AchievementsRepository
import com.lucasdias.gametrackr.data.achievements.FakeAchievementsRepository
import com.lucasdias.gametrackr.data.auth.AccountRepository
import com.lucasdias.gametrackr.data.auth.AuthRepository
import com.lucasdias.gametrackr.data.auth.FakeAccountRepository
import com.lucasdias.gametrackr.data.auth.FakePasswordRepository
import com.lucasdias.gametrackr.data.auth.PasswordRepository
import com.lucasdias.gametrackr.data.auth.RemoteAuthRepository
import com.lucasdias.gametrackr.data.community.CommunityFeedRepository
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.DiscoverCommunitiesRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeDiscoverCommunitiesRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import com.lucasdias.gametrackr.data.community.RemoteCommunityRepository
import com.lucasdias.gametrackr.data.game.GameRepository
import com.lucasdias.gametrackr.data.game.RemoteGameRepository
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import com.lucasdias.gametrackr.data.notifications.FakeNotificationSettingsRepository
import com.lucasdias.gametrackr.data.notifications.FakeNotificationsRepository
import com.lucasdias.gametrackr.data.notifications.NotificationSettingsRepository
import com.lucasdias.gametrackr.data.notifications.NotificationsRepository
import com.lucasdias.gametrackr.data.profile.FakeProfileOverviewRepository
import com.lucasdias.gametrackr.data.profile.ProfileOverviewRepository
import com.lucasdias.gametrackr.data.profile.ProfileRepository
import com.lucasdias.gametrackr.data.profile.RemoteProfileRepository
import com.lucasdias.gametrackr.data.profile.RemoteSetupRepository
import com.lucasdias.gametrackr.data.profile.SetupRepository
import com.lucasdias.gametrackr.data.stats.FakeStatsRepository
import com.lucasdias.gametrackr.data.stats.StatsRepository
import com.lucasdias.gametrackr.feature.app.achievements.AchievementsViewModel
import com.lucasdias.gametrackr.feature.app.achievements.GameAchievementsViewModel
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibraryViewModel
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedViewModel
import com.lucasdias.gametrackr.feature.app.community.communitydetail.CommunityDetailViewModel
import com.lucasdias.gametrackr.feature.app.community.createcommunity.CreateCommunityViewModel
import com.lucasdias.gametrackr.feature.app.community.createtopic.CreateTopicViewModel
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel
import com.lucasdias.gametrackr.feature.app.community.postdetail.PostDetailViewModel
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetailViewModel
import com.lucasdias.gametrackr.feature.app.home.HomeViewModel
import com.lucasdias.gametrackr.feature.app.library.LibraryViewModel
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsViewModel
import com.lucasdias.gametrackr.feature.app.profile.ProfileViewModel
import com.lucasdias.gametrackr.feature.app.profile.UserProfileViewModel
import com.lucasdias.gametrackr.feature.app.profile.editprofile.EditProfileViewModel
import com.lucasdias.gametrackr.feature.app.profile.setup.ContentSetupPhotoEncoder
import com.lucasdias.gametrackr.feature.app.profile.setup.EditSetupViewModel
import com.lucasdias.gametrackr.feature.app.profile.setup.MySetupViewModel
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import com.lucasdias.gametrackr.feature.app.search.SearchViewModel
import com.lucasdias.gametrackr.feature.app.settings.SettingsViewModel
import com.lucasdias.gametrackr.feature.app.settings.changepassword.ChangePasswordViewModel
import com.lucasdias.gametrackr.feature.app.stats.StatsViewModel
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

        single<GameRepository> { RemoteGameRepository(get(), get()) }
        single<CommunityRepository> { RemoteCommunityRepository(get(), get()) }
        single<CommunityFeedRepository> { FakeCommunityFeedRepository(get()) }
        single<DiscoverCommunitiesRepository> { FakeDiscoverCommunitiesRepository(get()) }
        single { PostUpdates() }
        single { CommunityUpdates() }
        single<AuthRepository> { RemoteAuthRepository(get(), get(), get(), get()) }
        single<ProfileRepository> { RemoteProfileRepository(get(), get()) }
        single<SetupRepository> { RemoteSetupRepository(get(), get(), baseUrl) }
        single<PasswordRepository> { FakePasswordRepository() }
        single<AccountRepository> { FakeAccountRepository() }
        single<NotificationSettingsRepository> { FakeNotificationSettingsRepository() }
        single<StatsRepository> { FakeStatsRepository() }
        single<LibraryRepository> { FakeLibraryRepository() }
        single { LibraryUpdates() }
        single<NotificationsRepository> { FakeNotificationsRepository() }
        single<AchievementsRepository> { FakeAchievementsRepository() }
        single<ProfileOverviewRepository> { FakeProfileOverviewRepository() }
        single { GoogleAuthService(androidContext(), get()) }
        single<GoogleAuth> { get<GoogleAuthService>() }

        viewModel { DiscoverCommunitiesViewModel(get(), get()) }
        viewModel { CommunityFeedViewModel(get(), get(), get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { (scope: SearchScope) -> SearchViewModel(scope, get()) }
        viewModel { (slug: String) -> GameDetailViewModel(get(), get(), get(), slug) }
        viewModel { AuthViewModel(get(), get()) }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegisterViewModel(get(), get(), androidContext()) }
        viewModel { ForgotPasswordViewModel(get(), androidContext()) }
        viewModel { (email: String) -> VerifyResetCodeViewModel(get(), androidContext(), email) }
        viewModel { (email: String, code: String) ->
            ResetPasswordViewModel(get(), androidContext(), email, code)
        }
        viewModel { params -> CreateTopicViewModel(params.getOrNull(), get(), get()) }
        viewModel { CreateCommunityViewModel(get()) }
        viewModel { ChangePasswordViewModel(get()) }
        viewModel { SettingsViewModel(get(), get()) }
        viewModel { (profile: Profile) -> EditProfileViewModel(profile, get(), get()) }
        viewModel { MySetupViewModel(get()) }
        viewModel { StatsViewModel(get()) }
        viewModel { LibraryViewModel(get(), get()) }
        viewModel { (slug: String, game: GameDetail) -> AddToLibraryViewModel(get(), get(), slug, game) }
        viewModel { AchievementsViewModel(get()) }
        viewModel { (gameId: Long) -> GameAchievementsViewModel(get(), gameId) }
        viewModel { NotificationsViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { (user: UserProfile) -> UserProfileViewModel(get(), user) }
        viewModel { (community: Community) -> CommunityDetailViewModel(get(), get(), get(), get(), community) }
        viewModel { (post: CommunityPost) -> PostDetailViewModel(get(), get(), get(), post) }
        viewModel { params -> EditSetupViewModel(params.getOrNull(), get(), ContentSetupPhotoEncoder(androidContext())) }
    }
