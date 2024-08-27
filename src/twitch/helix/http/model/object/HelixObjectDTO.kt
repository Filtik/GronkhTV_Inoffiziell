package de.filtik.gronkhtv.twitch.helix.http.model.`object`

import de.filtik.gronkhtv.twitch.helix.http.model.AbstractResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A wrapper data transfer object, that contains the fields
 * used in the Twitch Helix endpoints that return a single object and not a collection.
 * @constructor Creates a new Helix data transfer wrapper object used for single responses.
 * @param resource The resource to be retrieved.
 */
@Serializable
data class HelixObjectDTO<T : AbstractResource>(
    @SerialName("data")
    val resource: T
)