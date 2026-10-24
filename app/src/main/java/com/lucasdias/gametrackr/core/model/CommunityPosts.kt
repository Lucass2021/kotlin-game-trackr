package com.lucasdias.gametrackr.core.model

fun List<CommunityPost>.prependingPost(post: CommunityPost): List<CommunityPost> = listOf(post) + filterNot { it.id == post.id }
