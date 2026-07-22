package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.core.ui.theme.CoverAzureEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverPineEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverPineStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

object CommunityMockData {
    val feed =
        listOf(
            CommunityPost(
                id = 1,
                author = "@ShadowReaper",
                timeAgo = "2h ago",
                communityName = "RPG Speedrunners",
                title = "Finally hit Platinum on Elden Ring!",
                preview = "After 200+ hours and countless deaths to Malenia, I finally managed to secure the last trophy of the run.",
                likes = 1204,
                comments = 84,
                hasMedia = true,
                isLiked = true,
                avatarStart = CoverVioletStart,
                avatarEnd = CoverVioletEnd,
                mediaStart = CoverEmeraldStart,
                mediaEnd = CoverEmeraldEnd,
            ),
            CommunityPost(
                id = 2,
                author = "@PixelWitcher",
                timeAgo = "5h ago",
                communityName = "Cyberpunk Lore",
                title = "New Night City secrets discovered",
                preview = "The latest patch hid some new QR codes in the Kabuki market area. Has anyone else managed to decode them?",
                likes = 2401,
                comments = 312,
                hasMedia = true,
                avatarStart = CoverPineStart,
                avatarEnd = CoverPineEnd,
                mediaStart = CoverIndigoStart,
                mediaEnd = CoverIndigoEnd,
            ),
            CommunityPost(
                id = 3,
                author = "@RetroGamer",
                timeAgo = "8h ago",
                communityName = "Retro Vault",
                title = "Is the new DLC worth it?",
                preview = "A few hours in and here are my first impressions. The new map is massive and the combat finally feels tight.",
                likes = 85,
                comments = 12,
                hasMedia = false,
                avatarStart = CoverAzureStart,
                avatarEnd = CoverAzureEnd,
                mediaStart = CoverCyanStart,
                mediaEnd = CoverCyanEnd,
            ),
        )

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
            iconStart = CoverEmeraldStart,
            iconEnd = CoverEmeraldEnd,
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
                iconStart = CoverEmeraldStart,
                iconEnd = CoverEmeraldEnd,
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
                iconStart = CoverCrimsonStart,
                iconEnd = CoverCrimsonEnd,
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
                iconStart = CoverVioletStart,
                iconEnd = CoverVioletEnd,
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
                iconStart = CoverIndigoStart,
                iconEnd = CoverIndigoEnd,
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
                iconStart = CoverCrimsonStart,
                iconEnd = CoverCrimsonEnd,
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
                iconStart = CoverVioletStart,
                iconEnd = CoverVioletEnd,
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
                iconStart = CoverPineStart,
                iconEnd = CoverPineEnd,
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
                iconStart = CoverAzureStart,
                iconEnd = CoverAzureEnd,
            ),
        )

    val categories = listOf("All", "RPG", "FPS", "Indie", "Retro", "Speedrun", "Tech")

    val feedFilters = listOf("Latest", "Top Rated", "Following")

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
            iconStart = CoverEmeraldStart,
            iconEnd = CoverEmeraldEnd,
        )

    val communityPosts =
        listOf(
            CommunityPost(
                id = 50,
                author = "@chrono_runner",
                timeAgo = "2h ago",
                communityName = "RPG Speedrunners",
                title = "New glitch found in Chrono Trigger's 1999 AD sequence!",
                preview = "Testing out the latest route for the 100% run and stumbled across a frame-perfect warp that skips the whole tower.",
                likes = 432,
                comments = 24,
                hasMedia = true,
                avatarStart = CoverAzureStart,
                avatarEnd = CoverAzureEnd,
                mediaStart = CoverPineStart,
                mediaEnd = CoverPineEnd,
            ),
            CommunityPost(
                id = 51,
                author = "@master_of_none",
                timeAgo = "5h ago",
                communityName = "RPG Speedrunners",
                title = "Weekly challenge: barebones level 1 boss rush",
                preview = "This week we're tackling the main story bosses without any equipment upgrades. Post your splits below.",
                likes = 1204,
                comments = 156,
                hasMedia = false,
                avatarStart = CoverCrimsonStart,
                avatarEnd = CoverCrimsonEnd,
                mediaStart = CoverCyanStart,
                mediaEnd = CoverCyanEnd,
            ),
        )

    val detailPost =
        CommunityPost(
            id = 60,
            author = "@ShadowReaper",
            timeAgo = "Posted on March 14, 2024",
            communityName = "RPG Speedrunners",
            title = "The state of RPGs in 2024: is the golden age back?",
            preview =
                "After decades of chasing cinematic realism, developers are finally pivoting back to deep mechanical complexity. " +
                    "We're seeing a massive resurgence in turn-based systems and intricate skill trees that actually matter.",
            likes = 1204,
            comments = 24,
            hasMedia = true,
            isLiked = true,
            avatarStart = CoverVioletStart,
            avatarEnd = CoverVioletEnd,
            mediaStart = CoverIndigoStart,
            mediaEnd = CoverIndigoEnd,
        )

    val detailPostHighlights =
        listOf(
            "Emergent narrative choices that ripple through entire campaigns.",
            "Performance optimizations hitting 120FPS on baseline consoles.",
            "Modular difficulty settings catering to both casual and hardcore speedrunners.",
        )

    val comments =
        listOf(
            PostComment(
                id = 70,
                author = "@QuestMaster",
                timeAgo = "2h",
                content =
                    "I absolutely agree. The resurgence of CRPGs specifically has been a breath of fresh air. " +
                        "2024 is looking like a legendary year for build variety.",
                likes = 12,
                avatarStart = CoverVioletStart,
                avatarEnd = CoverVioletEnd,
                replies =
                    listOf(
                        PostComment(
                            id = 71,
                            author = "@LootGoblin",
                            timeAgo = "1h",
                            content = "Specifically the combat mechanics in the latest release — they finally got the balance right!",
                            likes = 5,
                            avatarStart = CoverEmeraldStart,
                            avatarEnd = CoverEmeraldEnd,
                        ),
                    ),
                hiddenReplies = 3,
            ),
            PostComment(
                id = 72,
                author = "@VoidWalker",
                timeAgo = "5h",
                content = "The inventory management is still a mess though. No matter how deep the story is, if the UI is clunky the experience suffers.",
                likes = 8,
                avatarStart = CoverAzureStart,
                avatarEnd = CoverAzureEnd,
            ),
            PostComment(
                id = 73,
                author = "@NeonSamurai",
                timeAgo = "7h",
                content = "Turn-based is back and I could not be happier. Give me a menu and a damage formula over another cinematic set piece.",
                likes = 21,
                isLiked = true,
                avatarStart = CoverCrimsonStart,
                avatarEnd = CoverCrimsonEnd,
            ),
        )

    val members =
        listOf(
            CommunityMember(80, "@ShadowReaper", "Moderator", CoverVioletStart, CoverVioletEnd),
            CommunityMember(81, "@chrono_runner", "Moderator", CoverAzureStart, CoverAzureEnd),
            CommunityMember(82, "@QuestMaster", "Member · joined 2 months ago", CoverEmeraldStart, CoverEmeraldEnd),
            CommunityMember(83, "@VoidWalker", "Member · joined 5 months ago", CoverCrimsonStart, CoverCrimsonEnd),
            CommunityMember(84, "@NeonSamurai", "Member · joined 1 year ago", CoverCyanStart, CoverCyanEnd),
        )

    val quickReactions = listOf("❤️", "🙌", "🔥", "👏", "😢", "😍")
}
