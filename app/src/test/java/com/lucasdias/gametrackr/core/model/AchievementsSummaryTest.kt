package com.lucasdias.gametrackr.core.model

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class AchievementsSummaryTest {
    @Test
    fun `summary aggregates every game`() {
        val summary = AchievementsSummary.from(listOf(COMPLETED_GAME, STARTED_GAME))

        assertEquals(3, summary.unlockedCount)
        assertEquals(4, summary.total)
        assertEquals(1, summary.platinumCount)
        assertEquals(1, summary.completedCount)
        assertEquals(2, summary.gameCount)
        assertEquals(75, summary.percent)
    }

    @Test
    fun `summary of no games is empty`() {
        val summary = AchievementsSummary.from(emptyList())

        assertEquals(0, summary.total)
        assertEquals(0, summary.percent)
    }

    private companion object {
        val COMPLETED_GAME =
            game(
                id = 1,
                Achievement(1, "Platinum", "All", AchievementTier.PLATINUM, unlockedAt = "Jul 2024"),
                Achievement(2, "Finish", "End", AchievementTier.GOLD, unlockedAt = "Jun 2024"),
            )

        val STARTED_GAME =
            game(
                id = 2,
                Achievement(3, "Start", "Begin", AchievementTier.BRONZE, unlockedAt = "May 2024"),
                Achievement(4, "Finish", "End", AchievementTier.GOLD),
            )

        fun game(
            id: Long,
            vararg achievements: Achievement,
        ) = GameAchievements(id, "Game $id", "PC", Color.Black, Color.Black, achievements.toList())
    }
}
