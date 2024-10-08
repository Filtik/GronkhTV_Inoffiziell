package de.filtik.gronkhtv.twitch.helix.moderation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BanEventType {
    @SerialName("moderation.user.ban")
    BAN,

    @SerialName("moderation.user.unban")
    UNBAN
}