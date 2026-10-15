package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.model.GameScreenshot
import com.lucasdias.gametrackr.core.model.GameSpec
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeGameRepository : GameRepository {
    override suspend fun fetchNewReleases(limit: Int): Result<List<Game>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(games.take(limit))
    }

    override suspend fun fetchMostAnticipated(limit: Int): Result<List<Game>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(games.take(limit))
    }

    override suspend fun fetchGame(slug: String): Result<GameDetail> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(gameDetail)
    }

    override suspend fun fetchAllNewReleases(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(games, page, perPage))
    }

    override suspend fun fetchAllMostAnticipated(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(games, page, perPage))
    }

    override suspend fun searchGames(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(games, page, perPage))
    }

    override suspend fun fetchPlatforms(): Result<List<GamePlatform>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(HomeMockData.platforms)
    }

    private companion object {
        val games = listOf(HomeMockData.sampleGame)

        val gameDetail =
            GameDetail(
                title = "Neon Ascent: Revival",
                year = "2024",
                rating = 4.8,
                platforms = listOf("PS5", "PC"),
                genres = listOf("Action RPG", "Open World", "Cyberpunk"),
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
                heroUrl = null,
                screenshots =
                    listOf(
                        GameScreenshot(CoverIndigoStart, CoverIndigoEnd),
                        GameScreenshot(CoverCyanStart, CoverCyanEnd),
                        GameScreenshot(CoverCrimsonStart, CoverCrimsonEnd),
                        GameScreenshot(CoverEmeraldStart, CoverEmeraldEnd),
                    ),
                about =
                    "Neon Ascent: Revival is the definitive next-gen cyberpunk experience. Set in " +
                        "the sprawling vertical megacity of Aethelgard, players take on the role of a " +
                        "rogue netrunner untangling a conspiracy that reaches from the neon-drenched " +
                        "undercity to the corporate spires above.",
                specs =
                    listOf(
                        GameSpec("Developer", "Void Interactive"),
                        GameSpec("Publisher", "Nova Games"),
                        GameSpec("Released", "Mar 14, 2024"),
                        GameSpec("Modes", "Single player, Co-operative"),
                    ),
            )
    }
}
