package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.PostComment

object CommunityPostMockData {
    val feed =
        listOf(
            CommunityPost(
                id = 1,
                author = "@ShadowReaper",
                authorUsername = "shadowreaper",
                timeAgo = "2h ago",
                communityName = "RPG Speedrunners",
                title = "Finally hit Platinum on Elden Ring!",
                preview = "After 200+ hours and countless deaths to Malenia, I finally managed to secure the last trophy of the run.",
                likes = 1204,
                comments = 84,
                hasMedia = true,
                isLiked = true,
                avatar = CoverPalette.VIOLET,
                media = CoverPalette.EMERALD,
            ),
            CommunityPost(
                id = 2,
                author = "@PixelWitcher",
                authorUsername = "pixelwitcher",
                timeAgo = "5h ago",
                communityName = "Cyberpunk Lore",
                title = "New Night City secrets discovered",
                preview = "The latest patch hid some new QR codes in the Kabuki market area. Has anyone else managed to decode them?",
                likes = 2401,
                comments = 312,
                hasMedia = true,
                avatar = CoverPalette.PINE,
                media = CoverPalette.INDIGO,
            ),
            CommunityPost(
                id = 3,
                author = "@RetroGamer",
                authorUsername = "retrogamer",
                timeAgo = "8h ago",
                communityName = "Retro Vault",
                title = "Is the new DLC worth it?",
                preview = "A few hours in and here are my first impressions. The new map is massive and the combat finally feels tight.",
                likes = 85,
                comments = 12,
                hasMedia = false,
                avatar = CoverPalette.AZURE,
                media = CoverPalette.CYAN,
            ),
        )

    val communityPosts =
        listOf(
            CommunityPost(
                id = 50,
                author = "@chrono_runner",
                authorUsername = "chrono_runner",
                timeAgo = "2h ago",
                communityName = "RPG Speedrunners",
                title = "New glitch found in Chrono Trigger's 1999 AD sequence!",
                preview = "Testing out the latest route for the 100% run and stumbled across a frame-perfect warp that skips the whole tower.",
                likes = 432,
                comments = 24,
                hasMedia = true,
                avatar = CoverPalette.AZURE,
                media = CoverPalette.PINE,
            ),
            CommunityPost(
                id = 51,
                author = "@master_of_none",
                authorUsername = "master_of_none",
                timeAgo = "5h ago",
                communityName = "RPG Speedrunners",
                title = "Weekly challenge: barebones level 1 boss rush",
                preview = "This week we're tackling the main story bosses without any equipment upgrades. Post your splits below.",
                likes = 1204,
                comments = 156,
                hasMedia = false,
                avatar = CoverPalette.CRIMSON,
                media = CoverPalette.CYAN,
            ),
        )

    val detailPost =
        CommunityPost(
            id = 60,
            author = "@ShadowReaper",
            authorUsername = "shadowreaper",
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
            avatar = CoverPalette.VIOLET,
            media = CoverPalette.INDIGO,
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
                avatar = CoverPalette.VIOLET,
                replies =
                    listOf(
                        PostComment(
                            id = 71,
                            author = "@LootGoblin",
                            timeAgo = "1h",
                            content = "Specifically the combat mechanics in the latest release — they finally got the balance right!",
                            likes = 5,
                            avatar = CoverPalette.EMERALD,
                        ),
                    ),
            ),
            PostComment(
                id = 72,
                author = "@VoidWalker",
                timeAgo = "5h",
                content = "The inventory management is still a mess though. No matter how deep the story is, if the UI is clunky the experience suffers.",
                likes = 8,
                avatar = CoverPalette.AZURE,
            ),
            PostComment(
                id = 73,
                author = "@NeonSamurai",
                timeAgo = "7h",
                content = "Turn-based is back and I could not be happier. Give me a menu and a damage formula over another cinematic set piece.",
                likes = 21,
                isLiked = true,
                avatar = CoverPalette.CRIMSON,
            ),
        )
}
