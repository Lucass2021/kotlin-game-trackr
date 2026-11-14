package com.lucasdias.gametrackr.di

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.model.Profile
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
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { DiscoverCommunitiesViewModel(get(), get()) }
        viewModel { CommunityFeedViewModel(get(), get(), get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { (scope: SearchScope) -> SearchViewModel(scope = scope, repository = get(), savedStateHandle = get()) }
        viewModel { (slug: String) -> GameDetailViewModel(get(), get(), get(), slug) }
        viewModel { AuthViewModel(get(), get()) }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegisterViewModel(get(), get()) }
        viewModel { ForgotPasswordViewModel(get()) }
        viewModel { (email: String) -> VerifyResetCodeViewModel(get(), email) }
        viewModel { (email: String, code: String) ->
            ResetPasswordViewModel(get(), email, code)
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
        viewModel { (username: String) -> UserProfileViewModel(get(), username) }
        viewModel { (community: Community) -> CommunityDetailViewModel(get(), get(), get(), get(), community) }
        viewModel { (post: CommunityPost) -> PostDetailViewModel(get(), get(), get(), post) }
        viewModel { params -> EditSetupViewModel(params.getOrNull(), get(), ContentSetupPhotoEncoder(androidContext())) }
    }
