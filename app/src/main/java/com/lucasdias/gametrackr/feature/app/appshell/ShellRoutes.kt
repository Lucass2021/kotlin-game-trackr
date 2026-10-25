package com.lucasdias.gametrackr.feature.app.appshell

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.lucasdias.gametrackr.feature.app.search.SearchScope

internal object ShellRoutes {
    const val TABS = "tabs"
    const val SEARCH = "search"
    const val SEARCH_ARG_SCOPE = "scope"
    const val SEARCH_ROUTE = "$SEARCH?$SEARCH_ARG_SCOPE={$SEARCH_ARG_SCOPE}"
    const val NOTIFICATIONS = "notifications"
    const val MENU = "menu"
    const val GAME_DETAIL = "gamedetail"
    const val GAME_DETAIL_ARG_SLUG = "slug"
    const val GAME_DETAIL_ROUTE = "$GAME_DETAIL?$GAME_DETAIL_ARG_SLUG={$GAME_DETAIL_ARG_SLUG}"
    const val COMMUNITY_DETAIL = "communitydetail"
    const val POST_DETAIL = "postdetail"
    const val STATS = "stats"
    const val STATS_ARG_OWNER = "owner"
    const val STATS_ROUTE = "$STATS?$STATS_ARG_OWNER={$STATS_ARG_OWNER}"
    const val USER_PROFILE = "userprofile"
    const val USER_PROFILE_ARG_NAME = "name"
    const val USER_PROFILE_ROUTE = "$USER_PROFILE?$USER_PROFILE_ARG_NAME={$USER_PROFILE_ARG_NAME}"
    const val EDIT_PROFILE = "editprofile"
    const val MY_SETUP = "mysetup"
    const val EDIT_SETUP = "editsetup"
    const val EDIT_SETUP_ARG_ID = "setupId"
    const val EDIT_SETUP_ROUTE = "$EDIT_SETUP?$EDIT_SETUP_ARG_ID={$EDIT_SETUP_ARG_ID}"
    const val ACHIEVEMENTS = "achievements"
    const val ACHIEVEMENTS_ARG_OWNER = "owner"
    const val ACHIEVEMENTS_ROUTE = "$ACHIEVEMENTS?$ACHIEVEMENTS_ARG_OWNER={$ACHIEVEMENTS_ARG_OWNER}"
    const val GAME_ACHIEVEMENTS = "gameachievements"
    const val GAME_ACHIEVEMENTS_ARG_ID = "gameId"
    const val GAME_ACHIEVEMENTS_ROUTE = "$GAME_ACHIEVEMENTS/{$GAME_ACHIEVEMENTS_ARG_ID}"
    const val SETTINGS = "settings"
    const val CHANGE_PASSWORD = "changepassword"
    const val CHANGE_PASSWORD_SUCCESS = "changepasswordsuccess"
    const val ABOUT = "about"
    const val HELP = "help"
    const val CREATE_COMMUNITY = "createcommunity"
    const val CREATE_TOPIC = "createtopic"

    fun search(scope: SearchScope) = "$SEARCH?$SEARCH_ARG_SCOPE=${scope.name}"

    fun stats(ownerName: String = "") = "$STATS?$STATS_ARG_OWNER=${Uri.encode(ownerName)}"

    fun achievements(ownerName: String = "") = "$ACHIEVEMENTS?$ACHIEVEMENTS_ARG_OWNER=${Uri.encode(ownerName)}"

    fun gameAchievements(gameId: Long) = "$GAME_ACHIEVEMENTS/$gameId"

    fun gameDetail(slug: String) = "$GAME_DETAIL?$GAME_DETAIL_ARG_SLUG=${Uri.encode(slug)}"

    fun editSetup(setupId: Int? = null) = if (setupId == null) EDIT_SETUP else "$EDIT_SETUP?$EDIT_SETUP_ARG_ID=$setupId"

    fun userProfile(name: String) = "$USER_PROFILE?$USER_PROFILE_ARG_NAME=${Uri.encode(name)}"
}

internal fun NavController.popBackStackIfResumed() {
    if (currentBackStackEntry?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
        popBackStack()
    }
}
