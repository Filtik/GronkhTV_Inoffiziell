package de.filtik.gronkhtv.twitch.helix.moderation.model

import de.filtik.gronkhtv.twitch.helix.http.model.AbstractResource
import de.filtik.gronkhtv.twitch.helix.shared.NullableInstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class BannedUser(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("user_name")
    val userName: String,
    @SerialName("expires_at")
    @Serializable(with = NullableInstantSerializer::class)
    val expiresAt: Instant? = null
) : AbstractResource()