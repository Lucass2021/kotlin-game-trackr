package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CoverPalette

object CommunityMockData {
    val suggested =
        Community(
            id = 10,
            name = "RPG Speedrunners",
            category = "RPG",
            members = "12.4k",
            posts = "340",
            online = "89",
            description = "The ultimate hub for breaking world records in classic and modern RPGs.",
            isJoined = false,
            icon = CoverPalette.EMERALD,
        )

    val featured =
        listOf(
            Community(
                id = 20,
                name = "Elden Ring Lore",
                category = "RPG",
                members = "8.2k",
                posts = "412",
                online = "120",
                description = "Theories, item descriptions and everything the Lands Between refuse to explain.",
                isJoined = false,
                icon = CoverPalette.EMERALD,
            ),
            Community(
                id = 21,
                name = "Retro Vault",
                category = "Retro",
                members = "12k",
                posts = "980",
                online = "203",
                description = "Cartridges, CRTs and high scores. Nostalgia lives here.",
                isJoined = false,
                icon = CoverPalette.CRIMSON,
            ),
            Community(
                id = 22,
                name = "Indie Gems Only",
                category = "Indie",
                members = "8.9k",
                posts = "255",
                online = "64",
                description = "Discovery platform for the little games with the biggest ideas.",
                isJoined = false,
                icon = CoverPalette.VIOLET,
            ),
        )

    val all =
        listOf(
            Community(
                id = 30,
                name = "Final Fantasy VII Rebirth",
                category = "RPG",
                members = "12.4k",
                posts = "530",
                online = "142",
                description = "Sharing the best builds, materia setups and side quest routes.",
                isJoined = true,
                icon = CoverPalette.INDIGO,
            ),
            Community(
                id = 31,
                name = "Valorant Tactics",
                category = "FPS",
                members = "45.2k",
                posts = "2.1k",
                online = "870",
                description = "The ultimate community for lineups, agent picks and ranked climbing.",
                isJoined = false,
                icon = CoverPalette.CRIMSON,
            ),
            Community(
                id = 32,
                name = "Indie Gems Only",
                category = "Indie",
                members = "8.9k",
                posts = "255",
                online = "64",
                description = "Discovery platform for the little games with the biggest ideas.",
                isJoined = false,
                icon = CoverPalette.VIOLET,
            ),
            Community(
                id = 33,
                name = "PC Modding Lab",
                category = "Tech",
                members = "22.1k",
                posts = "1.4k",
                online = "310",
                description = "Advanced techniques for hardware tuning, shaders and texture packs.",
                isJoined = false,
                icon = CoverPalette.PINE,
            ),
            Community(
                id = 34,
                name = "Speedrun Central",
                category = "Speedrun",
                members = "15.3k",
                posts = "770",
                online = "198",
                description = "Frame-perfect tricks, glitch hunting and world record splits.",
                isJoined = true,
                icon = CoverPalette.AZURE,
            ),
        )

    val detailCommunity =
        Community(
            id = 40,
            name = "RPG Speedrunners",
            category = "RPG",
            members = "12.4k",
            posts = "340",
            online = "89",
            description =
                "The ultimate hub for breaking world records in classic and modern RPGs. " +
                    "Share your routes, discuss glitch hunting techniques, and compare splits with the fastest players around.",
            isJoined = false,
            icon = CoverPalette.EMERALD,
            createdMonth = "March 2024",
        )

    val members =
        listOf(
            CommunityMember(80, "@ShadowReaper", "Moderator", CoverPalette.VIOLET, username = "shadowreaper"),
            CommunityMember(81, "@chrono_runner", "Moderator", CoverPalette.AZURE, username = "chrono_runner"),
            CommunityMember(82, "@QuestMaster", "Member · joined 2 months ago", CoverPalette.EMERALD, username = "questmaster"),
            CommunityMember(83, "@VoidWalker", "Member · joined 5 months ago", CoverPalette.CRIMSON, username = "voidwalker"),
            CommunityMember(84, "@NeonSamurai", "Member · joined 1 year ago", CoverPalette.CYAN, username = "neonsamurai"),
        )
}
