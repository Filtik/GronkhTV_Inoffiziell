package de.filtik.gronkhtv.twitch.helix.streams.key


import de.filtik.gronkhtv.twitch.helix.http.model.AbstractResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object for a stream key [String].
 * One-to-one mapping to the Twitch stream key JSON object.
 * @constructor Creates a new stream key object.
 * @param streamKey The stream key in [String] format.
 */
@Serializable
data class StreamKey(
    @SerialName("stream_key")
    val streamKey: String
) : AbstractResource()