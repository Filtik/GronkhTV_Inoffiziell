package de.filtik.gronkhtv.twitch.helix.moderation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModeratorEventType {
    @SerialName("moderation.moderator.remove")
    REMOVE,

    @SerialName("moderation.moderator.add")
    ADD
}