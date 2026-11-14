package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.PostComment

val Community.iconStart: Color get() = icon.start

val Community.iconEnd: Color get() = icon.end

val CommunityPost.avatarStart: Color get() = avatar.start

val CommunityPost.avatarEnd: Color get() = avatar.end

val CommunityPost.mediaStart: Color get() = media.start

val CommunityPost.mediaEnd: Color get() = media.end

val CommunityMember.avatarStart: Color get() = avatar.start

val CommunityMember.avatarEnd: Color get() = avatar.end

val PostComment.avatarStart: Color get() = avatar.start

val PostComment.avatarEnd: Color get() = avatar.end
