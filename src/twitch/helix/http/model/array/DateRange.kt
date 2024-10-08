package de.filtik.gronkhtv.twitch.helix.http.model.array


import de.filtik.gronkhtv.twitch.helix.shared.NullableInstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A data transfer object that represents the interval between two timestamps.
 * @constructor Creates a new date range object.
 * @param startedAt The starting timestamp, that will be converted from a [String] to an [Instant]
 * @param endedAt The ending timestamp, that will be converted from a [String] to an [Instant]
 */

@Serializable
data class DateRange(
    @SerialName("started_at")
    @Serializable(with = NullableInstantSerializer::class)
    val startedAt: Instant? = null,
    @SerialName("ended_at")
    @Serializable(with = NullableInstantSerializer::class)
    val endedAt: Instant? = null
)