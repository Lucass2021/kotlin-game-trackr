package com.lucasdias.gametrackr.di

import com.lucasdias.gametrackr.core.auth.GoogleAuth
import com.lucasdias.gametrackr.core.auth.GoogleAuthService
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule =
    module {
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
    }
