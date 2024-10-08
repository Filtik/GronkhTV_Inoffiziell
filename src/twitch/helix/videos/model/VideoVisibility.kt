package de.filtik.gronkhtv.twitch.helix.videos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VideoVisibility {
    @SerialName("public")
    PUBLIC,

    @SerialName("private")
    PRIVATE
}