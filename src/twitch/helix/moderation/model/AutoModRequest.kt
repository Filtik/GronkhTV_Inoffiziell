package de.filtik.gronkhtv.twitch.helix.moderation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutoModRequest(
    @SerialName("data")
    val data: Collection<AutoModMessage>
)