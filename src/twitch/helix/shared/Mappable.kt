package de.filtik.gronkhtv.twitch.helix.shared

/**
 * Represents all objects that can be converted to a [Map].
 */
interface Mappable {

    /**
     * Converts the object to a [Map].
     */
    fun toMap(): Map<String, Any?>

}