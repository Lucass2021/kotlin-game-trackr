package com.lucasdias.gametrackr.core.model

enum class NotificationSetting {
    PUSH,
    FRIEND_REQUESTS,
    COMMUNITY_REPLIES,
}

data class NotificationSettings(
    val push: Boolean = true,
    val friendRequests: Boolean = true,
    val communityReplies: Boolean = true,
) {
    fun isEnabled(setting: NotificationSetting): Boolean =
        when (setting) {
            NotificationSetting.PUSH -> push
            NotificationSetting.FRIEND_REQUESTS -> friendRequests
            NotificationSetting.COMMUNITY_REPLIES -> communityReplies
        }

    fun with(
        setting: NotificationSetting,
        enabled: Boolean,
    ): NotificationSettings =
        when (setting) {
            NotificationSetting.PUSH -> copy(push = enabled)
            NotificationSetting.FRIEND_REQUESTS -> copy(friendRequests = enabled)
            NotificationSetting.COMMUNITY_REPLIES -> copy(communityReplies = enabled)
        }
}
