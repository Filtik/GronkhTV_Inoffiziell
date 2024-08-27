package de.filtik.gronkhtv.twitch.helix.users.model


import de.filtik.gronkhtv.twitch.helix.http.model.AbstractResource
import de.filtik.gronkhtv.twitch.helix.shared.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class FollowEvent(
    @SerialName("from_id")
    val fromUserId: String,
    @SerialName("from_name")
    val fromUserName: String,
    @SerialName("to_id")
    val toUserId: String,
    @SerialName("to_name")
    val toUserName: String,
    @SerialName("followed_at")
    @Serializable(with = InstantSerializer::class)
    val followedAt: Instant
) : AbstractResource()